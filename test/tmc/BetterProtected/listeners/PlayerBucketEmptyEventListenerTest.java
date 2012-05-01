package tmc.BetterProtected.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tmc.BetterProtected.domain.*;
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
import static tmc.BetterProtected.domain.types.BlockEventType.*;

public class PlayerBucketEmptyEventListenerTest extends RepositoryTest {

    private PlayerBucketEmptyEventListener playerBucketEmptyEventListener;
    private List<Integer> ignoredBlockTypes = newArrayList(0, 6);
    private BlockEventRepository blockEventRepository;
    private PlayerRepository playerRepository;

    @Before
    public void setUp() throws Exception {
        blockEventRepository = new BlockEventRepository(getCollection("BlockEvents"));
        playerRepository = new PlayerRepository(getCollection("Players"));
        playerBucketEmptyEventListener = new PlayerBucketEmptyEventListener(blockEventRepository,
                playerRepository, ignoredBlockTypes);
    }

    @After
    public void tearDown() {
        clearTestData();
    }

    @Test
    public void playerCanPourBucketIfNoPriorPlacement() {
        TestBlock blockClicked = new TestBlock(2, 1, 1, DIRT);
        blockClicked.setRelative(new TestBlock(1, 1, 1, AIR));
        playerRepository.save(new Player("Jason"));

        PlayerBucketEmptyEvent event = makeEvent("Jason", false, blockClicked);
        playerBucketEmptyEventListener.onBucketPour(event);

        assertThat(findMostRecentBlockEvent().getBlockEventType(), is(PLACED));
    }

    @Test
    public void playerCanPourBucketIfBlockEventTypeIsRemoved() {
        TestBlock blockClicked = new TestBlock(2, 1, 1, DIRT);
        blockClicked.setRelative(new TestBlock(1, 1, 1, AIR));
        playerRepository.save(new Player("Jason"));
        blockEventRepository.save(BlockEvent.newBlockEvent(blockClicked.getRelative(), "George", REMOVED));

        PlayerBucketEmptyEvent event = makeEvent("Jason", false, blockClicked);
        playerBucketEmptyEventListener.onBucketPour(event);

        assertThat(findMostRecentBlockEvent().getBlockEventType(), is(PLACED));
        assertThat(findMostRecentBlockEvent().getOwner().getUsername(), is("Jason"));
    }

    @Test
    public void playerCanPourBucketIfBlockEventTypeIsUnprotected() {
        TestBlock blockClicked = new TestBlock(2, 1, 1, DIRT);
        blockClicked.setRelative(new TestBlock(1, 1, 1, AIR));
        playerRepository.save(new Player("Jason"));
        blockEventRepository.save(BlockEvent.newBlockEvent(blockClicked.getRelative(), "George", UNPROTECTED));

        PlayerBucketEmptyEvent event = makeEvent("Jason", false, blockClicked);
        playerBucketEmptyEventListener.onBucketPour(event);

        assertThat(findMostRecentBlockEvent().getBlockEventType(), is(PLACED));
        assertThat(findMostRecentBlockEvent().getOwner().getUsername(), is("Jason"));
    }

    @Test
    public void playerCanPourBucketInOwnedWaterSource() {
        TestBlock blockClicked = new TestBlock(2, 1, 1, DIRT);
        blockClicked.setRelative(new TestBlock(1, 1, 1, STATIONARY_WATER));
        blockEventRepository.save(BlockEvent.newBlockEvent(blockClicked.getRelative(), "Jason", PLACED));

        PlayerBucketEmptyEvent event = makeEvent("Jason", false, blockClicked);
        playerBucketEmptyEventListener.onBucketPour(event);

        List<BlockEvent> blockEvents = blockEventRepository.findByBlockCoordinate(new BlockCoordinate(1, 1, 1), new World("test"));
        assertThat(blockEvents.size(), is(2));
    }

    @Test
    public void playerCanNotPourBucketInUnownedWaterSource() {
        TestBlock blockClicked = new TestBlock(2, 1, 1, DIRT);
        blockClicked.setRelative(new TestBlock(1, 1, 1, STATIONARY_WATER));
        blockEventRepository.save(BlockEvent.newBlockEvent(blockClicked.getRelative(), "George", PLACED));

        PlayerBucketEmptyEvent event = makeEvent("Jason", false, blockClicked);
        playerBucketEmptyEventListener.onBucketPour(event);

        List<BlockEvent> blockEvents = blockEventRepository.findByBlockCoordinate(new BlockCoordinate(1, 1, 1), new World("test"));
        assertThat(blockEvents.size(), is(1));
    }

