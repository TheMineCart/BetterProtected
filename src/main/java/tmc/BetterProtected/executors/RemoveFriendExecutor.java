package tmc.BetterProtected.executors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tmc.BetterProtected.domain.Player;
import tmc.BetterProtected.services.PlayerRepository;

public class RemoveFriendExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length < 1 || strings.length > 1) return false;
        Player player = PlayerRepository.findByName(commandSender.getName());
        if (player != null) {
            boolean worked = player.removeFriend(strings[0]);
            if(worked) {
                PlayerRepository.save(player);
                commandSender.sendMessage(ChatColor.DARK_PURPLE + strings[0] + ChatColor.WHITE +
                        " has been removed from your friends.");
            } else {
                commandSender.sendMessage(ChatColor.DARK_PURPLE + strings[0] + ChatColor.WHITE +
                        " is not one of your friends.");
            }
            return true;
        }
        return false;
    }
}
