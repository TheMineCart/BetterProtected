// Copyright (C) 2012 Cyrus Innovation
package tmc.BetterProtected;

import com.mongodb.DB;
import com.mongodb.Mongo;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import tmc.BetterProtected.services.BlockEventRepository;
import tmc.BetterProtected.services.PlayerRepository;

import java.util.logging.Logger;

import static tmc.BetterProtected.Configuration.*;

public class Database {
    private static JavaPlugin betterProtected;
    private static Logger logger;
    private static Server server;
    private static Mongo mongoConnection;
    private static DB db;
    public static final String BLOCK_EVENTS_COLLECTION = "BlockEvents";
    public static final String PLAYERS_COLLECTION = "Players";

    public static void initialize(Configuration configuration, JavaPlugin plugin) {
        betterProtected = plugin;
        logger = plugin.getLogger();
        server = plugin.getServer();
        initializeMongoDB(configuration);
        initializeDatabase(configuration);
        logger.info("Initializing repositories...");
        initializeRepositories();
        logger.info("Indexing collections...");
        indexCollections();
    }

    public static void closeConnection() {
        mongoConnection.close();
    }

    public static Mongo getMongoConnection() {
        return mongoConnection;
    }

    public static DB getDB() {
        return db;
    }

    private static void initializeMongoDB(Configuration configuration) {
        String address = configuration.getDbConnectionInfo().get(ADDRESS_OPTION);
        try {
            mongoConnection = new Mongo(address);
            logger.info("Found MongoDB instance at " + address + ".");
        } catch (Exception e) {
            logger.warning("Could not find MongoDB instance at " + address + "");
            logger.warning(String.format("Error connecting to MongoDB:\n\r%s", e.toString()));
            logger.warning("Disabling due to critical error!!!");
            server.getPluginManager().disablePlugin(betterProtected);
        }
    }

    private static void initializeDatabase(Configuration configuration) {
        String dbName = configuration.getDbConnectionInfo().get(DB_NAME_OPTION);
        logger.info("Connecting to database " + dbName + "...");
        db = mongoConnection.getDB(dbName);

        String user = configuration.getDbConnectionInfo().get(DB_USER_OPTION);
        if(user != null) {
            logger.info("Attempting authentication to database " + dbName + " with user " + user + "...");
            String password = configuration.getDbConnectionInfo().get(DB_PASSWORD_OPTION);
            boolean success = db.authenticate(user, password.toCharArray());
            if (!success) {
                logger.warning("Incorrect Mongo Database Authentication Info: " +
                        "please double check the settings in your config.yml file.");
                logger.warning("Disabling due to critical error!!!");
                server.getPluginManager().disablePlugin(betterProtected);
            } else {
                logger.info("Connection to database " + dbName + " with authentication was successful!");
            }
        } else {
            logger.info("Connection to database " + dbName + " without authentication was successful!");
        }
    }

    private static void initializeRepositories(){
        BlockEventRepository.initialize(db.getCollection(BLOCK_EVENTS_COLLECTION));
        PlayerRepository.initialize(db.getCollection(PLAYERS_COLLECTION));
    }

    private static void indexCollections(){
        BlockEventRepository.initializeIndexes();
        PlayerRepository.initializeIndexes();
    }
}
