package tmc.BetterProtected.executors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;

public class TransformationExecutor implements CommandExecutor {

    private SimpleCommandMap commandMap;

    public TransformationExecutor(SimpleCommandMap commandMap) {
        this.commandMap = commandMap;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = null;
        if (commandSender instanceof Player) {
            player = (Player)commandSender;
        }

        if (command.equals(commandMap.getCommand("transform"))) {
            player.chat("The \"Transform\" command is being registered");
            return true;
        }
        return false;
    }
}
