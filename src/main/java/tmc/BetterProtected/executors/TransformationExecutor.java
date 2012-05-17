package tmc.BetterProtected.executors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tmc.BetterProtected.domain.World;
import tmc.BetterProtected.services.TransformationService;

import java.util.logging.Logger;

public class TransformationExecutor implements CommandExecutor {
    private final static String BANANA_PROTECT_DIRECTORY = "plugins/BananaProtect/";
    private Logger log;
    private TransformationService transformationService;

    public TransformationExecutor(Logger log, TransformationService transformationService) {
        this.log = log;
        this.transformationService = transformationService;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length < 1 || strings.length > 1) return false;

        World world = new World(strings[0]);
        boolean returnStatus = transformationService.persistPlacedBlocksFromFolder(BANANA_PROTECT_DIRECTORY + world.getName(), world);

        if(returnStatus){
            String successMessage = "Transformation of block information for " + world.getName() + " is complete!";
            log.info(successMessage);
            commandSender.sendMessage(successMessage);
        } else {
            String failureMessage;
            log.warning("Transformation of block information for " + world.getName() + " FAILED!");
            commandSender.sendMessage("Transformation of block information for " + world.getName() + ChatColor.DARK_RED + " FAILED!");
            failureMessage = "Did you spell " + world.getName() + " correctly?";
            log.warning(failureMessage);
            commandSender.sendMessage(failureMessage);
        }
        return true;
    }
}
