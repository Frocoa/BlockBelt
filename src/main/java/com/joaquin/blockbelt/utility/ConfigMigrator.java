package com.joaquin.blockbelt.utility;

import com.joaquin.blockbelt.BlockBelt;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class ConfigMigrator {

    static public void MigrateToLatest(BlockBelt controller) {
        FileConfiguration config = controller.getConfig();
        if (!config.getKeys(false).contains("Config Version")) {
            migrateConfigV0ToV1(controller);
        }
    }

    /*
    static private String getConfigVersion(FileConfiguration config) {
        return config.getString("Config Version");
    }
    */

    static private void migrateConfigV0ToV1(BlockBelt controller) {

        controller.getLogger().info("Configuration version 0.0 detected, updating to version 1.0");

        FileConfiguration config = controller.getConfig();
        HashMap<String, List<String>> belts = new HashMap<>();
        Set<String> keys  = config.getKeys(false);

        for (String key : keys) {
            List<String> materialList = config.getStringList(key);
            belts.put(key, materialList);
            config.set(key, null);
        }
        config.set("Config Version", 1.0);
        config.set("Settings.Hotkey F instead of Shift+F", true);
        config.set("Settings.Enabled by Default", true);
        for (String key: keys) {
            config.set("BlockBelts." + key + ".Materials", belts.get(key));
        }
        controller.saveConfig();
        controller.getLogger().info("Configuration updated to version 1.0 correctly!");
    }
}
