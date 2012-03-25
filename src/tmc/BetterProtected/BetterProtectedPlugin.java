package tmc.BetterProtected;

import com.mongodb.DB;
import com.mongodb.Mongo;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import tmc.BetterProtected.executors.TransformationExecutor;
import tmc.BetterProtected.svc.PlacedBlockRepository;
import tmc.BetterProtected.svc.RemovedBlockRepository;
import tmc.BetterProtected.svc.TransformationService;

import java.net.UnknownHostException;
import java.util.logging.Logger;

public class BetterProtectedPlugin extends JavaPlugin {
    public static String MONGO_CONNECTION_ERROR = "Error connecting to MongoDB:\n\r%s";
    private Mongo mongoConnection;
    private DB betterProtectedDB;
    private Logger log;
    private PlacedBlockRepository placedBlockRepository;
    private RemovedBlockRepository removedBlockRepository;
    private TransformationExecutor transformationExecutor;
    private Server server;
    private TransformationService transformationService;

    @Override
    public void onEnable() {
        initializeServer();
        initializeMongoDB();
        initializeDatabase();
        initializeRepositories();
        initializeServices();
        initializeCommandExecutors();
        log.info("BetterProtected initialization complete.");
    }

    @Override
    public void onDisable() {
        mongoConnection.close();
        log.info("BetterProtected shutdown complete.");
    }

    private void initializeServer() {
        server = this.getServer();
        log = server.getLogger();
    }

    private void initializeMongoDB() {
        try {
            mongoConnection = new Mongo();
        } catch (UnknownHostException e) {
            log.warning(String.format(MONGO_CONNECTION_ERROR, e.toString()));
        }
    }

    private void initializeDatabase() {
        //TODO: grab the name and connection info for the database from the plugin
        betterProtectedDB = mongoConnection.getDB("BetterProtected");
    }

    private void initializeRepositories() {
        placedBlockRepository = new PlacedBlockRepository(betterProtectedDB.getCollection("PlacedBlocks"));
        removedBlockRepository = new RemovedBlockRepository(betterProtectedDB.getCollection("RemovedBlocks"));
    }

    private void initializeServices() {
        transformationService = new TransformationService(placedBlockRepository);
    }

    private void initializeCommandExecutors() {
        transformationExecutor = new TransformationExecutor(log, transformationService);
        getCommand("transform").setExecutor(transformationExecutor);
    }
}
