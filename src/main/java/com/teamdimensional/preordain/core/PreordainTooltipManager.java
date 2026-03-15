package com.teamdimensional.preordain.core;

import com.teamdimensional.preordain.core.document.DocumentItemLinker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

@Mod.EventBusSubscriber
public class PreordainTooltipManager {
    public static final PreordainTooltipManager INSTANCE = new PreordainTooltipManager();
    public static final KeyBinding PREORDAIN_KEY = new KeyBinding("keybind.preordain.observe", Keyboard.KEY_W, "keybind.preordain.category");

    private static final int TOTAL_CHARS = 12;
    private static final int TICKS_PER_CHAR = 3;

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
            if (timeSpent >= TOTAL_CHARS * TICKS_PER_CHAR) {
                timeSpent = 0;
                ItemStack theStack = managedStack;
                managedStack = null;
                DocumentItemLinker.showDocumentForItem(theStack);
            }
        }
    }

    private static ItemStack getHoveredItem() {
        Container c = Minecraft.getMinecraft().player.openContainer;
        if (c == null) return null;
        GuiScreen sc = Minecraft.getMinecraft().currentScreen;
        if (sc == null) return null;
        int xPos = Mouse.getEventX() * Minecraft.getMinecraft().displayWidth / sc.width;
        int yPos = Mouse.getEventY() * Minecraft.getMinecraft().displayHeight / sc.height;
        for (Slot s : c.inventorySlots) {
            if (s.xPos <= xPos && xPos < s.xPos + 16 && s.yPos <= yPos && yPos < s.yPos + 16) {
                return s.getStack();
            }
        }
        return null;
    }

    public void manageTooltip(ItemTooltipEvent event) {
        updateManagedStack(event.getItemStack());

        String tooltip;
        if (timeSpent == 0) {
            tooltip = I18n.format("tooltip.preordain.observe", PREORDAIN_KEY.getDisplayName());
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i * TICKS_PER_CHAR < timeSpent; i++) sb.append('=');
            sb.append('>');
            for (int i = sb.length(); i < TOTAL_CHARS; i++) sb.append(' ');
            tooltip = sb.toString();
        }
        event.getToolTip().add(tooltip);
    }
}
