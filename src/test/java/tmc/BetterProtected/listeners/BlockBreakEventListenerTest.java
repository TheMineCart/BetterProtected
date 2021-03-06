package tmc.BetterProtected.listeners;

import org.bukkit.event.block.BlockBreakEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tmc.BetterProtected.domain.BlockCoordinate;
import tmc.BetterProtected.domain.BlockEvent;
import tmc.BetterProtected.services.BlockEventRepository;
import tmc.BetterProtected.services.PlayerRepository;
import tmc.BukkitTestUtilities.Mocks.TestBlock;
import tmc.BukkitTestUtilities.Mocks.TestPlayer;
import tmc.BukkitTestUtilities.Services.RepositoryTest;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.bukkit.Material.DIRT;
import static org.bukkit.Material.GRAVEL;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static tmc.BetterProtected.domain.types.BlockEventType.PLACED;
import static tmc.BetterProtected.domain.types.BlockEventType.REMOVED;

public class BlockBreakEventListenerTest extends RepositoryTest {
    private BlockBreakEventListener blockBreakEventListener;
    private List<Integer> ignoredBlockTypes = newArrayList(0, 6);

    @Before
    public void setUp() throws Exception {
        BlockEventRepository.initialize(getCollection("BlockEvents")) ;
        PlayerRepository.initialize(getCollection("Players"));
        blockBreakEventListener = new BlockBreakEventListener(ignoredBlockTypes);
    }

    @After
    public void tearDown() {
        clearTestData();
    }

    @Test
    public void playerCanBreakFreshBlock() {
        TestBlock block = new TestBlock(1, 1, 1, DIRT);
        BlockBreakEvent event = new BlockBreakEvent(block, new TestPlayer("Jason"));
        blockBreakEventListener.onBlockBreak(event);

        BlockEvent mostRecentBlockEvent = BlockEventRepository.findMostRecent(new BlockCoordinate(1, 1, 1), "test");
        assertThat(mostRecentBlockEvent.getBlockEventType(), is(REMOVED));
    }

    @Test
    public void playerCanBreakBlockWithEventTypeRemoved() {
        TestBlock block = new TestBlock(1, 1, 1, DIRT);
        BlockEventRepository.save(BlockEvent.newBlockEvent(block, "George", REMOVED));

        BlockBreakEvent event = new BlockBreakEvent(block, new TestPlayer("Jason"));
        blockBreakEventListener.onBlockBreak(event);

        List<BlockEvent> blockEvents = BlockEventRepository.findByBlockCoordinate(new BlockCoordinate(1, 1, 1), "test");
        assertThat(blockEvents.size(), is(2));
        assertThat(blockEvents.get(0).getOwner(), is("Jason"));
    }

    @Test
    public void playerCanBreakOwnBlock() {
        TestBlock block = new TestBlock(1, 1, 1, DIRT);
        BlockEventRepository.save(BlockEvent.newBlockEvent(block, "Jason", PLACED));

        BlockBreakEvent event = new BlockBreakEvent(block, new TestPlayer("Jason"));
        blockBreakEventListener.onBlockBreak(event);

        BlockEvent mostRecentBlockEvent = BlockEventRepository.findMostRecent(new BlockCoordinate(1, 1, 1), "test");
        assertThat(mostRecentBlockEvent.getBlockEventType(), is(REMOVED));
    }

    @Test
    public void opHasPermissionToBreakBlockOwnedByAnotherPlayer() {
        TestBlock block = new TestBlock(1, 1, 1, DIRT);
        BlockEventRepository.save(BlockEvent.newBlockEvent(block, "George", PLACED));

        TestPlayer player = new TestPlayer("Jason");
        player.setOp(true);
        BlockBreakEvent event = new BlockBreakEvent(block, player);
        blockBreakEventListener.onBlockBreak(event);

        BlockEvent mostRecentBlockEvent = BlockEventRepository.findMostRecent(new BlockCoordinate(1, 1, 1), "test");
        assertThat(mostRecentBlockEvent.getBlockEventType(), is(REMOVED));
    }

    @Test
    public void playerCannotBreakAnUnownedBlock() {
        TestBlock block = new TestBlock(1, 1, 1, DIRT);
        BlockEventRepository.save(BlockEvent.newBlockEvent(block, "George", PLACED));

        TestPlayer player = new TestPlayer("Jason");
        BlockBreakEvent event = new BlockBreakEvent(block, player);
        blockBreakEventListener.onBlockBreak(event);

        BlockEvent mostRecentBlockEvent = BlockEventRepository.findMostRecent(new BlockCoordinate(1, 1, 1), "test");
        assertThat(mostRecentBlockEvent.getBlockEventType(), is(PLACED));
    }

    @Test
    public void playerCanBreakIfIgnoredMaterialMatchesDatabaseCopy() {
        TestBlock block = new TestBlock(1, 1, 1, GRAVEL);
        blockBreakEventListener.ignoredMaterial.add(GRAVEL);
        BlockEventRepository.save(BlockEvent.newBlockEvent(block, "George", PLACED, GRAVEL));

        TestPlayer player = new TestPlayer("Jason");
        BlockBreakEvent event = new BlockBreakEvent(block, player);
        blockBreakEventListener.onBlockBreak(event);

        BlockEvent mostRecentBlockEvent = BlockEventRepository.findMostRecent(new BlockCoordinate(1, 1, 1), "test");
        assertThat(mostRecentBlockEvent.getBlockEventType(), is(REMOVED));
    }

    @Test
    public void playerCanNotBreakIfIgnoredMaterialDoesNotMatchDatabaseCopy() {
        TestBlock block = new TestBlock(1, 1, 1, GRAVEL);
        blockBreakEventListener.ignoredMaterial.add(GRAVEL);
        BlockEventRepository.save(BlockEvent.newBlockEvent(block, "George", PLACED, DIRT));

        TestPlayer player = new TestPlayer("Jason");
        BlockBreakEvent event = new BlockBreakEvent(block, player);
        blockBreakEventListener.onBlockBreak(event);

        BlockEvent mostRecentBlockEvent = BlockEventRepository.findMostRecent(new BlockCoordinate(1, 1, 1), "test");
        assertThat(mostRecentBlockEvent.getBlockEventType(), is(PLACED));
    }
}