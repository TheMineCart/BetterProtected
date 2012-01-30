package tmc.BetterProtected.svc;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.junit.Before;
import org.junit.Test;
import tmc.BetterProtected.domain.*;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;

public class TransformationServiceTest {
    private final String FIXTURE_DIRECTORY = "test\\fixtures\\files";
    private TransformationService transformationService;

    @Before
    public void setUp() throws Exception {
        transformationService = new TransformationService();
    }

    @Test
    public void shouldGetChunkKeyFromValidFileName() {
        String fileName = "random\\director\\in\\your\\file\\system\\-3.100.yml";

        ProtectedChunkKey key = transformationService.parseChunkKeyFromFileName(fileName);

        assertThat(key, is(new ProtectedChunkKey(-3, 100)));
    }

    @Test
    public void shouldGetNullValueFromInvalidFileName() {
        String fileName = "random\\director\\in\\your\\file\\system\\invalid.yml";

        ProtectedChunkKey key = transformationService.parseChunkKeyFromFileName(fileName);

        assertThat(key, is(nullValue()));
    }

    @Test
    public void shouldGetProtectedBlocksFromFile() throws IOException, InvalidConfigurationException {
        String fileName = "test\\fixtures\\10.10.yml";
        ProtectedWorld world = new ProtectedWorld();

        transformationService.transformFile(fileName, world);

        ProtectedChunk actualChunk = world.getChunkFrom(new ProtectedChunkKey(10, 10));
        ProtectedBlock bobBlock = new ProtectedBlock(1, 1, 1, Material.getMaterial(58), "Bob");

        assertThat(actualChunk.getBlocksAt(new ProtectedBlockKey(1)), hasItems(bobBlock));
    }

    @Test(expected = InvalidConfigurationException.class)
    public void shouldNotProcessFileWithNullChunkKey() throws IOException, InvalidConfigurationException {
        String fileName = "test\\fixtures\\nullChunkKey.yml";
        ProtectedWorld world = new ProtectedWorld();

        transformationService.transformFile(fileName, world);

        assertThat(world.numberOfChunks(), is(0));
    }

}
