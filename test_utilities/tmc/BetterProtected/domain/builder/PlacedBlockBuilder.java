package tmc.BetterProtected.domain.builder;

import com.sun.istack.internal.Builder;
import org.bukkit.Material;
import org.joda.time.DateTime;
import tmc.BetterProtected.domain.BlockCoordinate;
import tmc.BetterProtected.domain.ChunkCoordinate;
import tmc.BetterProtected.domain.PlacedBlock;
import tmc.BetterProtected.domain.Player;

public class PlacedBlockBuilder implements Builder<PlacedBlock> {

    public static PlacedBlockBuilder aPlacedBlock() {
        return new PlacedBlockBuilder();
    }

    @Override
    public PlacedBlock build() {
        PlacedBlock block = new PlacedBlock();
        block.setPlacedOn(new DateTime());
        block.setPlacedBy(new Player("Jason"));
        block.setBlockCoordinate(new BlockCoordinate(1,2,3));
        block.setChunkCoordinate(new ChunkCoordinate(1,2));
        block.setMaterial(Material.DIRT);
        return block;
    }
}
