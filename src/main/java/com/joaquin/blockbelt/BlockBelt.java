package com.joaquin.blockbelt;

import co.aikar.commands.BukkitCommandManager;
import com.joaquin.blockbelt.commands.BlockBeltCommand;
import com.joaquin.blockbelt.events.PlayerSwapItemsListener;
import com.joaquin.blockbelt.menu.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Logger;


public final class BlockBelt extends JavaPlugin {

    private HashMap<String, String> materialHash = new HashMap<>();
    private final HashSet<UUID> disabledPlayers = new HashSet<>();
    public static final Set<InventoryView> menuCache = new HashSet<>();

    BukkitCommandManager manager;

    @Override
    public void onEnable() {
        Logger logger = Bukkit.getLogger();
        PluginDescriptionFile pluginDescriptionFile = this.getDescription();
        logger.info(pluginDescriptionFile.getName() + " Version: " +
                pluginDescriptionFile.getVersion() + " enabled!");

        saveDefaultConfig();
        createMaterialHash();

        /* Using a command framework makes managing commands simpler, my personal choice is ACF
        * https://github.com/aikar/commands/wiki/Using-ACF
        */
        manager = new BukkitCommandManager(this);
        registerCommands();
        registerEvents();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerEvents() {
        // It's better to have listeners in a separate class, for organization.
        Bukkit.getPluginManager().registerEvents(new PlayerSwapItemsListener(this), this);
        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);
    }

    private void registerCommands() {
        manager.registerCommand(new BlockBeltCommand(this, getLogger()));
    }

    // These three methods were named "...EnabledPlayers" but I changed it to "DisabledPlayers" because
    // it makes more sense.
    public HashSet<UUID> getDisabledPlayers() {
        return this.disabledPlayers;
    }

    public void addDisabledPlayer(UUID uuid) {
        this.disabledPlayers.add(uuid);
    }

    public void removeDisabledPlayer(UUID uuid) {
        this.disabledPlayers.remove(uuid);
    }

    public void createMaterialHash() {
        HashMap<String, String> newMaterialMap = new HashMap<>();
        Set<String> keys = this.getConfig().getConfigurationSection("BlockBelts").getKeys(false);
        for(String key: keys) {
            List<String> list = getConfig().getStringList("BlockBelts."+ key + ".Materials");
            //List<String> list = this.getConfig().getStringList(key);
            for(String materialString: list) {
                newMaterialMap.put(materialString, key);
            }
        }
        this.materialHash = newMaterialMap;
    }

    public HashMap<String, String> getMaterialHash() {
        return this.materialHash;
    }
}
