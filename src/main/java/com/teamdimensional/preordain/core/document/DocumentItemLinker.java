package com.teamdimensional.preordain.core.document;

import com.teamdimensional.preordain.Preordain;
import com.teamdimensional.preordain.library.RevertibleRegistry;
import com.teamdimensional.preordain.renderer.PreordainRenderingManager;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;

@Mod.EventBusSubscriber
public class DocumentItemLinker {
    final RevertibleRegistry<Map<String, PreordainDocument>> links
        = new RevertibleRegistry<>(Object2ObjectOpenHashMap::new);

    private static String stringify(ItemStack it) {
        return Objects.requireNonNull(it.getItem().getRegistryName()) + ":" + it.getMetadata();
    }

    public void registerLink(PreordainDocument doc, ItemStack it) {
        links.get().put(stringify(it), doc);
    }

    private @Nullable PreordainDocument getLink(ItemStack it) {
        return links.get().get(stringify(it));
    }

    public void showDocumentForItem(ItemStack stack) {
        String key = stringify(stack);
        if (links.get().containsKey(key)) {
            PreordainRenderingManager.showDocument(links.get().get(key));
        } else {
            Preordain.LOGGER.error("Undocumented item: {}! This should not happen.", stack);
        }
    }

    public boolean hasLink(ItemStack it) {
        return getLink(it) != null;
    }

}
