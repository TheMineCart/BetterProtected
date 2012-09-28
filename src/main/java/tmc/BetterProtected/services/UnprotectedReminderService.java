// Copyright (C) 2012 Cyrus Innovation
package tmc.BetterProtected.services;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import tmc.BetterProtected.Configuration;

public class UnprotectedReminderService implements Runnable {
    private static UnprotectedReminderService service = new UnprotectedReminderService();
    private boolean active = false;

    private int protectionReminderInterval = 60000;
    private Server server = null;

    @Override
    public void run() {
        while (active) {
            try {
                Thread.sleep(protectionReminderInterval);
                for(Player player : server.getOnlinePlayers()) {
                    if (!PlayerRepository.findPlayerProtectionByName(player.getName())) {
                        player.sendMessage("Your block protection is currently " +
                                           ChatColor.DARK_RED + "off" + ChatColor.WHITE + ".");
                        player.sendMessage("Run " + ChatColor.DARK_GREEN + "/bp+" + ChatColor.WHITE +
                                           " to re-enable your block protection.");
                    }
                }
            } catch (InterruptedException e) {
                server.getLogger().warning("Thread sleep failed in the UnprotectedReminderService.\n" + e.toString());
                break;
            }
        }
    }

    public static void start() {
        service.active = true;
        new Thread(service).start();
    }

    public static void stop() {
        service.active = false;
    }

    public static void initialize(Configuration configuration, Server server) {
        service.protectionReminderInterval = configuration.getProtectionReminderInterval() * 1000;
        service.server = server;
    }
}
