package tmc.BetterProtected.svc;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;
import org.junit.Before;
import org.junit.Test;
import tmc.BetterProtected.domain.ProtectedBlock;
import tmc.BetterProtected.domain.ProtectedChunk;
import tmc.BetterProtected.domain.ProtectedChunkKey;
import tmc.BetterProtected.domain.ProtectedWorld;

import java.io.File;
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
    public void singleFileSpike() throws IOException, InvalidConfigurationException {
        String fileName = "test\\fixtures\\files\\-23.-8.yml";
        YamlConfiguration configuration = new YamlConfiguration();

        configuration.load(fileName);
       
        ProtectedBlock block;
        for (String path : configuration.getKeys(false)) {
            String player = configuration.getString(path + ".player");
            int blockType = configuration.getInt(path + ".type");
            String[] coordinates = path.split(",");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            int z = Integer.parseInt(coordinates[2]);
            block = new ProtectedBlock(x, y, z, Material.getMaterial(blockType), player);
            
            System.out.println("I have a block with coordinates: ");
            System.out.println("X: " + block.getX());
            System.out.println("Y: " + block.getY());
            System.out.println("Z: " + block.getZ());
            System.out.println("The owner is: " + block.getPlayer());
            System.out.println("The type is: " + block.getBlockType().toString());
            //And parse the block location
        }


    }

    @Test
    public void fileReaderSpike() {

        File fileFolder = new File(FIXTURE_DIRECTORY);
        File[] files = fileFolder.listFiles();

        ProtectedWorld protectedWorld = new ProtectedWorld();
        ProtectedChunk protectedChunk;

        for (File file : files) {
            if (file.isFile()) {
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
                YamlConfigurationOptions options = yamlConfiguration.options();
                YamlConfiguration configuration = new YamlConfiguration();
                try {
                    configuration.load(file);
                    System.out.println(configuration.get("-355,63,-119"));
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (InvalidConfigurationException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
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
