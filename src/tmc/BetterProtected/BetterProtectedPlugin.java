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

import static tmc.BetterProtected.Configuration.*;

public class BetterProtectedPlugin extends JavaPlugin {
    public static final String BLOCK_EVENTS_COLLECTION = "BlockEvents";
    public static String MONGO_CONNECTION_ERROR = "Error connecting to MongoDB:\n\r%s";
    private Mongo mongoConnection;
    private DB betterProtectedDB;
    private Logger logger;
    private BlockEventRepository blockEventRepository;
    private TransformationExecutor transformationExecutor;
    private Server server;
    private TransformationService transformationService;
    private Configuration configuration;

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
        configuration = new Configuration(this.getConfig(), this);
    }

    private void initializeMongoDB() {
        try {
            mongoConnection = new Mongo(configuration.getDbConnectionInfo().get(ADDRESS_OPTION));
        } catch (UnknownHostException e) {
            logger.warning(String.format(MONGO_CONNECTION_ERROR, e.toString()));
        }
    }

    private void initializeDatabase() {
        String dbName = configuration.getDbConnectionInfo().get(DB_NAME_OPTION);
        betterProtectedDB = mongoConnection.getDB(dbName);

        String user = configuration.getDbConnectionInfo().get(DB_USER_OPTION);
        if(user != null) {
            String password = configuration.getDbConnectionInfo().get(DB_PASSWORD_OPTION);
            boolean success = betterProtectedDB.authenticate(user, password.toCharArray());
            if (!success) {
                logger.warning("Incorrect Mongo Database Authentication Info: " +
                        "please double check the settings in your config.yml file.");
                onDisable();
            }
        }
    }

    private void initializeRepositories() {
        blockEventRepository = new BlockEventRepository(betterProtectedDB.getCollection(BLOCK_EVENTS_COLLECTION));
    }

    private void initializeServices() {
        transformationService = new TransformationService(blockEventRepository, server);
    }

    private void initializeCommandExecutors() {
        transformationExecutor = new TransformationExecutor(logger, transformationService);
        getCommand("transform").setExecutor(transformationExecutor);
    }

    private void registerEventListeners() {
        server.getPluginManager().registerEvents(new BlockListener(blockEventRepository, configuration.getUnprotectedBlockIds()), this);
//        server.getPluginManager().registerEvents(new BlockPhysicsListener(blockEventRepository, server), this);
        server.getPluginManager().registerEvents(new PlayerListener(blockEventRepository), this);
    }
}
