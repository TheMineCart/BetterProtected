package BananaProtect;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockListener;
//import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.config.Configuration;

public class AntiGrief extends BlockListener {

    private final Protection plugin;
    private final String pluginpath = "plugins/BananaProtect";
    /*
     * Initiate the listener
     */

    public AntiGrief(Protection callbackPlugin) {
        plugin = callbackPlugin;
    }

    @Override
    public void onBlockIgnite(BlockIgniteEvent event) {
        event.setCancelled(true);
    }

//    @Override
//    public void onBlockPhysics(BlockPhysicsEvent event) {
//        Material block = event.getBlock().getType();
//        if (block == Material.GRAVEL || block == Material.SAND) {
//            event.setCancelled(true);
//        }
//    }
    
    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!player.isOp()) {
            Block block = event.getBlock();
            World world = block.getWorld();
            File worldpath = new File(pluginpath + "/" + world.getName());
            Chunk chunk = world.getChunkAt(block.getLocation());
            String cname = chunk.getX() + "." + chunk.getZ() + ".yml";
            String cpath = block.getLocation().getBlockX() + "," + block.getLocation().getBlockY() + "," + block.getLocation().getBlockZ();
            String cpath2 = block.getLocation().getBlockX() + "," + (block.getLocation().getBlockY() + 1) + "," + block.getLocation().getBlockZ();
            Configuration pC = plugin.getConfig(new File(worldpath + "/" + cname));
            //pC.setHeader(player.getName());
            if (!plugin.getConfiguration().getBoolean("allow." + pC.getString(cpath + ".player", "") + "." + player.getName().toLowerCase(), false) && pC.getString(cpath + ".player", "").length() > 0) {
                if (!pC.getString(cpath + ".player", "").equals(player.getName()) && pC.getString(cpath + ".player", "").length() != 0) {
                    event.setCancelled(true);
                } else if (block.getRelative(0, 1, 0).getTypeId() == 71 || block.getRelative(0, 1, 0).getTypeId() == 64) {
                    if (!pC.getString(cpath2 + ".player", "").equals(player.getName()) && pC.getString(cpath2 + ".player", "").length() != 0) {
                        event.setCancelled(true);
                    } else {
                        pC.removeProperty(cpath);
                        pC.save();
                    }
                }
            }
        }

    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        //LETS ADD SOME KIND OF WATER/LAVA CHECK
        if (player.isOp() && (event.getBlock().getTypeId() == 95)) {
            event.setCancelled(true);
            String[] whostuff = plugin.whoPlaced(event.getBlock().getLocation().getBlock(), new File(pluginpath + "/" + player.getWorld().getName()));
            String whoplaced = whostuff[0];
            if (whoplaced.length() == 0) {
                player.sendMessage(ChatColor.GREEN + "[Notice] " + ChatColor.LIGHT_PURPLE + "The block at " + event.getBlock().getLocation().getBlockX() + "," + event.getBlock().getLocation().getBlockY() + "," + event.getBlock().getLocation().getBlockZ() + " has no history.");
            } else {
                player.sendMessage(ChatColor.GREEN + "[Notice] " + ChatColor.LIGHT_PURPLE + "The " + Material.getMaterial(Integer.parseInt(whostuff[1])).name().toLowerCase().replace("_", " ") + " at " + event.getBlock().getLocation().getBlockX() + "," + event.getBlock().getLocation().getBlockY() + "," + event.getBlock().getLocation().getBlockZ() + " was placed by " + whoplaced + ".");
            }
        } else if (event.getBlock().getTypeId() == 52) {
            if (!player.isOp()) {
                event.setCancelled(true);
            }
        } else {
            saveBlockProtectionInformation(event, player);
        }
    }

    private void saveBlockProtectionInformation(BlockPlaceEvent event, Player player) {
        Block block = event.getBlock();
        Material blockType = block.getType();
        if (!isDisabledBlockType(blockType)) {
            World world = block.getWorld();
            File worldpath = new File(pluginpath + "/" + world.getName());
            Chunk chunk = world.getChunkAt(block.getLocation());
            String cname = chunk.getX() + "." + chunk.getZ() + ".yml";
            String cpath = block.getLocation().getBlockX() + "," + block.getLocation().getBlockY() + "," + block.getLocation().getBlockZ();
            if (!worldpath.exists()) {
                worldpath.mkdir();
            }
            Configuration pC = plugin.getConfig(new File(worldpath + "/" + cname));
            pC.setProperty(cpath + ".player", player.getName());
            pC.setProperty(cpath + ".type", event.getBlock().getTypeId());
            if (block.getTypeId() == 71 || block.getTypeId() == 64) {
                String cpath2 = block.getLocation().getBlockX() + "," + (block.getLocation().getBlockY() + 1) + "," + block.getLocation().getBlockZ();
                pC.setProperty(cpath2 + ".player", player.getName());
                pC.setProperty(cpath2 + ".type", event.getBlock().getTypeId());
            }
            pC.save();
        }
    }
    
    private boolean isDisabledBlockType(Material blockType) {
        if(    blockType == Material.BROWN_MUSHROOM 
            || blockType == Material.CACTUS 
            || blockType == Material.CROPS 
            || blockType == Material.GRASS 
            || blockType == Material.GRAVEL 
            || blockType == Material.LONG_GRASS 
            || blockType == Material.RED_MUSHROOM 
            || blockType == Material.SAND 
            || blockType == Material.SAPLING
            || blockType == Material.SEEDS  
            || blockType == Material.SUGAR_CANE
            || blockType == Material.WHEAT   ) {
            
            return true;
            
        }
        
        return false;
    }
}