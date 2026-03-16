package com.teamdimensional.preordain.library.function;

import com.teamdimensional.preordain.core.function.PreordainFunction;
import com.teamdimensional.preordain.renderer.ponder.world.WorldPonder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class FunctionFill extends PreordainFunction {

    private AxisAlignedBB bbox;
    private IBlockState state;

    @Override
    public void apply(WorldPonder world) {
        for (int i = (int) bbox.minX; i <= bbox.maxX; i++) {
            for (int j = (int) bbox.minY; j <= bbox.maxY; j++) {
                for (int k = (int) bbox.minZ; k <= bbox.maxZ; k++) {
                    world.setBlockState(new BlockPos(i, j, k), state);
                }
            }
        }
    }

}
