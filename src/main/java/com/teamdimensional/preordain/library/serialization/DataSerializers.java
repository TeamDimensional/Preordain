package com.teamdimensional.preordain.library.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class DataSerializers {

    public static BlockPos getBlockPos(JsonElement e) throws JsonParseException {
        JsonArray o = e.getAsJsonArray();
        return new BlockPos(o.get(0).getAsInt(), o.get(1).getAsInt(), o.get(2).getAsInt());
    }

    public static AxisAlignedBB getBoundingBox(JsonElement e) throws JsonParseException {
        JsonArray o = e.getAsJsonArray();
        BlockPos p1 = getBlockPos(o.get(0));
        BlockPos p2 = getBlockPos(o.get(1));
        return new AxisAlignedBB(p1, p2);
    }

    public static ItemStack getStack(String e) throws JsonParseException {
        String s = e.replace('@', ':');
        String[] colonSplit = s.split(":");
        if (colonSplit.length != 2 && colonSplit.length != 3) {
            throw new JsonParseException("Invalid itemstack: " + s);
        }
        ResourceLocation rl = new ResourceLocation(colonSplit[0], colonSplit[1]);
        Item it = ForgeRegistries.ITEMS.getValue(rl);
        if (it == null) {
            throw new JsonParseException("Item " + rl + " not found");
        }
        int meta = 0;
        if (colonSplit.length == 3) {
            try {
                meta = Integer.parseInt(colonSplit[2]);
            } catch (NumberFormatException ex) {
                throw new JsonParseException("Invalid item metadata in the itemstack: " + s);
            }
        }
        return new ItemStack(it, 1, meta);
    }

    public static ItemStack getStack(JsonElement e) throws JsonParseException {
        return getStack(e.getAsString());
    }

    @SuppressWarnings("deprecation")
    public static IBlockState getBlockState(String e) throws JsonParseException {
        String s = e.replace('@', ':');
        String[] colonSplit = s.split(":");
        if (colonSplit.length != 2 && colonSplit.length != 3) {
            throw new JsonParseException("Invalid block state: " + s);
        }
        ResourceLocation rl = new ResourceLocation(colonSplit[0], colonSplit[1]);
        Block b = ForgeRegistries.BLOCKS.getValue(rl);
        if (b == null) {
            throw new JsonParseException("Block " + rl + " not found");
        }
        int meta = 0;
        if (colonSplit.length == 3) {
            try {
                meta = Integer.parseInt(colonSplit[2]);
            } catch (NumberFormatException ex) {
                throw new JsonParseException("Invalid block metadata in the blockstate: " + s);
            }
        }
        return b.getStateFromMeta(meta);
    }

    public static IBlockState getBlockState(JsonElement e) throws JsonParseException {
        return getBlockState(e.getAsString());
    }

}
