package tmc.BetterProtected.Listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import tmc.BetterProtected.domain.BlockCoordinate;
import tmc.BetterProtected.domain.BlockEvent;
import tmc.BetterProtected.domain.World;
import tmc.BetterProtected.svc.BlockEventRepository;

import java.util.List;

public class BlockListener implements Listener {

    private BlockEventRepository blockEventRepository;

    public BlockListener(BlockEventRepository blockEventRepository) {
        this.blockEventRepository = blockEventRepository;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        BlockCoordinate coordinate = new BlockCoordinate(block.getX(), block.getY(), block.getZ());
        World world = new World(block.getWorld().getName());
        List<BlockEvent> blockEventList = blockEventRepository.findByBlockCoordinate(coordinate, world);

        Player player = event.getPlayer();
        player.chat(String.format("You just broke a block at: %s, %s, %s", block.getX(), block.getY(), block.getZ()));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        player.chat(String.format("You just placed a block at: %s, %s, %s", block.getX(), block.getY(), block.getZ()));
    }
}
