package com.teamdimensional.preordain.library;

import com.teamdimensional.preordain.Preordain;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandPreordainReload extends CommandBase {

    @Override
    public String getName() {
        return "preordainreload";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "preordainreload";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        Preordain.loader.load();
        String text;
        if (Preordain.loader.init()) {
            text = I18n.format("command.preordain_reload.success", Preordain.loader.count());
        } else {
            text = I18n.format("command.preordain_reload.failure");
        }
        sender.sendMessage(new TextComponentString(text));
    }

}
