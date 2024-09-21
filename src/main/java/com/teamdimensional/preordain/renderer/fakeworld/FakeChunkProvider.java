package com.teamdimensional.preordain.renderer.fakeworld;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class FakeChunkProvider implements IChunkProvider {
    private final World world;
    private final Map<Long, Chunk> loadedChunks = new Long2ObjectOpenHashMap<>();

    public FakeChunkProvider(World world) {
        this.world = world;
    }

    @Nullable
    @Override
    public Chunk getLoadedChunk(int x, int z) {
        return loadedChunks.get(ChunkPos.asLong(x, z));
    }

    @Override
    @Nonnull
    public Chunk provideChunk(int x, int z) {
        long key = ChunkPos.asLong(x, z);
        if (!loadedChunks.containsKey(key)) {
            Chunk c = new Chunk(world, x, z);
            loadedChunks.put(key, c);
        }
        return loadedChunks.get(key);
    }

    @Override
    public boolean tick() {
        for (Chunk chunk : loadedChunks.values()) {
            chunk.onTick(false);
        }
        return !loadedChunks.isEmpty();
    }

    @Override
    @Nonnull
    public String makeString() {
        return "FakeChunkProvider";
    }

    @Override
    public boolean isChunkGeneratedAt(int x, int z) {
        return false;
    }
}
