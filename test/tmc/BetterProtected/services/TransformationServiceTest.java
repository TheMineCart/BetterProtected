package tmc.BetterProtected.services;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tmc.BetterProtected.domain.*;

import java.io.IOException;
import java.util.List;

import static org.bukkit.Material.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class TransformationServiceTest extends RepositoryTest{
    private final String FIXTURE_DIRECTORY = "test/fixtures/files";
    private TransformationService transformationService;
    private BlockEventRepository blockEventRepository;
    private TestServer server;
    private TestWorld world;

    @Before
    public void setUp() throws Exception {
        server = new TestServer();
        world = new TestWorld("test");
        server.addWorld(world);

        world.addBlock(new TestBlock(1, 2, 3, AIR));

        blockEventRepository = new BlockEventRepository(getCollection("PlacedBlocks"));
        transformationService = new TransformationService(blockEventRepository, server);
    }

    @After
    public void tearDown() {
        clearTestData();
    }

    @Test
    public void shouldPreferServersBlockTypeIfMismatchFromFile() throws IOException, InvalidConfigurationException {
        TestBlock match = new TestBlock(1, 1, 1, WORKBENCH); // Workbench is 58 same as in file
        TestBlock mismatch = new TestBlock(15, -1, 1, NETHER_BRICK); // 5 in file

        this.world.addBlock(match);
        this.world.addBlock(mismatch);

        World world = new World("test");
        transformationService.persistPlacedBlocksFromFile("test/fixtures/12.12.yml", world);

        BlockCoordinate matchCoord = new BlockCoordinate(1, 1, 1);
        assertThat(blockEventRepository.findMostRecent(matchCoord, world).getMaterial(), is(WORKBENCH));

        BlockCoordinate mismatchCoord = new BlockCoordinate(15, -1, 1);
        assertThat(blockEventRepository.findMostRecent(mismatchCoord, world).getMaterial(), is(NETHER_BRICK));
    }

    @Test
    public void shouldNotPersistBlockIfItIsAirInWorld() throws IOException, InvalidConfigurationException {
        TestBlock bobBlock = new TestBlock(1, 1, 1, Material.getMaterial(58));
        TestBlock rogerBlock = new TestBlock(15, -1, 1, Material.getMaterial(5));
        TestBlock gregBlock = new TestBlock(-1, -20, 1, Material.getMaterial(19));
        TestBlock louisBlock = new TestBlock(-1, -8, -1, Material.getMaterial(73));

        this.world.addBlock(bobBlock);
        this.world.addBlock(rogerBlock);
        this.world.addBlock(gregBlock);
        this.world.addBlock(louisBlock);
        this.world.addBlock(new TestBlock(-11,1,1, AIR));
        this.world.addBlock(new TestBlock(18,1,-1, AIR));
        this.world.addBlock(new TestBlock(1,-5,-1, AIR));

        World world = new World("test");
        transformationService.persistPlacedBlocksFromFile("test/fixtures/11.11.yml", world);

        BlockCoordinate fredCoord = new BlockCoordinate(-11, 1, 1);
        assertThat(blockEventRepository.findMostRecent(fredCoord, world), nullValue());

        BlockCoordinate leonCoord = new BlockCoordinate(18, 1, -1);
        assertThat(blockEventRepository.findMostRecent(leonCoord, world), nullValue());

        BlockCoordinate johnCoord = new BlockCoordinate(1, -5, -1);
        assertThat(blockEventRepository.findMostRecent(johnCoord, world), nullValue());

        BlockCoordinate bobCoord = new BlockCoordinate(1, 1, 1);
        assertThat(blockEventRepository.findMostRecent(bobCoord, world), not(nullValue()));

        BlockCoordinate rogerCoord = new BlockCoordinate(15, -1, 1);
        assertThat(blockEventRepository.findMostRecent(rogerCoord, world), not(nullValue()));

        BlockCoordinate gregCoord = new BlockCoordinate(-1, -20, 1);
        assertThat(blockEventRepository.findMostRecent(gregCoord, world), not(nullValue()));

        BlockCoordinate louisCoord = new BlockCoordinate(-1, -8, -1);
        assertThat(blockEventRepository.findMostRecent(louisCoord, world), not(nullValue()));
    }

    @Test
    public void shouldPersistAllPlaceBlocksFromDirectory() {
        World world = new World("test");
        transformationService.persistPlacedBlocksFromFolder(FIXTURE_DIRECTORY, world);

        assertThat(blockEventRepository.count(), is(66816L));

        BlockCoordinate blockCoordinate = new BlockCoordinate(-708L, 63L, -608L);
        List<BlockEvent> blockEvents = blockEventRepository.findByBlockCoordinate(blockCoordinate, world);

        assertThat(blockEvents.get(0).getOwner().getUsername(), is("Katehhh"));
        assertThat(blockEvents.get(0).getMaterial(), is(DIRT));
    }

    @Test(expected = IOException.class)
    public void shouldParseEitherWindowsOrUnixDirectories() throws IOException, InvalidConfigurationException {
        String windowsFile = "random/directory/in/your/file/system/-3.100.yml";
        String unixFile = "random/directory/in/your/file/system/-3.100.yml";

        World world = new World("test");
        transformationService.persistPlacedBlocksFromFile(windowsFile, world);
        transformationService.persistPlacedBlocksFromFile(unixFile, world);
    }

    @Test
    public void shouldGetChunkCoordinateFromValidFileName() {
        String fileName = "random/director/in/your/file/system/-3.100.yml";

        ChunkCoordinate coordinate = transformationService.parseChunkCoordinateFromFileName(fileName);

        assertThat(coordinate, is(new ChunkCoordinate(-3L, 100L)));
    }

    @Test
    public void shouldGetNullChunkCoordinateValueFromInvalidFileName() {
        String fileName = "random/director/in/your/file/system/invalid.yml";

        ChunkCoordinate coordinate = transformationService.parseChunkCoordinateFromFileName(fileName);

        assertThat(coordinate, is(nullValue()));
    }
}
