package tmc.BetterProtected.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import tmc.BetterProtected.domain.BlockEvent;
import tmc.BetterProtected.services.BlockEventRepository;

import java.util.List;

import static org.bukkit.Material.*;
import static tmc.BetterProtected.domain.types.BlockEventType.PLACED;

public class PlayerBucketPourEventListener extends GenericBlockListener implements Listener {

    public PlayerBucketPourEventListener(BlockEventRepository blockEventRepository, List<Integer> unprotectedBlockIds) {
        super(blockEventRepository, unprotectedBlockIds);
    }

    @EventHandler
    public void onBucketPour(PlayerBucketEmptyEvent event) {
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
}
