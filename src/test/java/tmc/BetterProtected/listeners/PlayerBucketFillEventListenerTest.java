package tmc.BetterProtected.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;
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
import static org.bukkit.Material.*;
import static org.bukkit.block.BlockFace.EAST;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static tmc.BetterProtected.domain.types.BlockEventType.PLACED;
import static tmc.BetterProtected.domain.types.BlockEventType.REMOVED;

public class PlayerBucketFillEventListenerTest extends RepositoryTest {
    private PlayerBucketFillEventListener playerBucketFillEventListener;
    private List<Integer> ignoredBlockTypes = newArrayList(0, 6);

    @Before
    public void setUp() throws Exception {
        BlockEventRepository.initialize(getCollection("BlockEvents")) ;
        PlayerRepository.initialize(getCollection("Players")) ;
        playerBucketFillEventListener = new PlayerBucketFillEventListener(ignoredBlockTypes);
    }

    @After
    public void tearDown() {
        clearTestData();
    }

    @Test
    public void playerCanFillFromFreshWaterBlock() {
        TestBlock blockClicked = new TestBlock(2, 1, 1, DIRT);
        blockClicked.setRelative(new TestBlock(1, 1, 1, WATER));
        PlayerBucketFillEvent event = makeEvent("Jason", false, blockClicked);
        playerBucketFillEventListener.onBucketFill(event);

        BlockEvent mostRecentBlockEvent = findMostRecentBlockEvent();
        assertThat(mostRecentBlockEvent.getBlockEventType(), is(REMOVED));
        assertThat(event.isCancelled(), is(false));
    }

    @Test
    public void playerCanFillBucketWithLiquidOfTypeRemoved() {
        TestBlock blockClicked = new TestBlock(2, 1, 1, DIRT);
        blockClicked.setRelative(new TestBlock(1, 1, 1, WATER));
        BlockEventRepository.save(BlockEvent.newBlockEvent(blockClicked.getRelative(), "George", REMOVED));

        PlayerBucketFillEvent event = makeEvent("Jason", false, blockClicked);
        playerBucketFillEventListener.onBucketFill(event);

        List<BlockEvent> blockEvents = BlockEventRepository.findByBlockCoordinate(new BlockCoordinate(1, 1, 1), "test");
        assertThat(blockEvents.size(), is(2));
        assertThat(blockEvents.get(0).getOwner(), is("Jason"));
        assertThat(event.isCancelled(), is(false));
    }

    @Test
    public void playerCanFillBucketFromOwnWater() {
        TestBlock blockClicked = new TestBlock(2, 1, 1, DIRT);
        blockClicked.setRelative(new TestBlock(1, 1, 1, WATER));
        BlockEventRepository.save(BlockEvent.newBlockEvent(blockClicked.getRelative(), "Jason", PLACED));

        PlayerBucketFillEvent event = makeEvent("Jason", false, blockClicked);
        playerBucketFillEventListener.onBucketFill(event);

        BlockEvent mostRecentBlockEvent = findMostRecentBlockEvent();
        assertThat(mostRecentBlockEvent.getBlockEventType(), is(REMOVED));
    }

    @Test
    public void opCanFillBucketFromWaterOwnedByAnotherPlayer() {
        TestBlock blockClicked = new TestBlock(2, 1, 1, DIRT);
        blockClicked.setRelative(new TestBlock(1, 1, 1, WATER));
        BlockEventRepository.save(BlockEvent.newBlockEvent(blockClicked.getRelative(), "George", PLACED));

        PlayerBucketFillEvent event = makeEvent("Jason", true, blockClicked);
        playerBucketFillEventListener.onBucketFill(event);

        BlockEvent mostRecentBlockEvent = findMostRecentBlockEvent();


        assertThat(mostRecentBlockEvent.getOwner(), is("Jason"));
        assertThat(mostRecentBlockEvent.getBlockEventType(), is(REMOVED));
    }

    @Test
    public void playerCannotFillFromAnUnownedWaterSource() {
        TestBlock blockClicked = new TestBlock(2, 1, 1, DIRT);
        blockClicked.setRelative(new TestBlock(1, 1, 1, WATER));
        BlockEventRepository.save(BlockEvent.newBlockEvent(blockClicked.getRelative(), "George", PLACED));

        PlayerBucketFillEvent event = makeEvent("Jason", false, blockClicked);
        playerBucketFillEventListener.onBucketFill(event);

        BlockEvent mostRecentBlockEvent = findMostRecentBlockEvent();
        assertThat(mostRecentBlockEvent.getBlockEventType(), is(PLACED));
    }

    @Test
    public void playerCanFillBucketIfIgnoredWaterMatchesDatabaseCopy() {
        TestBlock blockClicked = new TestBlock(2, 1, 1, DIRT);
        blockClicked.setRelative(new TestBlock(1, 1, 1, WATER));
        playerBucketFillEventListener.ignoredMaterial.add(WATER);
        BlockEventRepository.save(BlockEvent.newBlockEvent(blockClicked.getRelative(), "George", PLACED, WATER));

        PlayerBucketFillEvent event = makeEvent("Jason", false, blockClicked);
        playerBucketFillEventListener.onBucketFill(event);

        BlockEvent mostRecentBlockEvent = findMostRecentBlockEvent();
        assertThat(mostRecentBlockEvent.getBlockEventType(), is(REMOVED));
    }

    @Test
    public void playerCanNotFillBucketIfIgnoredLiquidDoesNotMatchDatabaseCopy() {
        TestBlock blockClicked = new TestBlock(2, 1, 1, DIRT);
        blockClicked.setRelative(new TestBlock(1, 1, 1, WATER));
        playerBucketFillEventListener.ignoredMaterial.add(WATER);
        BlockEventRepository.save(BlockEvent.newBlockEvent(blockClicked.getRelative(), "George", PLACED, DIRT));

        PlayerBucketFillEvent event = makeEvent("Jason", false, blockClicked);
        playerBucketFillEventListener.onBucketFill(event);

        BlockEvent mostRecentBlockEvent = findMostRecentBlockEvent();
        assertThat(mostRecentBlockEvent.getBlockEventType(), is(PLACED));
    }

    private BlockEvent findMostRecentBlockEvent() {
        return BlockEventRepository.findMostRecent(new BlockCoordinate(1, 1, 1), "test");
    }

    private PlayerBucketFillEvent makeEvent(String playerName, boolean isOp, Block blockClicked) {
        TestPlayer player = new TestPlayer(playerName);
        player.setOp(isOp);
        return new PlayerBucketFillEvent(player, blockClicked, EAST, WATER_BUCKET, new ItemStack(WATER_BUCKET));
    }
}