    @Test
    public void opCanPourInOwnedWaterSource() {
        TestBlock blockClicked = new TestBlock(2, 1, 1, DIRT);
        blockClicked.setRelative(new TestBlock(1, 1, 1, STATIONARY_WATER));
        blockEventRepository.save(BlockEvent.newBlockEvent(blockClicked.getRelative(), "Jason", PLACED));

        PlayerBucketEmptyEvent event = makeEvent("Jason", true, blockClicked);
        playerBucketEmptyEventListener.onBucketPour(event);

        List<BlockEvent> blockEvents = blockEventRepository.findByBlockCoordinate(new BlockCoordinate(1, 1, 1), new World("test"));
        assertThat(blockEvents.size(), is(2));
    }

    @Test
    public void opCanPlaceInUnownedWaterSource() {
        TestBlock blockClicked = new TestBlock(2, 1, 1, DIRT);
        blockClicked.setRelative(new TestBlock(1, 1, 1, STATIONARY_WATER));
        blockEventRepository.save(BlockEvent.newBlockEvent(blockClicked.getRelative(), "George", PLACED));

        PlayerBucketEmptyEvent event = makeEvent("Jason", true, blockClicked);
        playerBucketEmptyEventListener.onBucketPour(event);

        List<BlockEvent> blockEvents = blockEventRepository.findByBlockCoordinate(new BlockCoordinate(1, 1, 1), new World("test"));
        assertThat(blockEvents.size(), is(2));
    }

    @Test
    public void playerWithProtectionDisabledPoursUnprotectedWater() {
        TestBlock blockClicked = new TestBlock(2, 1, 1, DIRT);
        blockClicked.setRelative(new TestBlock(1, 1, 1, AIR));
        Player ourPlayer = new Player("Jason");
        ourPlayer.setProtectionEnabled(false);
        playerRepository.save(ourPlayer);

        PlayerBucketEmptyEvent event = makeEvent("Jason", false, blockClicked);
        playerBucketEmptyEventListener.onBucketPour(event);

        assertThat(findMostRecentBlockEvent().getBlockEventType(), is(UNPROTECTED));
        assertThat(findMostRecentBlockEvent().getOwner().getUsername(), is("Jason"));
    }

    @Test
    public void doesNotPreventPouringWaterIfWaterIsIgnoredBlockType() {
        playerBucketEmptyEventListener.ignoredMaterial.add(WATER);
        playerBucketEmptyEventListener.ignoredMaterial.add(STATIONARY_WATER);
        TestBlock blockClicked = new TestBlock(2, 1, 1, DIRT);
        blockClicked.setRelative(new TestBlock(1, 1, 1, STATIONARY_WATER));
        blockEventRepository.save(BlockEvent.newBlockEvent(blockClicked.getRelative(), "George", PLACED));

        PlayerBucketEmptyEvent event = makeEvent("Jason", false, blockClicked);
        playerBucketEmptyEventListener.onBucketPour(event);

        List<BlockEvent> blockEvents = blockEventRepository.findByBlockCoordinate(new BlockCoordinate(1, 1, 1), new World("test"));
        assertThat(blockEvents.size(), is(1));
        assertThat(blockEvents.get(0).getOwner().getUsername(), is("George"));
    }

    @Test
    public void doesNotSaveBlockEventIfWaterIsIgnoredBlockType() {
        playerBucketEmptyEventListener.ignoredMaterial.add(WATER);
        playerBucketEmptyEventListener.ignoredMaterial.add(STATIONARY_WATER);
        TestBlock blockClicked = new TestBlock(2, 1, 1, DIRT);
        blockClicked.setRelative(new TestBlock(1, 1, 1, STATIONARY_WATER));

        PlayerBucketEmptyEvent event = makeEvent("Jason", false, blockClicked);
        playerBucketEmptyEventListener.onBucketPour(event);

        List<BlockEvent> blockEvents = blockEventRepository.findByBlockCoordinate(new BlockCoordinate(1, 1, 1), new World("test"));
        assertThat(blockEvents.size(), is(0));
    }

    private BlockEvent findMostRecentBlockEvent() {
        return blockEventRepository.findMostRecent(new BlockCoordinate(1, 1, 1), new World("test"));
    }

    private PlayerBucketEmptyEvent makeEvent(String playerName, boolean isOp, Block blockClicked) {
        TestPlayer player = new TestPlayer(playerName);
        player.setOp(isOp);
        return new PlayerBucketEmptyEvent(player, blockClicked, EAST, WATER_BUCKET, new ItemStack(WATER_BUCKET));
    }
}
