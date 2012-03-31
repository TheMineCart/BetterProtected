package tmc.BetterProtected.svc;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tmc.BetterProtected.domain.BlockCoordinate;
import tmc.BetterProtected.domain.BlockEvent;
import tmc.BetterProtected.domain.ChunkCoordinate;
import tmc.BetterProtected.domain.World;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class TransformationServiceTest extends RepositoryTest{
    private final String FIXTURE_DIRECTORY = "test\\fixtures\\files";
    private TransformationService transformationService;
    private BlockEventRepository blockEventRepository;

    @Before
    public void setUp() throws Exception {
        blockEventRepository = new BlockEventRepository(getCollection("PlacedBlocks"));
        transformationService = new TransformationService(blockEventRepository);
    }

    @After
    public void tearDown() {
        clearTestData();
    }

    @Test
    public void shouldPersistAllPlaceBlocksFromDirectory() {
        World world = new World("test");
        transformationService.persistPlacedBlocksFromFolder(FIXTURE_DIRECTORY, world);

        assertThat(blockEventRepository.count(), is(66816L));

        BlockCoordinate blockCoordinate = new BlockCoordinate(-708L, 63L, -608L);
        List<BlockEvent> blockEvents = blockEventRepository.findByBlockCoordinate(blockCoordinate, world);

        assertThat(blockEvents.get(0).getOwner().getUsername(), is("Katehhh"));
        assertThat(blockEvents.get(0).getMaterial(), is(Material.DIRT));
    }

    @Test(expected = IOException.class)
    public void shouldParseEitherWindowsOrUnixDirectories() throws IOException, InvalidConfigurationException {
        String windowsFile = "random\\directory\\in\\your\\file\\system\\-3.100.yml";
        String unixFile = "random/directory/in/your/file/system/-3.100.yml";

        World world = new World("test");
        transformationService.persistPlacedBlocksFromFile(windowsFile, world);
        transformationService.persistPlacedBlocksFromFile(unixFile, world);
    }

    @Test
    public void shouldGetChunkCoordinateFromValidFileName() {
        String fileName = "random\\director\\in\\your\\file\\system\\-3.100.yml";

        ChunkCoordinate coordinate = transformationService.parseChunkCoordinateFromFileName(fileName);

        assertThat(coordinate, is(new ChunkCoordinate(-3L, 100L)));
    }

    @Test
    public void shouldGetNullChunkCoordinateValueFromInvalidFileName() {
        String fileName = "random\\director\\in\\your\\file\\system\\invalid.yml";

        ChunkCoordinate coordinate = transformationService.parseChunkCoordinateFromFileName(fileName);

        assertThat(coordinate, is(nullValue()));
    }
}
