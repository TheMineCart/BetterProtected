package tmc.BetterProtected.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import tmc.BetterProtected.domain.BlockCoordinate;
import tmc.BetterProtected.domain.BlockEvent;
import tmc.BetterProtected.domain.World;
import tmc.BetterProtected.services.BlockEventRepository;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static org.bukkit.Material.*;
import static tmc.BetterProtected.domain.types.BlockEventType.PLACED;
import static tmc.BetterProtected.domain.types.BlockEventType.REMOVED;

public class BlockListener implements Listener {

    private BlockEventRepository blockEventRepository;
    private Set<Material> ignoredMaterial;

    public BlockListener(BlockEventRepository blockEventRepository, List<Integer> unprotectedBlockIds) {
        this.blockEventRepository = blockEventRepository;
        this.ignoredMaterial = newHashSet();
        for (Integer blockTypeId : unprotectedBlockIds) {
            ignoredMaterial.add(Material.getMaterial(blockTypeId));
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        BlockEvent mostRecentBlockEvent = getMostRecentBlockEvent(block);

        if (doesPlayerHavePermissionToBreak(player, mostRecentBlockEvent, block)) {
            blockEventRepository.save(BlockEvent.newBlockEvent(block, player.getName(), REMOVED));
        } else {
            event.setCancelled(true);
            player.sendMessage(ChatColor.DARK_RED + "You cannot break this block!");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        BlockEvent mostRecentBlockEvent = getMostRecentBlockEvent(block);

        if (doesPlayerHavePermissionToPlace(player, block, mostRecentBlockEvent)) {
            //Allow block placement but add REMOVED event if previous block is liquid
            if (mostRecentBlockEvent != null && isMaterialLiquid(mostRecentBlockEvent.getMaterial())) {
                blockEventRepository.save(BlockEvent.newBlockEvent(block, player.getName(), REMOVED, mostRecentBlockEvent.getMaterial()));
            }
            //Don't save block place event if block type is ignored
            if(!isMaterialIgnored(block.getType())) {
                blockEventRepository.save(BlockEvent.newBlockEvent(block, player.getName(), PLACED));
            }
        } else if (isMaterialIgnored(block.getType()) && event.getBlockReplacedState().getType() == AIR) {
            blockEventRepository.save(BlockEvent.newBlockEvent(block, player.getName(), REMOVED, AIR));
        } else if (event.getBlockReplacedState().getType() == AIR) {
            blockEventRepository.save(BlockEvent.newBlockEvent(block, player.getName(), REMOVED, AIR));
            blockEventRepository.save(BlockEvent.newBlockEvent(block, player.getName(), PLACED));
        } else {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.DARK_RED + "You cannot place " + block.getType() + " block here!");
        }
    }

    @EventHandler
    public void onBucketRemove(PlayerBucketFillEvent event) {
        Block block = event.getBlockClicked().getRelative(event.getBlockFace());
        Player player = event.getPlayer();

        if (doesPlayerHavePermissionToBreak(player, getMostRecentBlockEvent(block), block)) {
            blockEventRepository.save(BlockEvent.newBlockEvent(block, player.getName(), REMOVED));
        } else {
            event.setCancelled(true);
            player.sendMessage(ChatColor.DARK_RED + "You can not fill your bucket from this block!");
        }
    }

    @EventHandler
    public void onBucketPlace(PlayerBucketEmptyEvent event) {
        Block block = event.getBlockClicked().getRelative(event.getBlockFace());
        Player player = event.getPlayer();

        if (isMaterialIgnored(block.getType())) return; //Don't bother if the material doesn't get protection.

        if (doesPlayerHavePermissionToPlace(player, block, getMostRecentBlockEvent(block))) {
            Material blockType = block.getType();
            if (event.getBucket() == WATER_BUCKET) {
                blockType = STATIONARY_WATER;
            } else if (event.getBucket() == LAVA_BUCKET) {
                blockType = STATIONARY_LAVA;
            }

            blockEventRepository.save(BlockEvent.newBlockEvent(block, player.getName(), PLACED, blockType));
        } else {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.DARK_RED + "You cannot pour your bucket here!");
        }
    }

    private BlockEvent getMostRecentBlockEvent(Block block) {
        BlockCoordinate blockCoordinate = BlockCoordinate.newCoordinate(block);
        World world = World.newWorld(block);
        return blockEventRepository.findMostRecent(blockCoordinate, world);
    }

    private boolean doesPlayerHavePermissionToBreak(Player player, BlockEvent mostRecentBlockEvent, Block block) {
        if (mostRecentBlockEvent == null ) return true;
        if (mostRecentBlockEvent.getBlockEventType() == REMOVED) return true;
        if (isBlockEventOwnedByPlayer(player, mostRecentBlockEvent)) return true;
        if (isMaterialIgnored(block.getType()) && isMaterialIgnored(mostRecentBlockEvent.getMaterial())) {
            return true;
        }
        return player.isOp();
    }

    private boolean doesPlayerHavePermissionToPlace(Player player, Block block, BlockEvent mostRecentBlockEvent) {
        if (mostRecentBlockEvent == null) return true;
        if (mostRecentBlockEvent.getBlockEventType() == REMOVED) return true;
        if (isBlockEventOwnedByPlayer(player, mostRecentBlockEvent) && isAllowedToPlaceBlockIntoLiquid(mostRecentBlockEvent, block))
            return true;
        return player.isOp();
    }

    private boolean isBlockEventOwnedByPlayer(Player player, BlockEvent mostRecentBlockEvent) {
        return mostRecentBlockEvent.getOwner().getUsername().equalsIgnoreCase(player.getName());
    }

    private boolean isAllowedToPlaceBlockIntoLiquid(BlockEvent blockEvent, Block blockInHand) {
        return isMaterialLiquid(blockEvent.getMaterial()) && !isMaterialLiquid(blockInHand.getType());
    }

    private boolean isMaterialLiquid(Material material) {
        return material == STATIONARY_LAVA || material == STATIONARY_WATER ||
                material == LAVA || material == WATER;
    }

    private boolean isMaterialIgnored(Material material) {
        return ignoredMaterial.contains(material);
    }
}