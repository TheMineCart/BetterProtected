package tmc.BetterProtected.listeners;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tmc.BetterProtected.domain.*;
import tmc.BetterProtected.domain.types.BlockEventType;
import tmc.BetterProtected.services.BlockEventRepository;
import tmc.BukkitTestUtilities.Mocks.*;
import tmc.BukkitTestUtilities.Services.RepositoryTest;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

public class PlayerListenerTest extends RepositoryTest {

    private PlayerListener playerListener;
    private BlockEventRepository blockEventRepository;
    private TestWorld world;
    private TestChunk chunk;

    @Before
    public void setUp() throws Exception {
        blockEventRepository = new BlockEventRepository(getCollection("BlockEvents"));
        playerListener = new PlayerListener(blockEventRepository);
        world = new TestWorld("test");
        chunk = new TestChunk(1, 1, world);
    }

    @After
    public void tearDown() {
        clearTestData();
    }

    @Test
    public void probingARemovedBlockDoesNotSendPlayerMessage() {
        TestPlayer player = new TestPlayer(Material.STICK);
        player.setOp(true);
        TestBlock blockClicked = new TestBlock(1, 1, 1, Material.DIRT, chunk);
        blockEventRepository.save(BlockEvent.newBlockEvent(blockClicked, "Joe", BlockEventType.REMOVED));

        PlayerInteractEvent playerInteractEvent = new PlayerInteractEvent(player, Action.LEFT_CLICK_BLOCK,
                player.getItemInHand(), blockClicked, BlockFace.NORTH);
        playerListener.playerHitsBlockWithStick(playerInteractEvent);

        assertThat(player.getMessage(), nullValue());
    }

    @Test
    public void probingAnUnownedBlockDoesNotSendPlayerMessage() {
        TestPlayer player = new TestPlayer(Material.STICK);
        player.setOp(true);
        TestBlock blockClicked = new TestBlock(1, 1, 1, Material.DIRT, chunk);

        PlayerInteractEvent playerInteractEvent = new PlayerInteractEvent(player, Action.LEFT_CLICK_BLOCK,
                player.getItemInHand(), blockClicked, BlockFace.NORTH);
        playerListener.playerHitsBlockWithStick(playerInteractEvent);

        assertThat(player.getMessage(), nullValue());
    }

    @Test
    public void probingOwnedBlockWithOtherMaterialDoesNothing() {
        TestPlayer player = new TestPlayer(Material.BOAT);
        player.setOp(true);
        TestBlock blockClicked = new TestBlock(1, 1, 1, Material.DIRT, chunk);
        blockEventRepository.save(BlockEvent.newBlockEvent(blockClicked, "Joe", BlockEventType.PLACED));

        PlayerInteractEvent playerInteractEvent = new PlayerInteractEvent(player, Action.LEFT_CLICK_BLOCK,
                player.getItemInHand(), blockClicked, BlockFace.NORTH);
        playerListener.playerHitsBlockWithStick(playerInteractEvent);

        assertThat(player.getMessage(), nullValue());
    }

    @Test
    public void probingOwnedBlockSendsPlayerMessage() {
        TestPlayer player = new TestPlayer(Material.STICK);
        player.setOp(true);
        TestBlock blockClicked = new TestBlock(1, 1, 1, Material.DIRT, chunk);
        blockEventRepository.save(BlockEvent.newBlockEvent(blockClicked, "Joe", BlockEventType.PLACED));

        PlayerInteractEvent playerInteractEvent = new PlayerInteractEvent(player, Action.LEFT_CLICK_BLOCK,
                player.getItemInHand(), blockClicked, BlockFace.NORTH);
        playerListener.playerHitsBlockWithStick(playerInteractEvent);

        assertThat(player.getMessage(), notNullValue());
        assertThat(player.getMessage(), containsString("Joe"));
        assertThat(player.getMessage(), containsString("DIRT"));
    }

    /////////////////////////////////////////////
    @Test
    public void probingARemovedLiquidDoesNotSendPlayerMessage() {
        TestPlayer player = new TestPlayer(Material.EGG);
        player.setOp(true);
        TestBlock blockEgged = new TestBlock(1, 1, 1, Material.LAVA, chunk);
        world.addBlock(blockEgged);
        blockEventRepository.save(BlockEvent.newBlockEvent(blockEgged, "Joe", BlockEventType.REMOVED));

        TestEgg egg = new TestEgg(world, blockEgged);

        PlayerEggThrowEvent playerEggThrowEvent = new PlayerEggThrowEvent(player, egg, false, (byte) 0, EntityType.UNKNOWN);
        playerListener.playerThrowsEggAtBlock(playerEggThrowEvent);

        assertThat(player.getMessage(), nullValue());
    }

    @Test
    public void probingAnUnownedLiquidDoesNotSendPlayerMessage() {
        TestPlayer player = new TestPlayer(Material.EGG);
        player.setOp(true);
        TestBlock blockEgged = new TestBlock(1, 1, 1, Material.LAVA, chunk);
        world.addBlock(blockEgged);

        TestEgg egg = new TestEgg(world, blockEgged);

        PlayerEggThrowEvent playerEggThrowEvent = new PlayerEggThrowEvent(player, egg, false, (byte) 0, EntityType.UNKNOWN);
        playerListener.playerThrowsEggAtBlock(playerEggThrowEvent);

        assertThat(player.getMessage(), nullValue());
    }

    @Test
    public void probingOwnedLiquidDoesNotSendNonOpPlayerMessage() {
        TestPlayer player = new TestPlayer(Material.EGG);
        player.setOp(false);
        TestBlock blockEgged = new TestBlock(1, 1, 1, Material.LAVA, chunk);
        world.addBlock(blockEgged);
        blockEventRepository.save(BlockEvent.newBlockEvent(blockEgged, "Joe", BlockEventType.PLACED));

        TestEgg egg = new TestEgg(world, blockEgged);

        PlayerEggThrowEvent playerEggThrowEvent = new PlayerEggThrowEvent(player, egg, false, (byte) 0, EntityType.UNKNOWN);
        playerListener.playerThrowsEggAtBlock(playerEggThrowEvent);

        assertThat(player.getMessage(), nullValue());
    }

    @Test
    public void probingOwnedLiquidSendsPlayerMessage() {
        TestPlayer player = new TestPlayer(Material.EGG);
        player.setOp(true);
        TestBlock blockEgged = new TestBlock(1, 1, 1, Material.LAVA, chunk);
        world.addBlock(blockEgged);
        blockEventRepository.save(BlockEvent.newBlockEvent(blockEgged, "Joe", BlockEventType.PLACED));

        TestEgg egg = new TestEgg(world, blockEgged);

        PlayerEggThrowEvent playerEggThrowEvent = new PlayerEggThrowEvent(player, egg, false, (byte) 0, EntityType.UNKNOWN);
        playerListener.playerThrowsEggAtBlock(playerEggThrowEvent);

        assertThat(player.getMessage(), notNullValue());
        assertThat(player.getMessage(), containsString("Joe"));
        assertThat(player.getMessage(), containsString("LAVA"));
    }

}
