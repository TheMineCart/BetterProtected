package tmc.BetterProtected.domain.builder;

import com.sun.istack.internal.Builder;
import org.bukkit.Material;
import org.joda.time.DateTime;
import tmc.BetterProtected.domain.*;

public class RemovedBlockBuilder implements Builder<RemovedBlock> {
    private ChunkCoordinate chunkCoordinate = new ChunkCoordinate(1L, 2L);
    private BlockCoordinate blockCoordinate = new BlockCoordinate(1L, 2L, 3L);
    private Player player = new Player("Jason");
    private World world = new World("narnia");

    public static RemovedBlockBuilder aRemovedBlock() {
        return new RemovedBlockBuilder();
    }

    @Override
    public RemovedBlock build() {
        RemovedBlock block = new RemovedBlock();
        block.setRemovedOn(new DateTime());
        block.setRemovedBy(player);
        block.setBlockCoordinate(blockCoordinate);
        block.setChunkCoordinate(chunkCoordinate);
        block.setWorld(world);
        block.setMaterial(Material.DIRT);
        return block;
    }

    public RemovedBlockBuilder withChunkCoordinate(ChunkCoordinate chunkCoordinate) {
        this.chunkCoordinate = chunkCoordinate;
        return this;
    }

    public RemovedBlockBuilder withBlockCoordinate(BlockCoordinate blockCoordinate) {
        this.blockCoordinate = blockCoordinate;
        return this;
    }

    public RemovedBlockBuilder withRemovedBy(Player player) {
        this.player = player;
        return this;
    }

    public RemovedBlockBuilder withWorld(World world) {
        this.world = world;
        return this;
    }
}
