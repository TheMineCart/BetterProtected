package tmc.BetterProtected.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import tmc.BetterProtected.domain.BlockEvent;
import tmc.BetterProtected.domain.types.BlockEventType;
import tmc.BetterProtected.services.BlockEventRepository;
import tmc.BetterProtected.services.PlayerRepository;

import java.util.List;

import static org.bukkit.Material.*;
import static tmc.BetterProtected.domain.types.BlockEventType.*;

public class BlockPlacedEventListener extends GenericBlockListener implements Listener {

    public BlockPlacedEventListener(BlockEventRepository blockEventRepository, PlayerRepository playerRepository, List<Integer> unprotectedBlockIds) {
        super(blockEventRepository, playerRepository, unprotectedBlockIds);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Material previousBlockType = event.getBlockReplacedState().getType();
        Player player = event.getPlayer();
        BlockEvent mostRecentBlock = getMostRecentBlockEvent(block);

        if (isPlayerAllowedToPlaceBlock(player, mostRecentBlock, previousBlockType) &&
                !isMaterialIgnored(block.getType()) && !isPlayerHoeingDirtOrGrass(player, block, previousBlockType)) {
            saveBlockEvent(block, player);
        } else if (isMaterialIgnored(block.getType()) &&
                   isPlayerAllowedToPlaceBlock(player, mostRecentBlock, previousBlockType)) {

            //Do nothing - Don't cancel event and don't register event.

        } else if (isPlayerHoeingDirtOrGrass(player, block, previousBlockType) &&
                   isPlayerAllowedToPlaceBlock(player, mostRecentBlock, previousBlockType)) {

            if (isBlockEventOwnedByPlayer(player, mostRecentBlock)) {
                saveBlockEvent(block, player);
            } else {
                //Do nothing - Don't cancel event and don't register event.
            }

        } else {

            cancelBlockPlaceEvent(event, block, player);

        }
    }

    private void saveBlockEvent(Block block, Player player) {
        BlockEventType blockEventType;
        if(playerRepository.findPlayerProtectionByName(player.getName())) {
            blockEventType = PLACED;
        } else {
            blockEventType = UNPROTECTED;
        }

        BlockEvent blockEvent = BlockEvent.newBlockEvent(block, player.getName(), blockEventType);
        blockEventRepository.save(blockEvent);
    }

    private void cancelBlockPlaceEvent(BlockPlaceEvent event, Block block, Player player) {
        event.setCancelled(true);
        player.sendMessage(ChatColor.DARK_RED + "You cannot place a " + block.getType() + " here");
    }

    private boolean isPlayerAllowedToPlaceBlock(Player player, BlockEvent mostRecentBlock, Material previousBlockType) {
        return mostRecentBlock == null || mostRecentBlock.getBlockEventType() == REMOVED || previousBlockType == AIR ||
                isBlockEventOwnedByPlayer(player, mostRecentBlock) || player.isOp();
    }

    boolean isPlayerHoeingDirtOrGrass(Player player, Block block, Material previousBlockMaterial) {
        return (previousBlockMaterial == DIRT || previousBlockMaterial == GRASS) &&
                block.getType() == SOIL && isPlayerToolHoe(player);
    }

    boolean isPlayerToolHoe(Player player) {
        Material thingInHand = player.getItemInHand().getType();
        return thingInHand == DIAMOND_HOE || thingInHand == GOLD_HOE ||
                thingInHand == IRON_HOE || thingInHand == STONE_HOE || thingInHand == WOOD_HOE;
    }
}
