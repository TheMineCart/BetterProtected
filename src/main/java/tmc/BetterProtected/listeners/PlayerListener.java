package tmc.BetterProtected.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import tmc.BetterProtected.domain.BlockCoordinate;
import tmc.BetterProtected.domain.BlockEvent;
import tmc.BetterProtected.services.BlockEventRepository;

import static org.bukkit.Material.*;
import static tmc.BetterProtected.domain.types.BlockEventType.REMOVED;

public class PlayerListener implements Listener {

    @EventHandler
    public void playerHitsBlockWithStick(PlayerInteractEvent event) {
        Action playerAction = event.getAction();
        Player player = event.getPlayer();

        if (player.isOp() && playerClickedWithStick(player, playerAction)) {
            Block clickedBlock = event.getClickedBlock();
            printBlockEventInformation(player, clickedBlock);
        }
    }

    @EventHandler
    public void playerThrowsEggAtBlock(PlayerEggThrowEvent event) {
        Block block = event.getEgg().getLocation().getBlock();
        Material blockType = block.getType();
        Player player = event.getPlayer();

        if (player.isOp() && isMaterialLiquid(blockType)) {
            printBlockEventInformation(player, block);
        }
    }

    private void printBlockEventInformation(Player player, Block block) {
        BlockEvent protectedBlock = BlockEventRepository.findMostRecent(BlockCoordinate.newCoordinate(block), block.getWorld().getName());
        if (protectedBlock != null && protectedBlock.getBlockEventType() != REMOVED) {

            player.sendMessage(ChatColor.RED + String.format("%s at %s, %s, %s was placed by %s on %s at %s",
                    ChatColor.GOLD + protectedBlock.getMaterial().name() + ChatColor.RED,
                    ChatColor.DARK_AQUA + protectedBlock.getBlockCoordinate().getX().toString() + ChatColor.RED,
                    ChatColor.DARK_AQUA + protectedBlock.getBlockCoordinate().getY().toString() + ChatColor.RED,
                    ChatColor.DARK_AQUA + protectedBlock.getBlockCoordinate().getZ().toString() + ChatColor.RED,
                    ChatColor.DARK_PURPLE + protectedBlock.getOwner() + ChatColor.RED,
                    ChatColor.GREEN + protectedBlock.getInstant().toString("M/d/yy") + ChatColor.RED,
                    ChatColor.GREEN + protectedBlock.getInstant().toString("h:mm:ss aa")
            ));
        }
    }

    private boolean playerClickedWithStick(Player player, Action playerAction) {
         return player.getItemInHand().getType() == STICK &&
                 (playerAction == Action.LEFT_CLICK_BLOCK || playerAction == Action.RIGHT_CLICK_BLOCK);
    }

    private boolean isMaterialLiquid(Material blockType) {
        return blockType == STATIONARY_WATER || blockType == WATER || blockType == STATIONARY_LAVA || blockType == LAVA;
    }
}
