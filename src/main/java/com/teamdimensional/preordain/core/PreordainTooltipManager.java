package com.teamdimensional.preordain.core;

import com.teamdimensional.preordain.Preordain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

import org.lwjgl.input.Keyboard;

@Mod.EventBusSubscriber
public class PreordainTooltipManager {
    public static final PreordainTooltipManager INSTANCE = new PreordainTooltipManager();
    public static final KeyBinding PREORDAIN_KEY = new KeyBinding("keybind.preordain.observe", Keyboard.KEY_W, "keybind.preordain.category");

    private static final int TICKS_TO_FULL = 32;
    // How far is the progress bar from the adjacent lines?
    private static final int Y_GAP = 1;
    private static final int TOOLTIP_BORDER = 5;

    public static void init() {
        ClientRegistry.registerKeyBinding(PREORDAIN_KEY);
    }

    private static ItemStack managedStack = null;
    private static int timeSpent = 0;

    private static void updateManagedStack(ItemStack newStack) {
        if (newStack == null || managedStack == null || !newStack.isItemEqual(managedStack)) {
            timeSpent = 0;
        }
        managedStack = newStack;
    }

    @SubscribeEvent
    public static void addTooltip(ItemTooltipEvent event) {
        if (event.getEntityPlayer() == null)
            return;
        if (Preordain.loader.linker.hasLink(event.getItemStack())) {
            updateManagedStack(event.getItemStack());

            String tooltip;
            if (timeSpent == 0) {
                tooltip = I18n.format("tooltip.preordain.observe", PREORDAIN_KEY.getDisplayName());
            } else {
                // Add a newline so we can draw something in that space.
                tooltip = "";
            }
            event.getToolTip().add(tooltip);
        }
    }

    @SubscribeEvent
    public static void drawTooltip(RenderTooltipEvent.PostText event) {
        if (managedStack == null || !PREORDAIN_KEY.isKeyDown()) {
            return;
        }

        List<String> lines = event.getLines();
        int yPos = event.getY() + TOOLTIP_BORDER + Y_GAP;
        for (int i = 0; i < lines.size(); i++) {
            if (TextFormatting.getTextWithoutFormattingCodes(lines.get(i)).isEmpty()) {
                // Technically another mod may opt to add an empty line,
                // so we will be rendering in wrong place, but it's fine.
                drawPreordainRectangle(timeSpent, event.getX(), yPos,
                    event.getWidth(), event.getFontRenderer().FONT_HEIGHT - 2 * Y_GAP);
                break;
            }
            yPos += event.getFontRenderer().getWordWrappedHeight(lines.get(i), event.getWidth());
        }
    }

    private static void drawPreordainRectangle(int ticks, int x, int y, int fullWidth, int height) {
        int width = fullWidth * ticks / TICKS_TO_FULL;
        Gui.drawRect(x, y, x + width, y + height, 0xFFFFFFFF);
    }

    @SubscribeEvent
    public static void doTick(TickEvent.ClientTickEvent e) {

        EntityPlayer player = Minecraft.getMinecraft().player;
        if (e.phase == TickEvent.Phase.START || player == null || player.openContainer == null) {
            return;
        }

        KeyBinding.updateKeyBindState();
        if (!PREORDAIN_KEY.isKeyDown()) {
            managedStack = null;
            timeSpent = 0;
            return;
        }

        if (managedStack != null) {
            timeSpent++;
            if (timeSpent >= TICKS_TO_FULL) {
                timeSpent = 0;
                ItemStack theStack = managedStack;
                managedStack = null;
                Preordain.loader.linker.showDocumentForItem(theStack);
            }
        }
    }
}
