package tmc.BetterProtected.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tmc.BetterProtected.domain.Player;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

public class PlayerRepositoryTest extends RepositoryTest {

    private PlayerRepository playerRepository;

    @Before
    public void setUp() throws Exception {
        playerRepository = new PlayerRepository(getCollection("Players"));
    }

    @After
    public void tearDown() throws Exception {
        clearTestData();
    }

    @Test
    public void shouldSaveAPlayer() {
        Player player = new Player("Jason");

        playerRepository.save(player);

        Player jason = playerRepository.findByName("Jason");

        assertThat(jason.getUsername(), is("Jason"));
        assertThat(playerRepository.count(), is(1L));
    }

    @Test
    public void playerThatIsNotSavedIsNull() {
        Player player = new Player("Jason");

        playerRepository.save(player);

        Player jason = playerRepository.findByName("George");

        assertThat(jason, nullValue());
    }

    @Test
    public void canSaveChangesToAPlayer() {
        Player player = new Player("Jason");

        playerRepository.save(player);
        player.addFriend("George");
        player.setProtectionEnabled(false);

        playerRepository.save(player);

        Player jason = playerRepository.findByName("Jason");

        assertThat(playerRepository.count(), is(1L));
        assertThat(jason.getFriends(), hasItem("George"));
        assertThat(jason.getProtectionEnabled(), is(false));
    }

}
