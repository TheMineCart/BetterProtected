package tmc.BetterProtected.svc;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.joda.time.DateTime;
import tmc.BetterProtected.domain.*;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransformationService {
    public static final String FILE_REGEX = ".*[\\\\|/](-*\\d{0,5}).(-*\\d{0,5})\\.yml";
    private final Pattern pattern;
    private Logger minecraftLog = Logger.getLogger("Minecraft");
    private PlacedBlockRepository placedBlockRepository;

    public TransformationService() {
        pattern = Pattern.compile(FILE_REGEX);
    }

    public TransformationService(PlacedBlockRepository placedBlockRepository) {
        this();
        this.placedBlockRepository = placedBlockRepository;
    }

    public void persistPlacedBlocksFromFolder(String location, World world) {
        File folder = new File(location);

        File[] files = folder.listFiles();
        for (File file : files) {
            if(file.isFile())    {
                try {
                    persistPlacedBlocksFromFile(file.getAbsolutePath(), world);
                } catch (IOException e) {
                    minecraftLog.severe("java.io.IOException: File Stream failed to parse filename.");
                    e.printStackTrace();
                } catch (InvalidConfigurationException e) {
                    minecraftLog.warning("Unable to parse chunk data from file: " + file.getAbsolutePath());
                }
            }
        }
    }

    public void persistPlacedBlocksFromFile(String fileName, World world) throws IOException, InvalidConfigurationException {
        ChunkCoordinate chunkCoordinate = parseChunkCoordinateFromFileName(fileName);
        if (null == chunkCoordinate) {
            throw new InvalidConfigurationException();
        }

        minecraftLog.info("Parsing Chunk at coordinates x: " + chunkCoordinate.getX() + ", z: " + chunkCoordinate.getZ() + ".");


        YamlConfiguration configuration = new YamlConfiguration();
        configuration.load(fileName);

        DateTime now = new DateTime();

        for (String path : configuration.getKeys(false)) {
            String[] coordinates = path.split(",");
            Long x = Long.parseLong(coordinates[0]);
            Long y = Long.parseLong(coordinates[1]);
            Long z = Long.parseLong(coordinates[2]);

            placedBlockRepository.save(
                    new PlacedBlock(now, new Player(configuration.getString(path + ".player")),
                                    new BlockCoordinate(x, y, z), chunkCoordinate, world,
                                    Material.getMaterial(configuration.getInt(path + ".type"))
                                    ));
        }
    }


    public ChunkCoordinate parseChunkCoordinateFromFileName(String fileName) {
        Matcher matcher = pattern.matcher(fileName);

        if (matcher.matches()) {
            Long x = Long.parseLong(matcher.group(1));
            Long z = Long.parseLong(matcher.group(2));

            return new ChunkCoordinate(x, z);
        }

        return null;
    }
}
