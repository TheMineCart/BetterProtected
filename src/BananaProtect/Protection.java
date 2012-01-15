package BananaProtect;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

public class Protection extends JavaPlugin {

    AntiGrief playerListener = new AntiGrief(this);
    EntityHandler playerInteract = new EntityHandler(this);

   // @Override
    public void onDisable() {
        PluginDescriptionFile pdfFile = getDescription();
        System.out.println("[" + (pdfFile.getName()) + "]" + " version "
                + pdfFile.getVersion() + " is disabled!");
    }

    public Location getNextAirSpace(Location blockloc) {
        Location blockl = blockloc;
        Location blockl2 = blockl;
        for (int i = blockloc.getBlockY(); i < 127; i++) {
            blockl.setY(i);
            blockl2.setY(i + 1);
            if (blockl.getBlock().getTypeId() == 0
                    && blockl2.getBlock().getTypeId() == 0) {
                return blockl;
            }

        }
        return null;
    }

    public String[] whoPlaced(Block block, File pluginpath) {
        World world = block.getWorld();
        Chunk chunk = world.getChunkAt(block.getLocation());
        String cname = chunk.getX() + "." + chunk.getZ() + ".yml";
        String cpath = block.getLocation().getBlockX() + ","
                + block.getLocation().getBlockY() + ","
                + block.getLocation().getBlockZ();
        //System.out.print(pluginpath);
        Configuration pC = getConfig(new File(pluginpath + "/" + cname));
        String[] strings = {pC.getString(cpath + ".player", ""), pC.getString(cpath + ".type", "")};
        return strings;
    }

