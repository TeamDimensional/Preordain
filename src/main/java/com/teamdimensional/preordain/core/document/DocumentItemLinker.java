package com.teamdimensional.preordain.core.document;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;

public class DocumentItemLinker {
    // TODO: study https://github.com/westernat/Create-Ponder/blob/ponder/src/main/java/com/simibubi/create/foundation/ponder/PonderTooltipHandler.java

    public static final Map<String, PreordainDocument> links = new Object2ObjectOpenHashMap<>();

    private static String stringify(ItemStack it) {
        return Objects.requireNonNull(it.getItem().getRegistryName()) + ":" + it.getMetadata();
    }

    public static void registerLink(PreordainDocument doc, ItemStack it) {
        links.put(stringify(it), doc);
    }

    private static @Nullable PreordainDocument getLink(ItemStack it) {
        return links.get(stringify(it));
    }

}
