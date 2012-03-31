package tmc.BetterProtected.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.joda.time.DateTime;
import tmc.BetterProtected.domain.BlockCoordinate;
import tmc.BetterProtected.domain.BlockEvent;
import tmc.BetterProtected.domain.ChunkCoordinate;
import tmc.BetterProtected.domain.World;
import tmc.BetterProtected.domain.types.BlockEventType;
import tmc.BetterProtected.svc.BlockEventRepository;

public class BlockListener implements Listener {

    private BlockEventRepository blockEventRepository;

    public BlockListener(BlockEventRepository blockEventRepository) {
        this.blockEventRepository = blockEventRepository;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        BlockCoordinate blockCoordinate = new BlockCoordinate(block.getX(), block.getY(), block.getZ());
        World world = new World(block.getWorld().getName());
        BlockEvent mostRecentBlockEvent = blockEventRepository.findMostRecent(blockCoordinate, world);

        BlockEvent blockRemovedEvent = new BlockEvent(
                new DateTime(),
                new tmc.BetterProtected.domain.Player(event.getPlayer().getName()),
                BlockEventType.REMOVED,
                blockCoordinate,
                new ChunkCoordinate(block.getChunk().getX(), block.getChunk().getZ()),
                world,
                block.getType()
        );

        //Let's do some Logic!
        if (mostRecentBlockEvent == null) {
            blockEventRepository.save(blockRemovedEvent);
        } else if (event.getPlayer().isOp()) {
            blockEventRepository.save(blockRemovedEvent);
        } else if (mostRecentBlockEvent.getBlockEventType() == BlockEventType.REMOVED) {
            blockEventRepository.save(blockRemovedEvent);
        } else if (mostRecentBlockEvent.getOwner().getUsername().equalsIgnoreCase(event.getPlayer().getName())) {
            blockEventRepository.save(blockRemovedEvent);
        } else {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot break this block!");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        BlockCoordinate blockCoordinate = new BlockCoordinate(block.getX(), block.getY(), block.getZ());
        World world = new World(block.getWorld().getName());
        Player player = event.getPlayer();
        tmc.BetterProtected.domain.Player owner = new tmc.BetterProtected.domain.Player(player.getName());
        BlockEvent mostRecentBlockEvent = blockEventRepository.findMostRecent(blockCoordinate, world);

        Material blockType = block.getType();
        BlockEvent blockPlacedEvent = new BlockEvent(
                new DateTime(),
                owner,
                BlockEventType.PLACED,
                blockCoordinate,
                new ChunkCoordinate(block.getChunk().getX(), block.getChunk().getZ()),
                world,
                blockType
        );

        if(mostRecentBlockEvent == null || mostRecentBlockEvent.getBlockEventType() == BlockEventType.REMOVED || player.isOp()) {
            blockEventRepository.save(blockPlacedEvent); // Prevent protection on specific block types here.
        } else {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + String.format("You cannot place a %s block here!", blockType.toString()));
        }
    }
}
