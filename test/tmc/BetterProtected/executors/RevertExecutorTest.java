package tmc.BetterProtected.executors;

import org.bukkit.Material;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import tmc.BetterProtected.domain.*;
import tmc.BetterProtected.domain.types.BlockEventType;
import tmc.BetterProtected.services.BlockEventRepository;
import tmc.BetterProtected.services.PlayerRepository;
import tmc.BetterProtected.services.RepositoryTest;
import tmc.BetterProtected.services.RevertingService;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class RevertExecutorTest extends RepositoryTest {

    private RevertExecutor revertExecutor;
    private BlockEventRepository blockEventRepository;
    private TestWorld world;
    private TestServer server;
    private PlayerRepository playerRepository;

    @Before
    public void setUp() throws Exception {
        server = new TestServer();
        world = new TestWorld("Test");
        server.addWorld(world);

        blockEventRepository = new BlockEventRepository(getCollection("BlockEvents"));
        playerRepository = new PlayerRepository(getCollection("Players"));
        revertExecutor = new RevertExecutor(new RevertingService(blockEventRepository), playerRepository, server);
    }

    @After
    public void tearDown() throws Exception {
        clearTestData();
    }

    @Test
    @Ignore
    public void shouldRemoveBlocksInChunkOfCommandSender() throws Exception {
        TestChunk currentChunk = new TestChunk(1, 1, world);
        TestBlock jasonBlock = new TestBlock(1, 1, 1, Material.DIRT, currentChunk, world);
        jasonBlock.setWorld(world);
        currentChunk.addBlock(jasonBlock);
        currentChunk.getBlock(1, 1, 1);
        world.addBlock(jasonBlock);

        blockEventRepository.save(BlockEvent.newBlockEvent(jasonBlock, "Katie", BlockEventType.REMOVED, Material.GRASS));
        blockEventRepository.save(BlockEvent.newBlockEvent(jasonBlock, "Jason", BlockEventType.PLACED, Material.STONE));
        List<BlockEvent> blockEvents = blockEventRepository.findByBlockCoordinate(new BlockCoordinate(1, 1, 1), World.newWorld(jasonBlock));

        String[] parameters = new String[]{"Jason", "1"};
        boolean result = revertExecutor.onCommand(new TestCommandSender("Charlie", server), null, "revert", parameters);

        assertThat(result, is(true));
        assertThat(blockEvents.size(), is(2));
        assertThat(blockEvents.get(0).getBlockEventType(), is(BlockEventType.REMOVED));
        assertThat(blockEvents.get(0).getOwner().getUsername(), is("Charlie"));
        assertThat(world.getBlockAt(1, 1, 1).getType(), is(Material.GRASS));
    }

    @Test
    public void notEnoughArgumentsOnCommand() {
        String[] parameters = new String[]{""};
        boolean result = revertExecutor.onCommand(new TestCommandSender("Jason", server), null, "revert", parameters);
        assertThat(result, is(false));

        parameters = new String[]{"junk"};
        result = revertExecutor.onCommand(new TestCommandSender("Jason", server), null, "revert", parameters);
        assertThat(result, is(false));
    }

    @Test
    public void tooManyParametersOnCommand() {
        String[]  parameters = new String[]{"Has", "1", "Too Many"};
        boolean result = revertExecutor.onCommand(new TestCommandSender("Jason", server), null, "revert", parameters);
        assertThat(result, is(false));
    }

    @Test
    public void canNotConvertToIntegerOnCommand() {
        String[] parameters = new String[]{"junk", "junk"};
        boolean result = revertExecutor.onCommand(new TestCommandSender("Jason", server), null, "revert", parameters);
        assertThat(result, is(false));
    }

    @Test
    public void correctCommandParameters() {
        String[] parameters = new String[]{"PlayerName", "1"};
        boolean result = revertExecutor.onCommand(new TestCommandSender("Jason", server), null, "revert", parameters);
        assertThat(result, is(true));
    }

    @Test
    public void commandSenderMustBePlayer() {
        TestCommandSender consoleCommandSender = new TestCommandSender(null, server);
        boolean result = revertExecutor.onCommand(consoleCommandSender, null, "revert", new String[]{"PlayerName", "1"});
        assertThat(result, is(true));
        assertThat(consoleCommandSender.getMessage(), is("You have to be a logged in Op Player to run this command."));
    }

    @Test
    public void onlyOpCanRunCommand() {
        TestCommandSender nonOpPlayer = new TestCommandSender("nonOpPlayer", server, false);

        boolean result = revertExecutor.onCommand(nonOpPlayer, null, "revert", new String[]{"Jason", "1"});
        assertThat(result, is(true));
        assertThat(nonOpPlayer.getMessage(), is("You do not have access to this command."));
    }

    @Test
    public void validatePlayerExistsBeforeRevert() {
        TestCommandSender opPlayer = new TestCommandSender("Jason", server, true);

        boolean result = revertExecutor.onCommand(opPlayer, null, "revert", new String[]{"notAPlayer", "1"});
        assertThat(result, is(true));
        assertThat(opPlayer.getMessage(), is("Player notAPlayer does not exist.  Please double check your spelling."));
    }
}
