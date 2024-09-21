package com.teamdimensional.preordain.renderer;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class PreordainRenderRegion {

    public final World world;
    public AxisAlignedBB boundingBox = null;

    public PreordainRenderRegion(World world) {
        this.world = world;
    }

    public void setSize(Integer[] size) {
        boundingBox = new AxisAlignedBB(0, 0, 0, size[0], size[1], size[2]);
    }

}
