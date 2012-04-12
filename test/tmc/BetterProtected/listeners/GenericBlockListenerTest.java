package tmc.BetterProtected.listeners;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tmc.BetterProtected.domain.BlockEvent;
import tmc.BetterProtected.domain.Player;
import tmc.BetterProtected.domain.TestBlock;
import tmc.BetterProtected.domain.TestPlayer;
import tmc.BetterProtected.domain.types.BlockEventType;
import tmc.BetterProtected.services.BlockEventRepository;
import tmc.BetterProtected.services.PlayerRepository;
import tmc.BetterProtected.services.RepositoryTest;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class GenericBlockListenerTest extends RepositoryTest {

    private PlayerRepository playerRepository;
    private BlockEventRepository blockEventRepository;
    private GenericBlockListener genericBlockListener;

    @Before
    public void setUp() throws Exception {
        playerRepository = new PlayerRepository(getCollection("Players"));
        blockEventRepository = new BlockEventRepository(getCollection("BlockEvents"));
        genericBlockListener = new GenericBlockListener(blockEventRepository, playerRepository, newArrayList(6));
    }

    @After
    public void tearDown() throws Exception {
        clearTestData();
    }

    @Test
    public void returnTrueIfPlayerIsFriendOfBlockOwner() {
        TestPlayer player = new TestPlayer("Bob");
        Player jason = new Player("Jason");
        jason.addFriend("Bob");
        playerRepository.save(jason);

        BlockEvent jasonBlockEvent = BlockEvent.newBlockEvent(new TestBlock(1, 1, 1), "Jason", BlockEventType.PLACED);
        blockEventRepository.save(jasonBlockEvent);

        assertThat(genericBlockListener.isPlayerFriendOfBlockEventOwner(player, jasonBlockEvent), is(true));
    }

    @Test
    public void returnFalseIfPlayerIsNotFriendOfBlockOwner() {
        TestPlayer player = new TestPlayer("Bob");
        Player jason = new Player("Jason");
        playerRepository.save(jason);

        BlockEvent jasonBlockEvent = BlockEvent.newBlockEvent(new TestBlock(1, 1, 1), "Jason", BlockEventType.PLACED);
        blockEventRepository.save(jasonBlockEvent);

        assertThat(genericBlockListener.isPlayerFriendOfBlockEventOwner(player, jasonBlockEvent), is(false));
    }

    @Test
    public void returnFalseIfPlayerIsNull() {
        Player jason = new Player("Jason");
        playerRepository.save(jason);

        BlockEvent jasonBlockEvent = BlockEvent.newBlockEvent(new TestBlock(1, 1, 1), "Jason", BlockEventType.PLACED);
        blockEventRepository.save(jasonBlockEvent);

        assertThat(genericBlockListener.isPlayerFriendOfBlockEventOwner(null, jasonBlockEvent), is(false));
    }

    @Test
    public void returnFalseIfBlockEventIsNull() {
        TestPlayer player = new TestPlayer("Bob");
        assertThat(genericBlockListener.isPlayerFriendOfBlockEventOwner(player, null), is(false));
    }
}
