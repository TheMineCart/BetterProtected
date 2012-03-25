package tmc.BetterProtected;

import com.mongodb.DB;
import com.mongodb.Mongo;
import org.bukkit.Server;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import tmc.BetterProtected.executors.TransformationExecutor;
import tmc.BetterProtected.svc.PlacedBlockRepository;
import tmc.BetterProtected.svc.RemovedBlockRepository;

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
    private SimpleCommandMap commandMap;

    @Override
    public void onEnable() {
        initializeServer();
        initializeMongoDB();
        initializeDatabase();
        initializeRepositories();
        initializeCommandExecutors();
    }

    @Override
    public void onDisable() {
        mongoConnection.close();
    }

    private void initializeServer() {
        server = this.getServer();
        log = server.getLogger();
        commandMap = new SimpleCommandMap(server);
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

    private void initializeCommandExecutors() {
        transformationExecutor = new TransformationExecutor(commandMap);
    }
}
