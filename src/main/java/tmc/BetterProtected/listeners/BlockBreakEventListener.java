// Copyright (C) 2012 Cyrus Innovation
package tmc.BetterProtected.listeners;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import tmc.BetterProtected.domain.BlockEvent;
import tmc.BetterProtected.services.BlockEventRepository;

import java.util.List;

import static tmc.BetterProtected.domain.types.BlockEventType.REMOVED;

public class BlockBreakEventListener extends GenericBlockListener implements Listener {

    public BlockBreakEventListener(List<Integer> unprotectedBlockIds) {
        super(unprotectedBlockIds);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (doesPlayerHavePermissionToBreak(player, getMostRecentBlockEvent(block), block)) {
            BlockEventRepository.save(BlockEvent.newBlockEvent(block, player.getName(), REMOVED));
        } else {
            event.setCancelled(true);
            player.sendMessage(ChatColor.DARK_RED + "You cannot break this block!");
        }
    }
}
