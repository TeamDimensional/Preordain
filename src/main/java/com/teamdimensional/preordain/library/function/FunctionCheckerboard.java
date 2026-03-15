package com.teamdimensional.preordain.library.function;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamdimensional.preordain.Preordain;
import com.teamdimensional.preordain.core.function.PreordainFunction;
import com.teamdimensional.preordain.library.serialization.DataSerializers;
import com.teamdimensional.preordain.renderer.ponder.world.WorldPonder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class FunctionCheckerboard extends PreordainFunction {

    private final AxisAlignedBB boundingBox;
    private final IBlockState state1;
    private final IBlockState state2;

    public FunctionCheckerboard(AxisAlignedBB boundingBox, IBlockState state1, IBlockState state2) {
        this.boundingBox = boundingBox;
        this.state1 = state1;
        this.state2 = state2;
    }

    @Override
    public void apply(WorldPonder world) {
        int b = 0;
        for (int i = (int) boundingBox.minX; i <= boundingBox.maxX; i++) {
            for (int j = (int) boundingBox.minY; j <= boundingBox.maxY; j++) {
                for (int k = (int) boundingBox.minZ; k <= boundingBox.maxZ; k++) {
                    IBlockState toSet = (i + j + k) % 2 == 0 ? state1 : state2;
                    world.setBlockState(new BlockPos(i, j, k), toSet);
                    b++;
                }
            }
        }
        Preordain.LOGGER.info("Added {} blocks", b);
    }

    public static FunctionCheckerboard create(JsonElement e) {
        JsonObject json = e.getAsJsonObject();
        AxisAlignedBB boundingBox = DataSerializers.getBoundingBox(json.get("bbox"));
        IBlockState state1 = DataSerializers.getBlockState(json.get("block1"));
        IBlockState state2 = DataSerializers.getBlockState(json.get("block2"));
        return new FunctionCheckerboard(boundingBox, state1, state2);
    }
}
