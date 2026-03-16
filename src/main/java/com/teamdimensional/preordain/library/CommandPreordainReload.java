package com.teamdimensional.preordain.library;

import com.teamdimensional.preordain.Preordain;

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
        Preordain.loader.init();
        sender.sendMessage(new TextComponentString("Reloaded " + Preordain.loader.documents.size() + " documents!"));
    }

}
