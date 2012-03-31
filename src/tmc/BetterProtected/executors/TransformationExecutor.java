package tmc.BetterProtected.executors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tmc.BetterProtected.domain.World;
import tmc.BetterProtected.svc.TransformationService;

import java.util.logging.Logger;

public class TransformationExecutor implements CommandExecutor {
    private final static String BANANA_PROTECT_DIRECTORY = "plugins/BananaProtect/";
    private Logger log;
    private TransformationService transformationService;

    public TransformationExecutor(Logger log, TransformationService transformationService) {
        this.log = log;
        this.transformationService = transformationService;
        this.log.info("newing the transformationService");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length < 1 || strings.length > 1) return false;

        World world = new World(strings[0]);
        transformationService.persistPlacedBlocksFromFolder(BANANA_PROTECT_DIRECTORY + world.getName(), world);
        log.info("Transformation of block information for " + world.getName() + " is complete!");
        return true;
    }
}
