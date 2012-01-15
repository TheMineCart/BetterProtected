package BananaProtect;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

public class EntityHandler extends PlayerListener {

    private final Protection plugin;
    /*
     * Initiate the listener
     */

    public EntityHandler(Protection callbackPlugin) {
        plugin = callbackPlugin;
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        File pluginpath = new File("plugins/BananaProtect");
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.DISPENSER) {
            
                disableEventIfPlayerIsNotOpAndOutputMessage(event, player, pluginpath);
            
            }
        }
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.LEVER
                || event.getClickedBlock().getType() == Material.STONE_BUTTON
                || event.getClickedBlock().getTypeId() == 71
                || event.getClickedBlock().getTypeId() == 64
                || event.getClickedBlock().getType() == Material.CHEST
                || event.getClickedBlock().getType() == Material.FURNACE
                || event.getClickedBlock().getType() == Material.DISPENSER) {
                
                disableEventIfPlayerIsNotOpAndOutputMessage(event, player, pluginpath);
            
            }
        }
        if (player.isOp() && (event.getAction() == Action.LEFT_CLICK_BLOCK) && player.getItemInHand().getType() == Material.STICK) {
            String[] whostuff = plugin.whoPlaced(event.getClickedBlock(), new File(pluginpath + "/" + player.getWorld().getName()));
            String whoplaced = whostuff[0];
            if (whoplaced.length() == 0) {
                player.sendMessage(ChatColor.GREEN + "[Notice] " + ChatColor.LIGHT_PURPLE + "The block at " + event.getClickedBlock().getLocation().getBlockX() + "," + event.getClickedBlock().getLocation().getBlockY() + "," + event.getClickedBlock().getLocation().getBlockZ() + " has no history.");
            } else {
                player.sendMessage(ChatColor.GREEN + "[Notice] " + ChatColor.LIGHT_PURPLE + "The " + event.getClickedBlock().getType().name().toLowerCase().replace("_", " ") + " at " + event.getClickedBlock().getLocation().getBlockX() + "," + event.getClickedBlock().getLocation().getBlockY() + "," + event.getClickedBlock().getLocation().getBlockZ() + " was placed by " + whoplaced + ".");
            }
        }
    }

    private void disableEventIfPlayerIsNotOpAndOutputMessage(PlayerInteractEvent event, Player player, File pluginpath) {
        if (!player.isOp()) {
            String[] whostuff = plugin.whoPlaced(event.getClickedBlock(), new File(pluginpath + "/" + event.getClickedBlock().getWorld().getName() + "/"));
            String whoplaced = whostuff[0];
            if (whoplaced.length() > 0) {
                if (!whoplaced.equals(player.getName())) {
                    player.sendMessage(ChatColor.RED + "[Warning] " + ChatColor.LIGHT_PURPLE + "You did not place that block!");
                    event.setCancelled(true);
                }
            }
        }
    }
}
