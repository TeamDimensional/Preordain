package com.teamdimensional.preordain.renderer.ponder.scene;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PonderScene {
    public final Map<BlockPos, IBlockState> blocks = new Object2ObjectOpenHashMap<>();
    public final List<EntityConfig<Entity>> entities = new ArrayList<>();
    public final Map<BlockPos, EntityConfig<TileEntity>> tileEntities = new Object2ObjectOpenHashMap<>();

    public List<Entity> getRealEntities() {
        return entities.stream().filter(x -> !x.renderOnly).map(x -> x.entity).collect(Collectors.toList());
    }

    public static class EntityConfig<T> {
        public final T entity;
        public final boolean renderOnly;
        public final boolean ticking;

        public EntityConfig(T entity, boolean renderOnly, boolean ticking) {
            this.entity = entity;
            this.renderOnly = renderOnly;
            this.ticking = ticking;
        }
    }
}
