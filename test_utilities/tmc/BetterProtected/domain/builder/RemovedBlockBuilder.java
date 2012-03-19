package tmc.BetterProtected.domain.builder;

import com.sun.istack.internal.Builder;
import org.bukkit.Material;
import org.joda.time.DateTime;
import tmc.BetterProtected.domain.BlockCoordinate;
import tmc.BetterProtected.domain.ChunkCoordinate;
import tmc.BetterProtected.domain.Player;
import tmc.BetterProtected.domain.RemovedBlock;

public class RemovedBlockBuilder implements Builder<RemovedBlock> {
    private ChunkCoordinate chunkCoordinate = new ChunkCoordinate(1,2);
    private BlockCoordinate blockCoordinate = new BlockCoordinate(1,2,3);
    private Player player = new Player("Jason");

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
}
