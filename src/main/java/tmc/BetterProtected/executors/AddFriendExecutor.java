package tmc.BetterProtected.executors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tmc.BetterProtected.domain.Player;
import tmc.BetterProtected.services.PlayerRepository;

public class AddFriendExecutor implements CommandExecutor {
    private PlayerRepository playerRepository;

    public AddFriendExecutor(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length < 1 || strings.length > 1) return false;
        Player player = playerRepository.findByName(commandSender.getName());
        if (player == null) {
            commandSender.sendMessage("You are not a player");
        } else if (playerRepository.findByName(strings[0]) == null) {
            commandSender.sendMessage(ChatColor.DARK_PURPLE + strings[0] + ChatColor.WHITE +
                " is not a valid player name or is not in the system.");
        } else {
            boolean worked = player.addFriend(strings[0]);
            if (worked) {
                playerRepository.save(player);
                commandSender.sendMessage(ChatColor.DARK_PURPLE + strings[0] + ChatColor.WHITE +
                        " has been added to your friends.");
            } else {
                commandSender.sendMessage(ChatColor.DARK_PURPLE + strings[0] + ChatColor.WHITE +
                        " is already a friend.");
            }
        }
        return true;
    }
}
