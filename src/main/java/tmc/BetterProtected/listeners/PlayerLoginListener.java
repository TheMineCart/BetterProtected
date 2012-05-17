package tmc.BetterProtected.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import tmc.BetterProtected.domain.Player;
import tmc.BetterProtected.services.PlayerRepository;

public class PlayerLoginListener implements Listener {

    private PlayerRepository playerRepository;

    public PlayerLoginListener(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        String playerName = event.getPlayer().getName();
        if (playerRepository.findByName(playerName) == null) {
            playerRepository.save(new Player(playerName));
        }
    }
}
