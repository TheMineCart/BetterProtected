package tmc.BetterProtected.domain.builder;

import com.sun.istack.internal.Builder;
import org.bukkit.Material;
import org.joda.time.DateTime;
import tmc.BetterProtected.domain.*;
import tmc.BetterProtected.domain.types.BlockEventType;

public class BlockEventBuilder implements Builder<BlockEvent> {

    private ChunkCoordinate chunkCoordinate = new ChunkCoordinate(1L, 2L);
    private BlockCoordinate blockCoordinate = new BlockCoordinate(1L, 2L, 3L);
    private Player owner = new Player("Jason");
    private World world = new World("narnia");
    private DateTime instant = new DateTime();
    private BlockEventType blockEventType = BlockEventType.PLACED;

    public static BlockEventBuilder aPlacedBlock() {
        return new BlockEventBuilder();
    }

    @Override
    public BlockEvent build() {
        BlockEvent blockEvent = new BlockEvent(instant, owner, blockEventType, blockCoordinate, chunkCoordinate, world, Material.DIRT);
        return blockEvent;
    }

    public BlockEventBuilder withChunkCoordinate(ChunkCoordinate chunkCoordinate) {
        this.chunkCoordinate = chunkCoordinate;
        return this;
    }

    public BlockEventBuilder withBlockCoordinate(BlockCoordinate blockCoordinate) {
        this.blockCoordinate = blockCoordinate;
        return this;
    }

    public BlockEventBuilder withOwner(Player owner) {
        this.owner = owner;
        return this;
    }

    public BlockEventBuilder withWorld(World worldName) {
        this.world = worldName;
        return this;
    }

    public BlockEventBuilder withInstant(DateTime instant) {
        this.instant = instant;
        return this;
    }

    public BlockEventBuilder withBlockEventType(BlockEventType blockEventType) {
        this.blockEventType = blockEventType;
        return this;
    }
}
