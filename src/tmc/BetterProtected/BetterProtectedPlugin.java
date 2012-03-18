package tmc.BetterProtected;

import com.mongodb.DB;
import com.mongodb.Mongo;
import org.bukkit.plugin.java.JavaPlugin;
import tmc.BetterProtected.svc.PlacedBlockRepository;
import tmc.BetterProtected.svc.RemovedBlockRepository;

import java.net.UnknownHostException;
import java.util.logging.Logger;

public class BetterProtectedPlugin extends JavaPlugin {
    public static String MONGO_CONNECTION_ERROR = "Error connecting to MongoDB:\n\r%s";
    private Mongo mongoConnection;
    private DB betterProtectedDB;
    Logger log = getLogger();
    private PlacedBlockRepository placedBlockRepository;
    private RemovedBlockRepository removedBlockRepository;


    @Override
    public void onEnable() {
        initializeMongoDB();
        initializeDatabase();
        initializeRepositories();
    }

    @Override
    public void onDisable() {
        mongoConnection.close();
    }

    private void initializeMongoDB() {
        try {
            mongoConnection = new Mongo();
        } catch (UnknownHostException e) {
            log.warning(String.format(MONGO_CONNECTION_ERROR, e.toString()));
        }
    }

    private void initializeDatabase() {
        betterProtectedDB = mongoConnection.getDB("BetterProtected");
    }

    private void initializeRepositories() {
        placedBlockRepository = new PlacedBlockRepository(betterProtectedDB.getCollection("PlacedBlocks"));
        removedBlockRepository = new RemovedBlockRepository(betterProtectedDB.getCollection("RemovedBlocks"));
    }
}
