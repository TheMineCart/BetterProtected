package tmc.BetterProtected.domain;

import com.google.gson.Gson;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PlayerTest {
    @Test
    public void canDeserializePlayer() {
        String playerString = "{\"username\":\"Jason\"}";

        Gson gson = new Gson();
        Player player = gson.fromJson(playerString, Player.class);

        assertThat(player.getUsername(), is("Jason"));
    }

    @Test
    public void canSerializePlayer() {
        String expectedJson = "{\"username\":\"Jason\"}";
        Player jason = new Player("Jason");

        assertThat(new Gson().toJson(jason, Player.class), is(expectedJson));
    }
}
