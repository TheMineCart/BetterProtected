package tmc.BetterProtected.domain.builder;

import com.sun.istack.internal.Builder;
import tmc.BetterProtected.domain.Player;

public class PlayerBuilder implements Builder<Player> {

    private String username = "Jason";

    public static PlayerBuilder aPlayer() {
        return new PlayerBuilder();
    }

    @Override
    public Player build() {
        return new Player(username);
    }

    public PlayerBuilder withUsername(String username) {
        this.username = username;
        return this;
    }
}
