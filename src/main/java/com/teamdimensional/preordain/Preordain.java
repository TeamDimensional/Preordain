package com.teamdimensional.preordain;

import com.teamdimensional.preordain.core.PreordainTooltipManager;
import com.teamdimensional.preordain.core.document.DocumentLoader;
import com.teamdimensional.preordain.library.function.PreordainFunctions;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION, clientSideOnly = true)
public class Preordain {

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);

    /**
     * <a href="https://cleanroommc.com/wiki/forge-mod-development/event#overview">
     *     Take a look at how many FMLStateEvents you can listen to via the @Mod.EventHandler annotation here
     * </a>
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("Loading Preordain configurations...");
        PreordainFunctions.init();
        DocumentLoader.load(event.getSuggestedConfigurationFile().getParentFile().getParentFile());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LOGGER.info("Initializing Preordain registries...");
        DocumentLoader.init();
        PreordainTooltipManager.init();
    }

}
