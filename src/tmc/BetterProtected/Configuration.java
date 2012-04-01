package tmc.BetterProtected;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static org.bukkit.Material.*;

public class Configuration {

    public static final String DB_CONNECTION_SECTION = "MongoDbConnectionInfo";
    public static final String ADDRESS_OPTION = "address";
    public static final String DB_NAME_OPTION = "dbName";
    public static final String DB_USER_OPTION = "dbUser";
    public static final String DB_PASSWORD_OPTION = "dbPassword";

    public static final String BLOCK_PROTECTION_SETTINGS_SECTION = "BlockProtectionSettings";
    public static final String IGNORE_BLOCK_TYPE_OPTION = "ignoreBlockTypeIds";

    private FileConfiguration configuration;
    private BetterProtectedPlugin plugin;
    private List<Integer> unprotectedBlockIds;
    private Map<String, String> dbConnectionInfo;

    public Configuration(FileConfiguration configuration, BetterProtectedPlugin plugin) {
        this.configuration = configuration;
        this.plugin = plugin;
        if (configuration.getKeys(false).isEmpty()) {
            registerDefaultValues();
        } else {
            ConfigurationSection section = configuration.getConfigurationSection("MongoDbConnectionInfo");
            dbConnectionInfo = new HashMap<String, String>();
            dbConnectionInfo.put(ADDRESS_OPTION, section.getString(ADDRESS_OPTION));
            dbConnectionInfo.put(DB_NAME_OPTION, section.getString(DB_NAME_OPTION));
            dbConnectionInfo.put(DB_USER_OPTION, section.getString(DB_USER_OPTION));
            dbConnectionInfo.put(DB_PASSWORD_OPTION, section.getString(DB_PASSWORD_OPTION));
            unprotectedBlockIds = configuration.getIntegerList("BlockProtectionSettings.ignoreBlockTypeIds");
        }
    }

    private void registerDefaultValues() {
        dbConnectionInfo = new HashMap<String, String>();
        dbConnectionInfo.put(ADDRESS_OPTION, "localhost");
        dbConnectionInfo.put(DB_NAME_OPTION, "BetterProtected");
        dbConnectionInfo.put(DB_USER_OPTION, "root");
        dbConnectionInfo.put(DB_PASSWORD_OPTION, "password");
        configuration.createSection(DB_CONNECTION_SECTION, dbConnectionInfo);

        Map<String, Object> blockProtectionSettings = new HashMap<String, Object>();
        unprotectedBlockIds = newArrayList(AIR.getId(), SAPLING.getId(), SAND.getId(), GRAVEL.getId());
        blockProtectionSettings.put(IGNORE_BLOCK_TYPE_OPTION, unprotectedBlockIds);
        configuration.createSection(BLOCK_PROTECTION_SETTINGS_SECTION, blockProtectionSettings);

        plugin.saveConfig();
    }

    public List<Integer> getUnprotectedBlockIds() {
        return unprotectedBlockIds;
    }

    public Map<String, String> getDbConnectionInfo() {
        return dbConnectionInfo;
    }
}
