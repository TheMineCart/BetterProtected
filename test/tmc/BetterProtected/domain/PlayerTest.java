package tmc.BetterProtected.domain;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.internal.matchers.IsCollectionContaining.hasItem;
import static org.junit.internal.matchers.IsCollectionContaining.hasItems;

public class PlayerTest {
    @Test
    public void canAddFriends() {
        Player jason = new Player("Jason");
        jason.addFriend("Heather");
        jason.addFriend("Marty");

        assertThat(jason.getFriends(), hasItems("Heather", "Marty"));
    }

    @Test
    public void canRemoveFriends() {
        Player jason = new Player("Jason");

        jason.addFriend("John");
        jason.addFriend("Bob");
        assertThat(jason.getFriends().size(), is(2));

        jason.removeFriend("Bob");

        assertThat(jason.getFriends().size(), is(1));
        assertThat(jason.getFriends(), hasItem("John"));
    }

    @Test
    public void removingFriendThatDoesNotExistIsFine() {
        Player jason = new Player("Jason");
        jason.removeFriend(null);
        jason.removeFriend("Nobody");

        assertThat(jason.getFriends().size(), is(0));
    }
}
