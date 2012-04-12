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
import tmc.BetterProtected.services.PlayerRepository;

import java.util.List;

import static org.bukkit.Material.*;
import static tmc.BetterProtected.domain.types.BlockEventType.PLACED;

public class PlayerBucketEmptyEventListener extends GenericBlockListener implements Listener {

    public PlayerBucketEmptyEventListener(BlockEventRepository blockEventRepository, PlayerRepository playerRepository, List<Integer> unprotectedBlockIds) {
        super(blockEventRepository, playerRepository, unprotectedBlockIds);
    }

    @EventHandler
    public void onBucketPour(PlayerBucketEmptyEvent event) {
        Block block = event.getBlockClicked().getRelative(event.getBlockFace());
        Player player = event.getPlayer();

        if (doesPlayerHavePermissionToPlace(player, block, getMostRecentBlockEvent(block))) {
            Material blockType = block.getType();
            if (event.getBucket() == WATER_BUCKET) {
                blockType = STATIONARY_WATER;
            } else if (event.getBucket() == LAVA_BUCKET) {
                blockType = STATIONARY_LAVA;
            }

            if(!isMaterialIgnored(blockType)) {
                blockEventRepository.save(BlockEvent.newBlockEvent(block, player.getName(), PLACED, blockType));
            }
        } else if (isMaterialIgnored(block.getType())){
            //Do nothing because the material is ignored.
        } else {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.DARK_RED + "You cannot pour your bucket here!");
        }
    }
}
