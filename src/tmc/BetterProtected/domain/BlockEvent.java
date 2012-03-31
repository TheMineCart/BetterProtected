package tmc.BetterProtected.domain;

import com.google.gson.annotations.Expose;
import org.bukkit.Material;
import org.joda.time.DateTime;
import tmc.BetterProtected.domain.types.BlockEventType;

public class BlockEvent {
    @Expose private DateTime instant;
    @Expose private Player owner;
    @Expose private BlockEventType blockEventType;
    @Expose private BlockCoordinate blockCoordinate;
    @Expose private ChunkCoordinate chunkCoordinate;
    @Expose private World world;
    @Expose private Material material;

    public BlockEvent(DateTime instant, Player owner, BlockEventType blockEventType, BlockCoordinate blockCoordinate, ChunkCoordinate chunkCoordinate, World world, Material material) {
        this.instant = instant;
        this.owner = owner;
        this.blockEventType = blockEventType;
        this.blockCoordinate = blockCoordinate;
        this.chunkCoordinate = chunkCoordinate;
        this.world = world;
        this.material = material;
    }

    public DateTime getInstant() {
        return instant;
    }

    public Player getOwner() {
        return owner;
    }

    public BlockEventType getBlockEventType() {
        return blockEventType;
    }

    public BlockCoordinate getBlockCoordinate() {
        return blockCoordinate;
    }

    public ChunkCoordinate getChunkCoordinate() {
        return chunkCoordinate;
    }

    public World getWorld() {
        return world;
    }

    public Material getMaterial() {
        return material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlockEvent)) return false;

        BlockEvent that = (BlockEvent) o;

        if (blockCoordinate != null ? !blockCoordinate.equals(that.blockCoordinate) : that.blockCoordinate != null)
            return false;
        if (blockEventType != that.blockEventType) return false;
        if (chunkCoordinate != null ? !chunkCoordinate.equals(that.chunkCoordinate) : that.chunkCoordinate != null)
            return false;
        if (instant != null ? !instant.equals(that.instant) : that.instant != null) return false;
        if (material != that.material) return false;
        if (owner != null ? !owner.equals(that.owner) : that.owner != null) return false;
        if (world != null ? !world.equals(that.world) : that.world != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = instant != null ? instant.hashCode() : 0;
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (blockEventType != null ? blockEventType.hashCode() : 0);
        result = 31 * result + (blockCoordinate != null ? blockCoordinate.hashCode() : 0);
        result = 31 * result + (chunkCoordinate != null ? chunkCoordinate.hashCode() : 0);
        result = 31 * result + (world != null ? world.hashCode() : 0);
        result = 31 * result + (material != null ? material.hashCode() : 0);
        return result;
    }
}