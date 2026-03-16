package com.teamdimensional.preordain.core.document;

import com.google.common.collect.ImmutableList;
import com.teamdimensional.preordain.core.function.PreordainFunction;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PreordainDocument {

    private String key = null;
    private List<PreordainFunction> functions = new ArrayList<>();
    private String title = "";
    private ItemStack[] links = null;

    public ImmutableList<PreordainFunction> getFunctions() {
        return ImmutableList.copyOf(functions);
    }
    public String getTitle() {
        return title;
    }

    public void loadLinks(DocumentItemLinker linker) {
        if (links != null) {
            for (ItemStack stack : links) {
                linker.registerLink(this, stack);
            }
        }
    }

    public String getKey() {
        if (key == null) {
            throw new IllegalArgumentException("All Preordain documents must have a key");
        }
        return key;
    }

    public void initialize(PreordainPlanner planner, long delay) {
        for (PreordainFunction function : functions) {
            planner.register(function, function.getDelay() + delay);
        }
    }

    public void initialize(PreordainPlanner planner) {
        initialize(planner, 0);
    }

}
