// Copyright (C) 2012 Cyrus Innovation
package tmc.BetterProtected.domain;

import com.google.gson.annotations.Expose;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.joda.time.DateTime;
import tmc.BetterProtected.domain.types.BlockEventType;

public class BlockEvent {
    @Expose private DateTime i; //Instant
    @Expose private String o; //Owner
    @Expose private BlockEventType e; //eventType
    @Expose private BlockCoordinate b; //blockCoordinate
    @Expose private ChunkCoordinate c; //chunkCoordinate
    @Expose private String w; //world
    @Expose private Material m; //material

    public BlockEvent(DateTime instant, String owner, BlockEventType blockEventType, BlockCoordinate blockCoordinate, ChunkCoordinate chunkCoordinate, String world, Material material) {
        this.i = instant;
        this.o = owner;
        this.e = blockEventType;
        this.b = blockCoordinate;
        this.c = chunkCoordinate;
        this.w = world;
        this.m = material;
    }

    //Standard BlockEvent factory
    public static BlockEvent newBlockEvent(Block block, String playerName, BlockEventType type) {
        return newBlockEvent(block, playerName, type, block.getType());
    }

    //For when you have to directly specify the material type
    public static BlockEvent newBlockEvent(Block block, String playerName, BlockEventType type, Material material) {
        BlockCoordinate blockCoordinate = BlockCoordinate.newCoordinate(block);
        String world = block.getWorld().getName();

        return new BlockEvent(new DateTime(), playerName, type, blockCoordinate,
                new ChunkCoordinate(block.getChunk().getX(), block.getChunk().getZ()), world, material);
    }

    public DateTime getInstant() {
        return i;
    }

    public String getOwner() {
        return o;
    }

    public BlockEventType getBlockEventType() {
        return e;
    }

    public BlockCoordinate getBlockCoordinate() {
        return b;
    }

    public ChunkCoordinate getChunkCoordinate() {
        return c;
    }

    public String getWorld() {
        return w;
    }

    public Material getMaterial() {
        return m;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlockEvent)) return false;

        BlockEvent that = (BlockEvent) o;

        if (b != null ? !b.equals(that.b) : that.b != null)
            return false;
        if (e != that.e) return false;
        if (c != null ? !c.equals(that.c) : that.c != null)
            return false;
        if (i != null ? !i.equals(that.i) : that.i != null) return false;
        if (m != that.m) return false;
        if (this.o != null ? !this.o.equals(that.o) : that.o != null) return false;
        if (w != null ? !w.equals(that.w) : that.w != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = i != null ? i.hashCode() : 0;
        result = 31 * result + (o != null ? o.hashCode() : 0);
        result = 31 * result + (e != null ? e.hashCode() : 0);
        result = 31 * result + (b != null ? b.hashCode() : 0);
        result = 31 * result + (c != null ? c.hashCode() : 0);
        result = 31 * result + (w != null ? w.hashCode() : 0);
        result = 31 * result + (m != null ? m.hashCode() : 0);
        return result;
    }
}