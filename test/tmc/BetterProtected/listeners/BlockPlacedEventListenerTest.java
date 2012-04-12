package tmc.BetterProtected.listeners;

import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tmc.BetterProtected.domain.*;
import tmc.BetterProtected.domain.types.BlockEventType;
import tmc.BetterProtected.services.BlockEventRepository;
import tmc.BetterProtected.services.PlayerRepository;
import tmc.BetterProtected.services.RepositoryTest;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.bukkit.Material.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static tmc.BetterProtected.domain.types.BlockEventType.*;

public class BlockPlacedEventListenerTest extends RepositoryTest {
    public static final String PLAYER_NAME = "Jason";
    private BlockPlacedEventListener blockPlacedEventListener;
    private List<Integer> ignoredBlockTypes = newArrayList(0, 6);
    private BlockEventRepository blockEventRepository;
    private PlayerRepository playerRepository;

    @Before
    public void setUp() throws Exception {
        blockEventRepository = new BlockEventRepository(getCollection("BlockEvents"));
        playerRepository = new PlayerRepository(getCollection("Players"));
        blockPlacedEventListener = new BlockPlacedEventListener(blockEventRepository,
                playerRepository, ignoredBlockTypes);
    }

    @After
    public void tearDown() {
        clearTestData();
    }

    @Test
    public void playerCanPlaceBlockIfNoPriorPlacement() {
        TestPlayer jason = new TestPlayer(PLAYER_NAME, DIRT);
        TestBlock placingBlock = new TestBlock(1, 1, 1, DIRT);

        BlockPlaceEvent event = makeEvent(placingBlock, AIR, DIRT, jason);
        blockPlacedEventListener.onBlockPlace(event);

        assertSavedBlockEventsAndAreTheyCanceled(event, 1, false);
    }

    @Test
    public void playerCanPlaceBlockIfPreviousBlockEventTypeRemoved() {
        TestPlayer jason = new TestPlayer(PLAYER_NAME, DIRT);
        TestBlock placingBlock = new TestBlock(1, 1, 1, DIRT);
        saveBlock(placingBlock, REMOVED, DIRT);

        BlockPlaceEvent event = makeEvent(placingBlock, AIR, DIRT, jason);
        blockPlacedEventListener.onBlockPlace(event);

        assertSavedBlockEventsAndAreTheyCanceled(event, 2, false);
    }

    @Test
    public void playerCanPlaceBlockIfPreviousBlockEventTypePlacedAndOwnedByPlayer() {
        TestPlayer jason = new TestPlayer(PLAYER_NAME, DIRT);
        TestBlock placingBlock = new TestBlock(1, 1, 1, DIRT);
        saveBlock(placingBlock, PLACED, DIRT);

        BlockPlaceEvent event = makeEvent(placingBlock, DIRT, DIRT, jason);
        blockPlacedEventListener.onBlockPlace(event);

        assertSavedBlockEventsAndAreTheyCanceled(event, 2, false);
    }

    @Test
    public void playerCanPlaceBlockIfBlockTypeIsAirButIsOwnedByOtherPlayer() {
        TestPlayer jason = new TestPlayer(PLAYER_NAME, DIRT);
        TestBlock placingBlock = new TestBlock(1, 1, 1, DIRT);
        saveUnownedBlock(placingBlock, PLACED, AIR);

        BlockPlaceEvent event = makeEvent(placingBlock, AIR, DIRT, jason);
        blockPlacedEventListener.onBlockPlace(event);

        assertSavedBlockEventsAndAreTheyCanceled(event, 2, false);
    }

    @Test
    public void playerCanNotPlaceBlockIfPreviousBlockEventTypePlacedAndOwnedByDifferentPlayer() {
        TestPlayer jason = new TestPlayer(PLAYER_NAME, DIRT);
        TestBlock placingBlock = new TestBlock(1, 1, 1, DIRT);
        saveUnownedBlock(placingBlock, PLACED, DIRT);

        BlockPlaceEvent event = makeEvent(placingBlock, DIRT, DIRT, jason);

        blockPlacedEventListener.onBlockPlace(event);

        assertSavedBlockEventsAndAreTheyCanceled(event, 1, true);
    }

