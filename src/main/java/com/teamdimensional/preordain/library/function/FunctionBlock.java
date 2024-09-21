package com.teamdimensional.preordain.library.function;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamdimensional.preordain.core.function.PreordainFunction;
import com.teamdimensional.preordain.library.serialization.DataSerializers;
import com.teamdimensional.preordain.renderer.PreordainRenderRegion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public class FunctionBlock extends PreordainFunction {

    private final BlockPos position;
    private final IBlockState state;

    public FunctionBlock(BlockPos position, IBlockState state) {
        this.position = position;
        this.state = state;
    }

    @Override
    public void apply(PreordainRenderRegion renderRegion) {
        renderRegion.world.setBlockState(position, state);
    }

    public static FunctionBlock create(JsonElement e) {
        JsonObject json = e.getAsJsonObject();
        BlockPos pos = DataSerializers.getBlockPos(json.get("pos"));
        IBlockState state = DataSerializers.getBlockState(json.get("block"));
        return new FunctionBlock(pos, state);
    }

}
