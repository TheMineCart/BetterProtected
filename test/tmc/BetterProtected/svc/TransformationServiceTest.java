package tmc.BetterProtected.svc;

import org.bukkit.configuration.InvalidConfigurationException;
import org.junit.Before;
import org.junit.Test;
import tmc.BetterProtected.domain.ChunkCoordinate;
import tmc.BetterProtected.domain.World;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class TransformationServiceTest {
    private final String FIXTURE_DIRECTORY = "test\\fixtures\\files";
    private TransformationService transformationService;

    @Before
    public void setUp() throws Exception {
        transformationService = new TransformationService();
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
