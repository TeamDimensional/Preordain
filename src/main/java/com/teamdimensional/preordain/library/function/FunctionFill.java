package com.teamdimensional.preordain.library.function;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamdimensional.preordain.core.function.PreordainFunction;
import com.teamdimensional.preordain.library.serialization.DataSerializers;
import com.teamdimensional.preordain.renderer.PreordainRenderRegion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class FunctionFill extends PreordainFunction {

    private final AxisAlignedBB boundingBox;
    private final IBlockState state;

    public FunctionFill(AxisAlignedBB boundingBox, IBlockState state) {
        this.boundingBox = boundingBox;
        this.state = state;
    }

    @Override
    public void apply(PreordainRenderRegion renderRegion) {
        for (int i = (int) boundingBox.minX; i <= boundingBox.maxX; i++) {
            for (int j = (int) boundingBox.minY; j <= boundingBox.maxY; j++) {
                for (int k = (int) boundingBox.minZ; k <= boundingBox.maxZ; k++) {
                    renderRegion.world.setBlockState(new BlockPos(i, j, k), state);
                }
            }
        }
    }

    public static FunctionFill create(JsonElement e) {
        JsonObject json = e.getAsJsonObject();
        AxisAlignedBB boundingBox = DataSerializers.getBoundingBox(json.get("pos"));
        IBlockState state = DataSerializers.getBlockState(json.get("block"));
        return new FunctionFill(boundingBox, state);
    }
}
