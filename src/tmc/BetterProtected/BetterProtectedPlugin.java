package tmc.BetterProtected;

import com.mongodb.DB;
import com.mongodb.Mongo;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import tmc.BetterProtected.executors.TransformationExecutor;
import tmc.BetterProtected.listeners.*;
import tmc.BetterProtected.services.BlockEventRepository;
import tmc.BetterProtected.services.TransformationService;

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
    private Server server;
    private TransformationService transformationService;
    private Configuration configuration;

    @Override
    public void onEnable() {
        initializeServer();
        logger.info("Beginning BetterProtected Initialization.");
        initializeMongoDB();
        initializeDatabase();
        initializeRepositories();
        initializeCollectionIndexes();
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
        logger.info("Configuring BetterProtected Plugin.");
        configuration = new Configuration(this.getConfig(), this);
    }

    private void initializeMongoDB() {
        try {
            String address = configuration.getDbConnectionInfo().get(ADDRESS_OPTION);
            mongoConnection = new Mongo(address);
            logger.info("Found MongoDB instance at " + address);
        } catch (UnknownHostException e) {
            logger.warning(String.format(MONGO_CONNECTION_ERROR, e.toString()));
        }
    }

    private void initializeDatabase() {
        String dbName = configuration.getDbConnectionInfo().get(DB_NAME_OPTION);
        logger.info("Connecting to database: " + dbName + ".");
        betterProtectedDB = mongoConnection.getDB(dbName);

        String user = configuration.getDbConnectionInfo().get(DB_USER_OPTION);
        if(user != null) {
            logger.info("Attempting authentication to database " + dbName + " with user " + user + ".");
            String password = configuration.getDbConnectionInfo().get(DB_PASSWORD_OPTION);
            boolean success = betterProtectedDB.authenticate(user, password.toCharArray());
            if (!success) {
                logger.warning("Incorrect Mongo Database Authentication Info: " +
                        "please double check the settings in your config.yml file.");
                onDisable();
            } else {
              logger.info("Authentication successful.");
            }
        }
    }

    private void initializeRepositories() {
        logger.info("Initializing Repositories");
        blockEventRepository = new BlockEventRepository(betterProtectedDB.getCollection(BLOCK_EVENTS_COLLECTION));

    }

    private void initializeCollectionIndexes() {
        logger.info("Indexing BlockEvents");
        blockEventRepository.initializeIndexes();
    }

    private void initializeServices() {
        logger.info("Initializing Services");
        transformationService = new TransformationService(blockEventRepository, server);
    }

    private void initializeCommandExecutors() {
        logger.info("Registering Command Executors");
        getCommand("transform").setExecutor(new TransformationExecutor(logger, transformationService));
    }

    private void registerEventListeners() {
        logger.info("Registering Event Listeners");
        server.getPluginManager().registerEvents(new BlockPlacedEventListener(blockEventRepository, configuration.getUnprotectedBlockIds()), this);
        server.getPluginManager().registerEvents(new BlockBreakEventListener(blockEventRepository, configuration.getUnprotectedBlockIds()), this);
        server.getPluginManager().registerEvents(new PlayerBucketFillEventListener(blockEventRepository, configuration.getUnprotectedBlockIds()), this);
        server.getPluginManager().registerEvents(new PlayerBucketEmptyEventListener(blockEventRepository, configuration.getUnprotectedBlockIds()), this);
        server.getPluginManager().registerEvents(new PlayerListener(blockEventRepository), this);
    }
}