    @Test
    public void playerCanAlwaysPlaceIfPlayerIsOp() {
        TestPlayer jason = new TestPlayer(PLAYER_NAME, DIRT);
        jason.setOp(true);
        TestBlock placingBlock = new TestBlock(1, 1, 1, DIRT);
        saveUnownedBlock(placingBlock, PLACED, DIRT);

        BlockPlaceEvent event = makeEvent(placingBlock, DIRT, DIRT, jason);
        blockPlacedEventListener.onBlockPlace(event);

        assertSavedBlockEventsAndAreTheyCanceled(event, 2, false);
    }

    @Test
    public void playerCanNotPlaceBlockInUnownedLiquid() {
        TestPlayer jason = new TestPlayer(PLAYER_NAME, DIRT);
        TestBlock placingBlock = new TestBlock(1, 1, 1, DIRT);

        saveUnownedBlock(placingBlock, PLACED, WATER);
        BlockPlaceEvent event = makeEvent(placingBlock, WATER, DIRT, jason);

        blockPlacedEventListener.onBlockPlace(event);

        assertSavedBlockEventsAndAreTheyCanceled(event, 1, true);
    }

    @Test
    public void doNotRecordPlacedBlockEventIfTypeIsIgnored() {
        blockPlacedEventListener.ignoredMaterial.add(Material.WOOL);

        TestPlayer jason = new TestPlayer(PLAYER_NAME, WOOL);
        TestBlock placingBlock = new TestBlock(1, 1, 1, WOOL);

        BlockPlaceEvent event = makeEvent(placingBlock, AIR, WOOL, jason);

        blockPlacedEventListener.onBlockPlace(event);

        assertSavedBlockEventsAndAreTheyCanceled(event, 0, false);
    }

    //Don't let a player place an ignored block into a liquid
    @Test
    public void doNotLetPlayerPlaceIgnoredMaterialInUnownedBlock() {
        blockPlacedEventListener.ignoredMaterial.add(Material.WOOL);

        TestPlayer jason = new TestPlayer(PLAYER_NAME, WOOL);
        TestBlock placingBlock = new TestBlock(1, 1, 1, WOOL);

        saveUnownedBlock(placingBlock, PLACED, WATER);
        BlockPlaceEvent event = makeEvent(placingBlock, WATER, DIRT, jason);

        blockPlacedEventListener.onBlockPlace(event);

        assertSavedBlockEventsAndAreTheyCanceled(event, 1, true);
    }

    @Test
    public void hoeingDirtIsAllowedIfPlayerOwnsDirt() {
        TestPlayer player = new TestPlayer(PLAYER_NAME, DIAMOND_HOE);
        TestBlock placingBlock = new TestBlock(1, 1, 1, SOIL);
        saveBlock(placingBlock, PLACED, DIRT);

        BlockPlaceEvent event = makeEvent(placingBlock, DIRT, SOIL, player);
        blockPlacedEventListener.onBlockPlace(event);

        assertSavedBlockEventsAndAreTheyCanceled(event, 2, false);
    }

    @Test
    public void playerCannotHoeUnownedBlock() {
        TestPlayer player = new TestPlayer(PLAYER_NAME, DIAMOND_HOE);
        TestBlock placingBlock = new TestBlock(1, 1, 1, SOIL);
        saveUnownedBlock(placingBlock, PLACED, DIRT);

        BlockPlaceEvent event = makeEvent(placingBlock, DIRT, SOIL, player);
        blockPlacedEventListener.onBlockPlace(event);

        assertSavedBlockEventsAndAreTheyCanceled(event, 1, true);
    }

    @Test
    public void soilThatChangesBackToDirtCanBeReHoed() {
        TestPlayer player = new TestPlayer(PLAYER_NAME, DIAMOND_HOE);
        TestBlock placingBlock = new TestBlock(1, 1, 1, SOIL);
        BlockEvent firstBlockEvent = BlockEvent.newBlockEvent(placingBlock, PLAYER_NAME, PLACED);
        blockEventRepository.save(firstBlockEvent);

        BlockPlaceEvent event = makeEvent(placingBlock, DIRT, DIRT, player);
        blockPlacedEventListener.onBlockPlace(event);

        assertSavedBlockEventsAndAreTheyCanceled(event, 2, false);
    }

