package tmc.BetterProtected;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import tmc.BetterProtected.executors.*;
import tmc.BetterProtected.listeners.*;
import tmc.BetterProtected.services.TransformationService;
import tmc.BetterProtected.services.UnprotectedReminderService;

import java.util.logging.Logger;

public class BetterProtectedPlugin extends JavaPlugin {
    private Logger logger;
    private Server server;
    private TransformationService transformationService;
    private Configuration configuration;

    @Override
    public void onEnable() {
        initializeServer();

        logger.info("Initializing database...");
        Database.initialize(configuration, this);

        if(this.isEnabled()) {
            logger.info("Initializing services...");
            initializeServices();

            logger.info("Starting services...");
            startServices();

            logger.info("Registering command executors...");
            initializeCommandExecutors();

            logger.info("Registering event listeners...");
            registerEventListeners();

            logger.info("BetterProtected initialization complete!");
        }
    }

    @Override
    public void onDisable() {
        Database.closeConnection();
        UnprotectedReminderService.stop();
        logger.info("BetterProtected shutdown complete.");
    }

    private void initializeServer() {
        server = this.getServer();
        logger = this.getLogger();
        logger.info("Configuring...");
        configuration = new Configuration(this.getConfig(), this);
    }

    private void initializeServices() {
        transformationService = new TransformationService(server);
        UnprotectedReminderService.initialize(configuration, server);
    }

    private void startServices() {
        UnprotectedReminderService.start();
    }

    private void initializeCommandExecutors() {
        getCommand("transform").setExecutor(new TransformationExecutor(logger, transformationService));
        getCommand("addfriend").setExecutor(new AddFriendExecutor());
        getCommand("removefriend").setExecutor(new RemoveFriendExecutor());
        getCommand("showfriends").setExecutor(new ShowFriendsExecutor());
        getCommand("toggleprotection").setExecutor(new ToggleProtectionExecutor());
        getCommand("protectionon").setExecutor(new ProtectionOnExecutor());
        getCommand("protectionoff").setExecutor(new ProtectionOffExecutor());
    }

    private void registerEventListeners() {
        server.getPluginManager().registerEvents(new BlockPlacedEventListener(configuration.getUnprotectedBlockIds()), this);
        server.getPluginManager().registerEvents(new BlockBreakEventListener(configuration.getUnprotectedBlockIds()), this);
        server.getPluginManager().registerEvents(new PlayerBucketFillEventListener(configuration.getUnprotectedBlockIds()), this);
        server.getPluginManager().registerEvents(new PlayerBucketEmptyEventListener(configuration.getUnprotectedBlockIds()), this);
        server.getPluginManager().registerEvents(new PlayerListener(), this);
        server.getPluginManager().registerEvents(new PlayerLoginListener(), this);
    }
}
