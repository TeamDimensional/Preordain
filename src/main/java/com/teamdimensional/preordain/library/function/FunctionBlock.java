package com.teamdimensional.preordain.library.function;

import com.teamdimensional.preordain.core.function.PreordainFunction;
import com.teamdimensional.preordain.renderer.ponder.world.WorldPonder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public class FunctionBlock extends PreordainFunction {

    private BlockPos pos;
    private IBlockState state;

    @Override
    public void apply(WorldPonder world) {
        world.setBlockState(pos, state);
    }

}
