package tmc.BetterProtected.listeners;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tmc.BetterProtected.domain.BlockEvent;
import tmc.BetterProtected.domain.Player;
import tmc.BetterProtected.services.BlockEventRepository;
import tmc.BetterProtected.services.PlayerRepository;
import tmc.BukkitTestUtilities.Mocks.TestBlock;
import tmc.BukkitTestUtilities.Mocks.TestPlayer;
import tmc.BukkitTestUtilities.Services.RepositoryTest;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static tmc.BetterProtected.domain.types.BlockEventType.*;

public class GenericBlockListenerTest extends RepositoryTest {

    private GenericBlockListener genericBlockListener;

    @Before
    public void setUp() throws Exception {
        PlayerRepository.initialize(getCollection("Players")) ;
        BlockEventRepository.initialize(getCollection("BlockEvents")) ;
        genericBlockListener = new GenericBlockListener(newArrayList(6));
    }

    @After
    public void tearDown() throws Exception {
        clearTestData();
    }

    @Test
    public void playerCanBreakBlockIfBlockEventTypeIsRemoved() {
        TestPlayer player = new TestPlayer("Bob");
        TestBlock testBlock = new TestBlock(1, 1, 1);

        BlockEvent jasonBlockEvent = BlockEvent.newBlockEvent(new TestBlock(1, 1, 1), "Jason", REMOVED);
        BlockEventRepository.save(jasonBlockEvent);

        assertThat(genericBlockListener.doesPlayerHavePermissionToBreak(player, jasonBlockEvent, testBlock), is(true));
    }

    @Test
    public void playerCanBreakBlockIfBlockEventTypeIsUnprotected() {
        TestPlayer player = new TestPlayer("Bob");
        TestBlock testBlock = new TestBlock(1, 1, 1);

        BlockEvent jasonBlockEvent = BlockEvent.newBlockEvent(new TestBlock(1, 1, 1), "Jason", UNPROTECTED);
        BlockEventRepository.save(jasonBlockEvent);

        assertThat(genericBlockListener.doesPlayerHavePermissionToBreak(player, jasonBlockEvent, testBlock), is(true));
    }

    @Test
    public void playerCanNotBreakBlockIfItIsProtected() {
        TestPlayer player = new TestPlayer("Bob");
        TestBlock testBlock = new TestBlock(1, 1, 1);

        BlockEvent jasonBlockEvent = BlockEvent.newBlockEvent(new TestBlock(1, 1, 1), "Jason", PLACED);
        BlockEventRepository.save(jasonBlockEvent);

        assertThat(genericBlockListener.doesPlayerHavePermissionToBreak(player, jasonBlockEvent, testBlock), is(false));
    }

    @Test
    public void returnTrueIfPlayerIsFriendOfBlockOwner() {
        TestPlayer player = new TestPlayer("Bob");
        Player jason = new Player("Jason");
        jason.addFriend("Bob");
        PlayerRepository.save(jason);

        BlockEvent jasonBlockEvent = BlockEvent.newBlockEvent(new TestBlock(1, 1, 1), "Jason", PLACED);
        BlockEventRepository.save(jasonBlockEvent);

        assertThat(genericBlockListener.isPlayerFriendOfBlockEventOwner(player, jasonBlockEvent), is(true));
    }

    @Test
    public void returnFalseIfPlayerIsNotFriendOfBlockOwner() {
        TestPlayer player = new TestPlayer("Bob");
        Player jason = new Player("Jason");
        PlayerRepository.save(jason);

        BlockEvent jasonBlockEvent = BlockEvent.newBlockEvent(new TestBlock(1, 1, 1), "Jason", PLACED);
        BlockEventRepository.save(jasonBlockEvent);

        assertThat(genericBlockListener.isPlayerFriendOfBlockEventOwner(player, jasonBlockEvent), is(false));
    }

    @Test
    public void returnFalseIfPlayerIsNull() {
        Player jason = new Player("Jason");
        PlayerRepository.save(jason);

        BlockEvent jasonBlockEvent = BlockEvent.newBlockEvent(new TestBlock(1, 1, 1), "Jason", PLACED);
        BlockEventRepository.save(jasonBlockEvent);

        assertThat(genericBlockListener.isPlayerFriendOfBlockEventOwner(null, jasonBlockEvent), is(false));
    }

    @Test
    public void returnFalseIfBlockEventIsNull() {
        TestPlayer player = new TestPlayer("Bob");
        assertThat(genericBlockListener.isPlayerFriendOfBlockEventOwner(player, null), is(false));
    }
}
