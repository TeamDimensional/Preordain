package com.teamdimensional.preordain.core.document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.teamdimensional.preordain.core.function.PreordainFunction;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class DocumentChecker {
    private final Map<String, PreordainDocument> docs;
    private final Map<String, CheckStatus> statuses = new Object2ObjectOpenHashMap<>();
    private final Map<String, String> dependencies = new Object2ObjectOpenHashMap<>();

    public static enum CheckStatus {
        UNCHECKED,
        CHECKING,
        CHECKED,
    }

    public static class DocumentCheckingException extends Exception {
        public final List<String> dependencyChain;

        DocumentCheckingException(String message, List<String> chain) {
            super(message);
            this.dependencyChain = chain;
        }
    }

    public DocumentChecker(Map<String, PreordainDocument> docs) {
        this.docs = docs;
    }

    public void check() throws DocumentCheckingException {
        for (String key : docs.keySet()) {
            check(key);
        }
    }

    private List<String> getChain(String key) {
        Set<String> items = new HashSet<>();
        List<String> out = new ArrayList<>();
        while (!out.contains(key)) {
            out.add(key);
            items.add(key);
            key = dependencies.get(key);
        }
        return out;
    }

    public void check(String key) throws DocumentCheckingException {
        CheckStatus status = statuses.getOrDefault(key, CheckStatus.UNCHECKED);
        switch (status) {
            case CHECKED:
                break;
            case CHECKING:
                throw new DocumentCheckingException("Recursion while loading preordains", getChain(key));
            case UNCHECKED:
                statuses.put(key, CheckStatus.CHECKING);
                check(docs.get(key));
                statuses.put(key, CheckStatus.CHECKED);
                break;
        }
    }

    private void check(PreordainDocument doc) throws DocumentCheckingException {
        for (PreordainFunction func : doc.getFunctions()) {
            func.check(this);
        }
    }

}
