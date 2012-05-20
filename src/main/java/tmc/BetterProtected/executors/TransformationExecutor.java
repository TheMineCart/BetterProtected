package tmc.BetterProtected.executors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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

        String world = strings[0];
        boolean returnStatus = transformationService.persistPlacedBlocksFromFolder(BANANA_PROTECT_DIRECTORY + world, world);

        if(returnStatus){
            String successMessage = "Transformation of block information for " + world + " is complete!";
            log.info(successMessage);
            commandSender.sendMessage(successMessage);
        } else {
            String failureMessage;
            log.warning("Transformation of block information for " + world + " FAILED!");
            commandSender.sendMessage("Transformation of block information for " + world + ChatColor.DARK_RED + " FAILED!");
            failureMessage = "Did you spell " + world + " correctly?";
            log.warning(failureMessage);
            commandSender.sendMessage(failureMessage);
        }
        return true;
    }
}
