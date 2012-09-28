// Copyright (C) 2012 Cyrus Innovation
package tmc.BetterProtected.executors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tmc.BetterProtected.services.PlayerRepository;

import java.util.Set;

public class ShowFriendsExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length > 1 || (!commandSender.isOp() && args.length > 0)) return false;

        String playerName = "";
        if (commandSender.isOp() && args.length == 1) {
            playerName = args[0];
            if(PlayerRepository.findByName(playerName) == null) {
                commandSender.sendMessage("No player matching the name " + ChatColor.RED + playerName +
                                          ChatColor.WHITE + ". Please check your spelling!");
                return true;
            }
        } else {
            playerName = commandSender.getName();
        }

        Set<String> friends = PlayerRepository.findFriendsByName(playerName);
        commandSender.sendMessage(ChatColor.DARK_PURPLE + playerName + ChatColor.WHITE +
                                  " has " + ChatColor.RED + friends.size() + ChatColor.WHITE + " friends.");
        if (friends.size() > 0) {
            StringBuilder message = new StringBuilder("Friends:");
            boolean firstTime = true;

            for (String friend : friends) {
                if (firstTime) {
                    message.append(" ").append(ChatColor.DARK_PURPLE);
                    firstTime = false;
                } else {
                    message.append(ChatColor.WHITE).append(", ").append(ChatColor.DARK_PURPLE);
                }

                message.append(friend);
            }
            message.append(ChatColor.WHITE).append(".");

            commandSender.sendMessage(message.toString());
        }
        return true;
    }
}
