package tmc.BetterProtected.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tmc.BetterProtected.domain.Player;
import tmc.BukkitTestUtilities.Services.RepositoryTest;

import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;
import static org.junit.matchers.JUnitMatchers.hasItems;

public class PlayerRepositoryTest extends RepositoryTest {

    @Before
    public void setUp() throws Exception {
        PlayerRepository.initialize(getCollection("Players")) ;
    }

    @After
    public void tearDown() throws Exception {
        clearTestData();
    }

    @Test
    public void shouldSaveAPlayer() {
        Player player = new Player("Jason");

        PlayerRepository.save(player);

        Player jason = PlayerRepository.findByName("Jason");

        assertThat(jason.getUsername(), is("Jason"));
        assertThat(PlayerRepository.count(), is(1L));
    }

    @Test
    public void playerThatIsNotSavedIsNull() {
        Player player = new Player("Jason");

        PlayerRepository.save(player);

        Player jason = PlayerRepository.findByName("George");

        assertThat(jason, nullValue());
    }

    @Test
    public void canSaveChangesToAPlayer() {
        Player player = new Player("Jason");

        PlayerRepository.save(player);
        player.addFriend("George");
        player.setProtectionEnabled(false);

        PlayerRepository.save(player);

        Player jason = PlayerRepository.findByName("Jason");

        assertThat(PlayerRepository.count(), is(1L));
        assertThat(jason.getFriends(), hasItem("George"));
        assertThat(jason.getProtectionEnabled(), is(false));
    }

    @Test
    public void canGetSetOfFriendNames() {
        Player player = new Player("Jason");
        player.addFriend("George");
        player.addFriend("Bob");

        PlayerRepository.save(player);

        Set<String> friends = PlayerRepository.findFriendsByName("Jason");

        assertThat(friends.size(), is(2));
        assertThat(friends, hasItems("George", "Bob"));
    }

    @Test
    public void canGetEmptySetOfFriendsIfInvalidPlayerName() {
        Set<String> friends = PlayerRepository.findFriendsByName("InvalidName");
        assertThat(friends, not(nullValue()));
    }

    @Test
    public void canGetEmptySetOfFriendsIfNullPlayerName() {
        Set<String> friends = PlayerRepository.findFriendsByName(null);
        assertThat(friends, not(nullValue()));
    }

    @Test
    public void shouldGetPlayerProtection() {
        Player jason = new Player("Jason");
        jason.setProtectionEnabled(true);
        PlayerRepository.save(jason);

        assertThat(PlayerRepository.findPlayerProtectionByName("Jason"), is(true));

        jason.setProtectionEnabled(false);
        PlayerRepository.save(jason);

        assertThat(PlayerRepository.findPlayerProtectionByName("Jason"), is(false));
    }

    @Test
    public void shouldGetFalsePlayerProtectionIfPlayerIsNotInRepository() {
        assertThat(PlayerRepository.findPlayerProtectionByName("Jason"), is(false));
    }

    @Test
    public void shouldReturnTrueIfValidPlayerName() {
        PlayerRepository.save(new Player("Jason"));
        assertThat(PlayerRepository.validPlayerName("Jason"), is(true));
    }

    @Test
    public void shouldReturnFalseIfInvalidPlayerName() {
        PlayerRepository.save(new Player("Jason"));
        assertThat(PlayerRepository.validPlayerName("JasonCrap"), is(false));
    }

}
