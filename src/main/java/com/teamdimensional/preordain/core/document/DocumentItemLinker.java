package com.teamdimensional.preordain.core.document;

import com.teamdimensional.preordain.Preordain;
import com.teamdimensional.preordain.core.PreordainTooltipManager;
import com.teamdimensional.preordain.renderer.PreordainRenderingManager;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;

@Mod.EventBusSubscriber
public class DocumentItemLinker {
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

    @SubscribeEvent
    public static void addTooltip(ItemTooltipEvent event) {
        if (event.getEntityPlayer() == null) return;
        String key = stringify(event.getItemStack());
        if (links.containsKey(key)) {
            PreordainTooltipManager.INSTANCE.manageTooltip(event);
        }
    }

    public static void showDocumentForItem(ItemStack stack) {
        String key = stringify(stack);
        if (links.containsKey(key)) {
            PreordainRenderingManager.showDocument(links.get(key));
        } else {
            Preordain.LOGGER.error("Undocumented item: {}! This should not happen.", stack);
        }
    }

}
