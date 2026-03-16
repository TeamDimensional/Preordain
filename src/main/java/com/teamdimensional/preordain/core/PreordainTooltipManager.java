package com.teamdimensional.preordain.core;

import com.teamdimensional.preordain.Preordain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

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
    public static void addTooltip(ItemTooltipEvent event) {
        if (event.getEntityPlayer() == null)
            return;
        if (Preordain.loader.linker.hasLink(event.getItemStack())) {
            PreordainTooltipManager.INSTANCE.manageTooltip(event);
        }
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
                Preordain.loader.linker.showDocumentForItem(theStack);
            }
        }
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
