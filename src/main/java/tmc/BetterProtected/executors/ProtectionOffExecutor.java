// Copyright (C) 2012 Cyrus Innovation
package tmc.BetterProtected.executors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tmc.BetterProtected.domain.Player;
import tmc.BetterProtected.services.PlayerRepository;

public class ProtectionOffExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length > 0) return false;
        Player player = PlayerRepository.findByName(commandSender.getName());

        if (player == null) {
            commandSender.sendMessage("You are not a player");
        } else {
            player.setProtectionEnabled(false);
            PlayerRepository.save(player);
            commandSender.sendMessage("Protection has been " +
                    ChatColor.DARK_RED + "Disabled" + ChatColor.WHITE +
                    " for new blocks that you place.");
        }
        return true;
    }
}
