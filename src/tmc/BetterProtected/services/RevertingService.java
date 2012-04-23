package tmc.BetterProtected.services;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.joda.time.DateTime;
import tmc.BetterProtected.domain.BlockEvent;
import tmc.BetterProtected.domain.ChunkCoordinate;
import tmc.BetterProtected.domain.Owner;
import tmc.BetterProtected.domain.World;
import tmc.BetterProtected.domain.types.BlockEventType;

import java.util.List;

public class RevertingService {

    private BlockEventRepository blockEventRepository;

    public RevertingService(BlockEventRepository blockEventRepository) {
        this.blockEventRepository = blockEventRepository;
    }

    public void revertChunks(int radius, Owner owner, Player player, Chunk chunk, World world) {
        List<ChunkCoordinate> chunkCoordinates = ChunkCoordinate.findChunkCoordinatesInRadius(radius, chunk.getX(), chunk.getZ());

        for (ChunkCoordinate chunkCoordinate : chunkCoordinates) {
            List<BlockEvent> blockEvents = blockEventRepository.findByChunkCoordinateAndOwner(chunkCoordinate, owner, world) ;

            for (BlockEvent blockEvent : blockEvents) {
                revertBlock(blockEvent, player, world);
            }
        }
    }

    public void revertBlock(BlockEvent blockEvent, Player player, World world) {
        //now get the history of those blocks;
        List<BlockEvent> history = blockEventRepository.findByBlockCoordinate(blockEvent.getBlockCoordinate(), world);
        if (history.size() > 1) {
            BlockEvent previousBlock = history.get(1);
            new BlockEvent(new DateTime(), new Owner(player.getName()), BlockEventType.REMOVED, blockEvent.getBlockCoordinate(), blockEvent.getChunkCoordinate(), world, Material.AIR);
        } else {

        }
    }
}
