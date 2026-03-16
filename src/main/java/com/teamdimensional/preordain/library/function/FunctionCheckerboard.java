package com.teamdimensional.preordain.library.function;

import com.teamdimensional.preordain.Preordain;
import com.teamdimensional.preordain.core.function.PreordainFunction;
import com.teamdimensional.preordain.renderer.ponder.world.WorldPonder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class FunctionCheckerboard extends PreordainFunction {

    private AxisAlignedBB bbox;
    private IBlockState state1;
    private IBlockState state2;

    @Override
    public void apply(WorldPonder world) {
        int b = 0;
        for (int i = (int) bbox.minX; i <= bbox.maxX; i++) {
            for (int j = (int) bbox.minY; j <= bbox.maxY; j++) {
                for (int k = (int) bbox.minZ; k <= bbox.maxZ; k++) {
                    IBlockState toSet = (i + j + k) % 2 == 0 ? state1 : state2;
                    world.setBlockState(new BlockPos(i, j, k), toSet);
                    b++;
                }
            }
        }
        Preordain.LOGGER.debug("Added {} blocks", b);
    }

}
