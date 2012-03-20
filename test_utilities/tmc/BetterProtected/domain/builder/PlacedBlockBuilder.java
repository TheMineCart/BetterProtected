package tmc.BetterProtected.domain.builder;

import com.sun.istack.internal.Builder;
import org.bukkit.Material;
import org.joda.time.DateTime;
import tmc.BetterProtected.domain.*;

public class PlacedBlockBuilder implements Builder<PlacedBlock> {

    private ChunkCoordinate chunkCoordinate = new ChunkCoordinate(1L, 2L);
    private BlockCoordinate blockCoordinate = new BlockCoordinate(1L, 2L, 3L);
    private Player player = new Player("Jason");
    private World worldName = new World("narnia");

    public static PlacedBlockBuilder aPlacedBlock() {
        return new PlacedBlockBuilder();
    }

    @Override
    public PlacedBlock build() {
        PlacedBlock block = new PlacedBlock();
        block.setPlacedOn(new DateTime());
        block.setPlacedBy(player);
        block.setBlockCoordinate(blockCoordinate);
        block.setChunkCoordinate(chunkCoordinate);
        block.setMaterial(Material.DIRT);
        block.setWorld(worldName);
        return block;
    }

    public PlacedBlockBuilder withChunkCoordinate(ChunkCoordinate chunkCoordinate) {
        this.chunkCoordinate = chunkCoordinate;
        return this;
    }

    public PlacedBlockBuilder withBlockCoordinate(BlockCoordinate blockCoordinate) {
        this.blockCoordinate = blockCoordinate;
        return this;
    }

    public PlacedBlockBuilder withPlacedBy(Player player) {
        this.player = player;
        return this;
    }

    public PlacedBlockBuilder withWorld(World worldName) {
        this.worldName = worldName;
        return this;
    }
}
