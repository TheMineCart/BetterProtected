package tmc.BetterProtected.executors;

import org.bukkit.Chunk;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tmc.BetterProtected.domain.Owner;
import tmc.BetterProtected.domain.World;
import tmc.BetterProtected.services.PlayerRepository;
import tmc.BetterProtected.services.RevertingService;

public class RevertExecutor implements CommandExecutor{
    private PlayerRepository playerRepository;
    private Server server;
    private RevertingService revertingService;

    public RevertExecutor(RevertingService revertingService, PlayerRepository playerRepository, Server server) {
        this.revertingService = revertingService;
        this.playerRepository = playerRepository;
        this.server = server;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length != 2) return false;
        int radius;
        try {
            radius = Integer.parseInt(strings[1]);
        } catch (NumberFormatException e) {
            return false;
        }
        if(commandSender.getName() == null) {
            commandSender.sendMessage("You have to be a logged in Op Player to run this command.");
            return true;
        }
        if (!commandSender.isOp()) {
            commandSender.sendMessage("You do not have access to this command.");
            return true;
        }
        if (playerRepository.findByName(strings[0]) == null) {
            commandSender.sendMessage("Player " + strings[0] + " does not exist.  Please double check your spelling.");
            return true;
        }

        //We are good to go
        Player player = server.getPlayer(commandSender.getName());
        Chunk chunk = player.getLocation().getChunk();
        World world = World.newWorld(player.getWorld());
        Owner owner = new Owner(strings[0]);
        //build a list of chunks for the radius from chunk

        revertingService.revertChunks(radius, owner, player, chunk, world);

        return true;
    }
}
