package com.teamdimensional.preordain.renderer;

import com.teamdimensional.preordain.Preordain;
import com.teamdimensional.preordain.core.document.PreordainDocument;
import com.teamdimensional.preordain.renderer.ponder.scene.GuiPonder;
import com.teamdimensional.preordain.renderer.ponder.world.WorldPonder;
import net.minecraft.client.Minecraft;

public class PreordainRenderingManager {
    public static void showDocument(PreordainDocument doc) {
        Preordain.LOGGER.debug("Showing document: {}", doc.getKey());

        GuiPonder gui = new GuiPonder(
                Minecraft.getMinecraft().currentScreen,
                new WorldPonder(Minecraft.getMinecraft().profiler, doc));
        Minecraft.getMinecraft().displayGuiScreen(gui);
    }
}