    @Test
    public void playerDoesNotGainOwnershipFromHoeingUnownedBlock() {
        TestPlayer player = new TestPlayer(PLAYER_NAME, DIAMOND_HOE);
        TestBlock placingBlock = new TestBlock(1, 1, 1, SOIL);

        BlockPlaceEvent event = makeEvent(placingBlock, GRASS, SOIL, player);
        blockPlacedEventListener.onBlockPlace(event);

        assertSavedBlockEventsAndAreTheyCanceled(event, 0, false);
    }

    @Test
    public void blockEventTypeIsUnprotectedIfPlayerProtectionDisabled() {
        TestPlayer player = new TestPlayer(PLAYER_NAME, DIRT);
        TestBlock placingBlock = new TestBlock(1, 1, 1, DIRT);
        Player ourPlayer = new Player(player.getName());
        ourPlayer.setProtectionEnabled(false);
        playerRepository.save(ourPlayer);

        BlockPlaceEvent event = makeEvent(placingBlock, AIR, DIRT, player);
        blockPlacedEventListener.onBlockPlace(event);

        BlockEvent mostRecent = blockEventRepository.findMostRecent(new BlockCoordinate(1, 1, 1), new World(placingBlock.getWorld().getName()));
        assertThat(mostRecent.getBlockEventType(), is(UNPROTECTED));
    }

    @Test
    public void blockEventTypeIsUnprotectedIfPlayerProtectionDisabledAndHoeingDirt() {
        TestPlayer player = new TestPlayer(PLAYER_NAME, DIAMOND_HOE);
        TestBlock placingBlock = new TestBlock(1, 1, 1, SOIL);
        saveBlock(placingBlock, PLACED, DIRT);
        Player ourPlayer = new Player(player.getName());
        ourPlayer.setProtectionEnabled(false);
        playerRepository.save(ourPlayer);

        BlockPlaceEvent event = makeEvent(placingBlock, DIRT, SOIL, player);
        blockPlacedEventListener.onBlockPlace(event);

        BlockEvent mostRecent = blockEventRepository.findMostRecent(new BlockCoordinate(1, 1, 1), new World(placingBlock.getWorld().getName()));
        assertThat(mostRecent.getBlockEventType(), is(UNPROTECTED));
    }

    private BlockEvent saveBlock(TestBlock placingBlock, BlockEventType type, Material material) {
        BlockEvent originalBlockEvent = BlockEvent.newBlockEvent(placingBlock, PLAYER_NAME, type, material);
        blockEventRepository.save(originalBlockEvent);
        return originalBlockEvent;
    }

    private BlockEvent saveUnownedBlock(TestBlock placingBlock, BlockEventType type, Material material) {
        BlockEvent originalBlockEvent = BlockEvent.newBlockEvent(placingBlock, "Roger", type, material);
        blockEventRepository.save(originalBlockEvent);
        return originalBlockEvent;
    }

    private BlockPlaceEvent makeEvent(TestBlock placingBlock, Material previousBlockType, Material itemInHand, TestPlayer player) {
        TestBlock adjacentBlock = new TestBlock(1, 2, 1, DIRT);
        ItemStack playerItemStack = new ItemStack(itemInHand);
        return new BlockPlaceEvent(placingBlock, new TestBlockState(previousBlockType),
                adjacentBlock, playerItemStack, player, true);
    }

    private void assertSavedBlockEventsAndAreTheyCanceled(BlockPlaceEvent event, int numberOfSavedBlockEvents, boolean isEventCanceled) {
        BlockCoordinate coordinate = new BlockCoordinate(1, 1, 1);
        World world = new World("test");
        assertThat(blockEventRepository.findByBlockCoordinate(coordinate, world).size(), is(numberOfSavedBlockEvents));
        assertThat(event.isCancelled(), is(isEventCanceled));
    }
}
