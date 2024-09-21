package com.teamdimensional.preordain.core.document;

import com.google.common.collect.ImmutableList;
import com.teamdimensional.preordain.core.function.PreordainFunction;
import com.teamdimensional.preordain.library.serialization.DataSerializers;
import com.teamdimensional.preordain.renderer.PreordainRenderRegion;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PreordainDocument {

    private String key = null;
    private String parent = null;
    private Integer[] size = null;
    private List<PreordainFunction> functions = new ArrayList<>();
    private boolean visible = true;
    private String title = "";
    private String[] links = null;

    public Integer[] getSize() {
        return size;
    }

    public ImmutableList<PreordainFunction> getFunctions() {
        return ImmutableList.copyOf(functions);
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
            if (size == null) size = parentDoc.getSize();
            functions = Stream.concat(parentDoc.getFunctions().stream(), functions.stream()).collect(Collectors.toList());
        }
        if (size == null || size.length != 3) {
            throw new IllegalArgumentException("Preordain documents must have a size field with exactly 3 integers");
        }
        if (links != null) {
            for (String s : links) {
                ItemStack stack = DataSerializers.getStack(s);
                DocumentItemLinker.registerLink(this, stack);
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

    public void apply(PreordainRenderRegion region) {
        region.setSize(size);
        for (PreordainFunction function : functions) {
            function.apply(region);
        }
    }

}
