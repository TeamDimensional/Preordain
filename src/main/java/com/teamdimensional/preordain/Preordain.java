package com.teamdimensional.preordain;

import com.teamdimensional.preordain.core.PreordainTooltipManager;
import com.teamdimensional.preordain.core.document.DocumentLoader;
import com.teamdimensional.preordain.library.CommandPreordainReload;
import com.teamdimensional.preordain.library.function.PreordainFunctions;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION, clientSideOnly = true)
public class Preordain {

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);
    public static DocumentLoader loader;

    /**
     * <a href="https://cleanroommc.com/wiki/forge-mod-development/event#overview">
     *     Take a look at how many FMLStateEvents you can listen to via the @Mod.EventHandler annotation here
     * </a>
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("Loading Preordain configurations...");
        PreordainFunctions.init();
        loader = new DocumentLoader(event.getSuggestedConfigurationFile().getParentFile().getParentFile());
        loader.load();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LOGGER.info("Initializing Preordain registries...");
        loader.init();
        PreordainTooltipManager.init();
    }

    @Mod.EventHandler
    public void serverInit(FMLServerStartingEvent event) {
        LOGGER.info("Initializing Preordain commands...");
        ClientCommandHandler.instance.registerCommand(new CommandPreordainReload());
    }

}
