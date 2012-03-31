package tmc.BetterProtected.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import tmc.BetterProtected.domain.BlockCoordinate;
import tmc.BetterProtected.domain.BlockEvent;
import tmc.BetterProtected.domain.World;
import tmc.BetterProtected.svc.BlockEventRepository;

public class PlayerListener implements Listener {

    private BlockEventRepository blockEventRepository;

    public PlayerListener(BlockEventRepository blockEventRepository) {
        this.blockEventRepository = blockEventRepository;
    }

    @EventHandler
    public void playerHitsBlockWithStick(PlayerInteractEvent event) {
        Action playerAction = event.getAction();
        Player player = event.getPlayer();

        if (player.isOp() && playerClickedWithStick(player, playerAction)) {
            Block clickedBlock = event.getClickedBlock();
            BlockEvent protectedBlock = blockEventRepository.findMostRecent(BlockCoordinate.newCoordinate(clickedBlock), World.newWorld(clickedBlock));
            if (protectedBlock != null) {

                player.sendMessage(ChatColor.RED + String.format("%s at %s, %s, %s was placed by %s on %s at %s",
                        ChatColor.GOLD + protectedBlock.getMaterial().name() + ChatColor.RED,
                        ChatColor.DARK_AQUA + protectedBlock.getBlockCoordinate().getX().toString() + ChatColor.RED,
                        ChatColor.DARK_AQUA + protectedBlock.getBlockCoordinate().getY().toString() + ChatColor.RED,
                        ChatColor.DARK_AQUA + protectedBlock.getBlockCoordinate().getZ().toString() + ChatColor.RED,
                        ChatColor.DARK_PURPLE + protectedBlock.getOwner().getUsername() + ChatColor.RED,
                        ChatColor.GREEN + protectedBlock.getInstant().toString("M/d/yy") + ChatColor.RED,
                        ChatColor.GREEN + protectedBlock.getInstant().toString("h:mm:ss aa")
                ));
            }
        }
    }

    private boolean playerClickedWithStick(Player player, Action playerAction) {
         return player.getItemInHand().getType() == Material.STICK &&
                 (playerAction == Action.LEFT_CLICK_BLOCK || playerAction == Action.RIGHT_CLICK_BLOCK);
    }
}
