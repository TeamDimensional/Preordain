package com.teamdimensional.preordain.renderer.ponder.world;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class WorldProviderPonder extends WorldProvider {
    @Override
    public DimensionType getDimensionType() {
        return DimensionType.OVERWORLD;
    }
}
