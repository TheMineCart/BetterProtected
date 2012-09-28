// Copyright (C) 2012 Cyrus Innovation
package tmc.BetterProtected.executors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tmc.BetterProtected.domain.Player;
import tmc.BetterProtected.services.PlayerRepository;

public class ToggleProtectionExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length > 0) return false;
        Player player = PlayerRepository.findByName(commandSender.getName());

        if (player == null) {
            commandSender.sendMessage("You are not a player");
        } else {
            player.setProtectionEnabled(!player.getProtectionEnabled());
            PlayerRepository.save(player);

            String enabledOrDisabled = "";
            if (player.getProtectionEnabled()) {
                enabledOrDisabled = ChatColor.DARK_GREEN + "Enabled" + ChatColor.WHITE;
            } else {
                enabledOrDisabled = ChatColor.DARK_RED + "Disabled" + ChatColor.WHITE;
            }
            commandSender.sendMessage("Protection has been " + enabledOrDisabled + " for new blocks that you place.");
        }
        return true;
    }
}
