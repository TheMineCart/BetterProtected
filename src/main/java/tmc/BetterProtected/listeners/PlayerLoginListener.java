// Copyright (C) 2012 Cyrus Innovation
package tmc.BetterProtected.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import tmc.BetterProtected.domain.Player;
import tmc.BetterProtected.services.PlayerRepository;

public class PlayerLoginListener implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        String playerName = event.getPlayer().getName();
        if (PlayerRepository.findByName(playerName) == null) {
            PlayerRepository.save(new Player(playerName));
        }
    }
}
