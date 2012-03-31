package tmc.BetterProtected;

import com.mongodb.DB;
import com.mongodb.Mongo;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import tmc.BetterProtected.executors.TransformationExecutor;
import tmc.BetterProtected.listeners.BlockListener;
import tmc.BetterProtected.listeners.PlayerListener;
import tmc.BetterProtected.svc.BlockEventRepository;
import tmc.BetterProtected.svc.TransformationService;

import java.net.UnknownHostException;
import java.util.logging.Logger;

public class BetterProtectedPlugin extends JavaPlugin {
    public static String MONGO_CONNECTION_ERROR = "Error connecting to MongoDB:\n\r%s";
    private Mongo mongoConnection;
    private DB betterProtectedDB;
    private Logger logger;
    private BlockEventRepository blockEventRepository;
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
        registerEventListeners();
        logger.info("BetterProtected initialization complete.");
    }

    @Override
    public void onDisable() {
        mongoConnection.close();
        logger.info("BetterProtected shutdown complete.");
    }

    private void initializeServer() {
        server = this.getServer();
        logger = server.getLogger();
    }

    private void initializeMongoDB() {
        try {
            mongoConnection = new Mongo();
        } catch (UnknownHostException e) {
            logger.warning(String.format(MONGO_CONNECTION_ERROR, e.toString()));
        }
    }

    private void initializeDatabase() {
        //TODO: grab the name and connection info for the database from the configuration file
        betterProtectedDB = mongoConnection.getDB("BetterProtected");
    }

    private void initializeRepositories() {
        blockEventRepository = new BlockEventRepository(betterProtectedDB.getCollection("BlockEvents"));
    }

    private void initializeServices() {
        transformationService = new TransformationService(blockEventRepository);
    }

    private void initializeCommandExecutors() {
        transformationExecutor = new TransformationExecutor(logger, transformationService);
        getCommand("transform").setExecutor(transformationExecutor);
    }

    private void registerEventListeners() {
        server.getPluginManager().registerEvents(new BlockListener(blockEventRepository), this);
        server.getPluginManager().registerEvents(new PlayerListener(blockEventRepository), this);
    }
}
