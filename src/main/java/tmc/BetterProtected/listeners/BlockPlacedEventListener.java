package tmc.BetterProtected.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import tmc.BetterProtected.domain.BlockEvent;
import tmc.BetterProtected.domain.types.BlockEventType;
import tmc.BetterProtected.services.BlockEventRepository;
import tmc.BetterProtected.services.PlayerRepository;

import java.util.List;

import static org.bukkit.Material.*;
import static tmc.BetterProtected.domain.types.BlockEventType.PLACED;
import static tmc.BetterProtected.domain.types.BlockEventType.UNPROTECTED;

public class BlockPlacedEventListener extends GenericBlockListener implements Listener {

    public BlockPlacedEventListener(List<Integer> unprotectedBlockIds) {
        super(unprotectedBlockIds);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Material previousBlockType = event.getBlockReplacedState().getType();
        Player player = event.getPlayer();
        BlockEvent mostRecentBlock = getMostRecentBlockEvent(block);

        if (doesPlayerHavePermissionToPlace(block, previousBlockType, player, mostRecentBlock) &&
                !isMaterialIgnored(block.getType()) && !isPlayerHoeingDirtOrGrass(player, block, previousBlockType)) {
            saveBlockEvent(block, player);
        } else if (isMaterialIgnored(block.getType()) &&
                doesPlayerHavePermissionToPlace(block, previousBlockType, player, mostRecentBlock)) {

            //Do nothing - Don't cancel event and don't register event.

        } else if (isPlayerHoeingDirtOrGrass(player, block, previousBlockType) &&
                doesPlayerHavePermissionToPlace(block, previousBlockType, player, mostRecentBlock)) {

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
        if(PlayerRepository.findPlayerProtectionByName(player.getName())) {
            blockEventType = PLACED;
        } else {
            blockEventType = UNPROTECTED;
        }

        BlockEventRepository.save(BlockEvent.newBlockEvent(block, player.getName(), blockEventType));
    }

    private void cancelBlockPlaceEvent(BlockPlaceEvent event, Block block, Player player) {
        event.setCancelled(true);
        player.sendMessage(ChatColor.DARK_RED + "You cannot place a " + block.getType() + " here");
    }

    private boolean doesPlayerHavePermissionToPlace(Block block, Material previousBlockType, Player player, BlockEvent mostRecentBlock) {
        return doesPlayerHavePermissionToPlace(player, block, mostRecentBlock) || isMaterialAir(previousBlockType);
    }

    private boolean isMaterialAir(Material blockType) {
        return blockType == AIR;
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
