package com.teamdimensional.preordain.core.document;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonParseException;
import com.teamdimensional.preordain.Preordain;
import com.teamdimensional.preordain.core.function.PreordainFunction;
import com.teamdimensional.preordain.library.serialization.DataSerializers;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PreordainDocument {

    private String key = null;
    private String parent = null;
    private List<PreordainFunction> functions = new ArrayList<>();
    private boolean visible = true;
    private String title = "";
    private String[] links = null;

    public ImmutableList<PreordainFunction> getFunctions() {
        return ImmutableList.copyOf(functions);
    }
    public String getTitle() {
        return title;
    }

    private transient int initState = 0;

    public void initialize() {
        if (initState == 1) throw new IllegalArgumentException("Illegal loop detected while resolving parents");
        if (initState == 2) return;
        initState = 1;

        if (parent != null) {
            if (!DocumentLoader.documents.containsKey(parent)) {
                throw new IllegalArgumentException("Unknown document parent: " + parent);
            }
            PreordainDocument parentDoc = DocumentLoader.documents.get(parent);
            parentDoc.initialize();
            functions = Stream.concat(parentDoc.getFunctions().stream(), functions.stream()).collect(Collectors.toList());
        }
        if (links != null) {
            try {
                for (String s : links) {
                    ItemStack stack = DataSerializers.getStack(s);
                    DocumentItemLinker.registerLink(this, stack);
                }
            } catch (JsonParseException e) {
                Preordain.LOGGER.warn("Unknown item to bind document {} to! It won't be displayed.", key);
            }
        }

        initState = 2;
    }

    void markInitialized() {
        initState = 2;
    }

    public String getKey() {
        if (key == null) {
            throw new IllegalArgumentException("All Preordain documents must have a key");
        }
        return key;
    }

    public void initialize(PreordainPlanner planner) {
        for (PreordainFunction function : functions) {
            planner.register(function, function.getDelay());
        }
    }

}