    protected Configuration getConfig(File filepath) {
        if (!filepath.exists()) {

            try {
                filepath.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        Configuration c = new Configuration(filepath);
        c.load();

        return c;
    }

    //@Override
    public void onEnable() {
        //getConfiguration().setHeader("#");
        getConfiguration().save();
        PluginDescriptionFile pdfFile = getDescription();
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.BLOCK_PLACE, playerListener,
                Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_PHYSICS, playerListener,
                Event.Priority.Low, this);
        pm.registerEvent(Event.Type.BLOCK_BREAK, playerListener,
                Event.Priority.High, this);
        pm.registerEvent(Event.Type.BLOCK_DAMAGE, playerListener,
                Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_IGNITE, playerListener,
                Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerInteract,
                Event.Priority.Normal, this);

        System.out.println("[" + (pdfFile.getName()) + "]" + " version "
                + pdfFile.getVersion() + " is enabled!");

        if (!pdfFile.getName().equalsIgnoreCase("BananaProtect")) {
            System.err.print("This plugin was stolen. Report it to codename_B");
            System.exit(-1);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        boolean result = false;

        if (cmd.getName().equalsIgnoreCase("ballow")) {
            result = bAllow(sender, args);
        } else if (cmd.getName().equalsIgnoreCase("bremove")) {
            result = bRemove(sender, args);
        } else if (cmd.getName().equalsIgnoreCase("rollback")) {
            result = rollback(sender, args);
        } else if (cmd.getName().equalsIgnoreCase("regen")) {
            result = regenerate(sender, args);
        }
        return result;
    }

    private boolean bAllow(CommandSender sender, String[] args) {
        if ((sender instanceof Player)) {
            if (args.length == 1) {
                Player player = (Player) sender;
                Configuration pC = getConfiguration();
                pC.setProperty("allow." + player.getName() + "." + args[0].toLowerCase(), true);
                pC.save();
                sender.sendMessage(ChatColor.GREEN + "[Notice] "
                        + ChatColor.LIGHT_PURPLE + args[0] + " added to allowed players.");
                return true;
            }
        }
        return false;
    }

    private boolean bRemove(CommandSender sender, String[] args) {
        if ((sender instanceof Player)) {
            if (args.length == 1) {
                Player player = (Player) sender;
                Configuration pC = getConfiguration();
                //pC.setProperty("allow." + player.getName()+"."+args[0], false);
                pC.removeProperty("allow." + player.getName() + "." + args[0].toLowerCase());
                try {
                    pC.save();
                } catch (Exception e) {
                    System.err.print(player.getName() + " cannot type properly!");
                    System.err.print(e);
                }
                sender.sendMessage(ChatColor.GREEN + "[Notice] "
                        + ChatColor.LIGHT_PURPLE + args[0] + " removed from allowed players.");
                return true;
            }
        }
        return false;
    }

    private boolean rollback(CommandSender sender, String[] args) {
        if ((sender instanceof Player)) {
            Player player = (Player) sender;
            if (player.isOp()) {
                System.out.println(player.getName()
                        + " used command /rollback at coordinates "
                        + player.getLocation().getBlockX() + ","
                        + player.getLocation().getBlockY() + ","
                        + player.getLocation().getBlockZ());
                if (args.length == 2) {
                    int radius = Integer.parseInt(args[0]);
                    if (radius > 10) {
                        radius = 10;
                    }
                    File pluginpath = new File("plugins/BananaProtect");
                    Chunk chunk = player.getWorld().getChunkAt(
                            player.getLocation());
                    int chunkX = chunk.getX();
                    int chunkZ = chunk.getZ();
                    int count = 0;
                    for (int x = 0 - radius; x < radius; x++) {
                        for (int z = 0 - radius; z < radius; z++) {
                            String cname = (chunkX + x) + "."
                                    + (chunkZ + z) + ".yml";
                            Configuration pC = getConfig(new File(
                                    pluginpath + "/" + player.getWorld().getName() + "/" + cname));
                            //System.out.print(pluginpath + "/" + player.getWorld().getName() + "/" + cname);
                            for (String key : pC.getKeys()) {
                                // System.out.println(key);
                                String playername = pC.getString(key
                                        + ".player", "");
                                // String blocktype =
                                // pC.getString(key+".type","");
                                if (playername.equalsIgnoreCase(args[1])) {
                                    String[] locXYZ = key.split(",");
                                    // System.out.println(key);
                                    count++;
                                    Block block = player.getWorld().getBlockAt(
                                            Integer.parseInt(locXYZ[0]),
                                            Integer.parseInt(locXYZ[1]),
                                            Integer.parseInt(locXYZ[2]));
                                    if (block.getY() < 63) {
                                        block.setTypeId(1);
                                    }
                                    if (block.getY() == 63) {
                                        block.setTypeId(2);
                                    }
                                    if (block.getY() > 63) {
                                        block.setTypeId(0);
                                    }
                                    pC.removeProperty(key);
                                }
                            }
                            pC.save();
                        }
                    }
                    sender.sendMessage(ChatColor.GREEN + "[Notice] "
                            + ChatColor.LIGHT_PURPLE + count
                            + " changes rolled back!");
                    return true;
                }
                if (args.length == 3) {
                    int radius = Integer.parseInt(args[0]);
                    if (radius > 10) {
                        radius = 10;
                    }
                    File pluginpath = new File("plugins/CreativeBanana");
                    Chunk chunk = player.getWorld().getChunkAt(
                            player.getLocation());
                    int chunkX = chunk.getX();
                    int chunkZ = chunk.getZ();
                    int count = 0;
                    for (int x = 0 - radius; x < radius; x++) {
                        for (int z = 0 - radius; z < radius; z++) {
                            String cname = (chunkX + x) + "."
                                    + (chunkZ + z) + ".yml";
                            Configuration pC = getConfig(new File(
                                    pluginpath + "/" + cname));

                            for (String key : pC.getKeys()) {
                                // System.out.println(key);
                                String playername = pC.getString(key
                                        + ".player", "");
                                String blocktype = pC.getString(key
                                        + ".type", "");
                                if (playername.equalsIgnoreCase(args[1])
                                        && blocktype.equals(args[2])) {
                                    String[] locXYZ = key.split(",");
                                    // System.out.println(key);
                                    count++;
                                    Block block = player.getWorld().getBlockAt(
                                            Integer.parseInt(locXYZ[0]),
                                            Integer.parseInt(locXYZ[1]),
                                            Integer.parseInt(locXYZ[2]));
                                    if (block.getY() < 63) {
                                        block.setTypeId(1);
                                    }
                                    if (block.getY() == 63) {
                                        block.setTypeId(2);
                                    }
                                    if (block.getY() > 63) {
                                        block.setTypeId(0);
                                    }
                                    pC.removeProperty(key);
                                }
                            }
                            pC.save();
                        }
                    }
                    sender.sendMessage(ChatColor.GREEN + "[Notice] "
                            + ChatColor.LIGHT_PURPLE + count
                            + " changes rolled back!");
                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "[Warning] "
                        + ChatColor.LIGHT_PURPLE
                        + "You must be op to use this command!");
            }
        }
        return false;
    }

    private boolean regenerate(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You aren't a player");
            return true;
        }
        Player player = (Player) sender;
        if (player.isOp()) {
            player.sendMessage(ChatColor.GREEN + "[Notice] "
                    + ChatColor.LIGHT_PURPLE + "Regenerating chunk...");
            player.getWorld().regenerateChunk(
                    player.getWorld().getChunkAt(player.getLocation()).getX(),
                    player.getWorld().getChunkAt(player.getLocation()).getZ());
            return true;
        } else {
            player.sendMessage(ChatColor.RED + "[Warning] "
                    + ChatColor.LIGHT_PURPLE
                    + "You must be opped to use this command!");
        }
        return false;
    }
}