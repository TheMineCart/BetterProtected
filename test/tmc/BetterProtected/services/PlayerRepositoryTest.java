package tmc.BetterProtected.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tmc.BetterProtected.domain.Player;

import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;
import static org.junit.matchers.JUnitMatchers.hasItems;

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

    @Test
    public void canGetSetOfFriendNames() {
        Player player = new Player("Jason");
        player.addFriend("George");
        player.addFriend("Bob");

        playerRepository.save(player);

        Set<String> friends = playerRepository.findFriendsByName("Jason");

        assertThat(friends.size(), is(2));
        assertThat(friends, hasItems("George", "Bob"));
    }

    @Test
    public void canGetEmptySetOfFriendsIfInvalidPlayerName() {
        Set<String> friends = playerRepository.findFriendsByName("InvalidName");
        assertThat(friends, not(nullValue()));
    }

    @Test
    public void canGetEmptySetOfFriendsIfNullPlayerName() {
        Set<String> friends = playerRepository.findFriendsByName(null);
        assertThat(friends, not(nullValue()));
    }

    @Test
    public void shouldGetPlayerProtection() {
        Player jason = new Player("Jason");
        jason.setProtectionEnabled(true);
        playerRepository.save(jason);

        assertThat(playerRepository.findPlayerProtectionByName("Jason"), is(true));

        jason.setProtectionEnabled(false);
        playerRepository.save(jason);

        assertThat(playerRepository.findPlayerProtectionByName("Jason"), is(false));
    }

    @Test
    public void shouldGetFalsePlayerProtectionIfPlayerIsNotInRepository() {
        assertThat(playerRepository.findPlayerProtectionByName("Jason"), is(false));
    }

}
