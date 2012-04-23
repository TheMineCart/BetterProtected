package tmc.BetterProtected.domain;


import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class TestChunk implements Chunk {
    private int x;
    private int z;
    private World world;
    private List<Block> blocks = new ArrayList<Block>();

    public TestChunk(int x, int z, World world) {
        this.x = x;
        this.z = z;
        this.world = world;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getZ() {
        return this.z;
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        for (Block block : blocks) {
            if (block.getX() == x && block.getY() == y && block.getZ() == z) {
                return block;
            }
        }
        return new TestBlock(x, y, z, Material.AIR);
    }

    @Override
    public ChunkSnapshot getChunkSnapshot() {
        return null;
    }

    @Override
    public ChunkSnapshot getChunkSnapshot(boolean b, boolean b1, boolean b2) {
        return null;
    }

    @Override
    public Entity[] getEntities() {
        return new Entity[0];
    }

    @Override
    public BlockState[] getTileEntities() {
        return new BlockState[0];
    }

    @Override
    public boolean isLoaded() {
        return false;
    }

    @Override
    public boolean load(boolean b) {
        return false;
    }

    @Override
    public boolean load() {
        return false;
    }

    @Override
    public boolean unload(boolean b, boolean b1) {
        return false;
    }

    @Override
    public boolean unload(boolean b) {
        return false;
    }

    @Override
    public boolean unload() {
        return false;
    }

    public void addBlock(Block testBlock) {
        blocks.add(testBlock);
    }
}
