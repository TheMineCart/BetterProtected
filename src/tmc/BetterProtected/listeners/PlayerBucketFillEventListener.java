package tmc.BetterProtected.listeners;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketFillEvent;
import tmc.BetterProtected.domain.BlockEvent;
import tmc.BetterProtected.services.BlockEventRepository;
import tmc.BetterProtected.services.PlayerRepository;

import java.util.List;

import static tmc.BetterProtected.domain.types.BlockEventType.REMOVED;

public class PlayerBucketFillEventListener extends GenericBlockListener implements Listener {

    public PlayerBucketFillEventListener(BlockEventRepository blockEventRepository, PlayerRepository playerRepository, List<Integer> unprotectedBlockIds) {
        super(blockEventRepository, playerRepository, unprotectedBlockIds);
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {
        Block block = event.getBlockClicked().getRelative(event.getBlockFace());
        Player player = event.getPlayer();

        if (doesPlayerHavePermissionToBreak(player, getMostRecentBlockEvent(block), block)) {
            blockEventRepository.save(BlockEvent.newBlockEvent(block, player.getName(), REMOVED));
        } else {
            event.setCancelled(true);
            player.sendMessage(ChatColor.DARK_RED + "You can not fill your bucket from this block!");
        }
    }
}
