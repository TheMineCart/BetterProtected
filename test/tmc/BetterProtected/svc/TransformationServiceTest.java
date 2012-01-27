package tmc.BetterProtected.svc;

import org.junit.Before;
import org.junit.Test;
import tmc.BetterProtected.domain.ProtectedChunk;
import tmc.BetterProtected.domain.ProtectedChunkKey;
import tmc.BetterProtected.domain.ProtectedWorld;

import java.io.File;

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
    public void fileReaderSpike() {
        File fileFolder = new File(FIXTURE_DIRECTORY);
        File[] files = fileFolder.listFiles();

        ProtectedWorld protectedWorld = new ProtectedWorld();
        ProtectedChunk protectedChunk;

        for (File file : files) {
            if (file.isFile()) {

                String fileName = file.toString();

                ProtectedChunkKey key = transformationService.parseChunkKeyFromFileName(fileName);
                if (key != null) {
                    protectedChunk = new ProtectedChunk(key.getX(), key.getZ());
                    protectedWorld.addChunk(protectedChunk);
                }
            }
        }
    }
}
