package tmc.BetterProtected.domain;

import com.google.gson.annotations.Expose;
import org.bukkit.Material;

public class Block {
    @Expose private BlockCoordinate blockCoordinate;
    @Expose private ChunkCoordinate chunkCoordinate;
    @Expose private World world;
    @Expose private Material material;

    public BlockCoordinate getBlockCoordinate() {
        return blockCoordinate;
    }

    public void setBlockCoordinate(BlockCoordinate blockCoordinate) {
        this.blockCoordinate = blockCoordinate;
    }

    public ChunkCoordinate getChunkCoordinate() {
        return chunkCoordinate;
    }

    public void setChunkCoordinate(ChunkCoordinate chunkCoordinate) {
        this.chunkCoordinate = chunkCoordinate;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Block)) return false;

        Block block = (Block) o;

        if (blockCoordinate != null ? !blockCoordinate.equals(block.blockCoordinate) : block.blockCoordinate != null)
            return false;
        if (chunkCoordinate != null ? !chunkCoordinate.equals(block.chunkCoordinate) : block.chunkCoordinate != null)
            return false;
        if (material != block.material) return false;
        if (world != null ? !world.equals(block.world) : block.world != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = blockCoordinate != null ? blockCoordinate.hashCode() : 0;
        result = 31 * result + (chunkCoordinate != null ? chunkCoordinate.hashCode() : 0);
        result = 31 * result + (world != null ? world.hashCode() : 0);
        result = 31 * result + (material != null ? material.hashCode() : 0);
        return result;
    }
}
