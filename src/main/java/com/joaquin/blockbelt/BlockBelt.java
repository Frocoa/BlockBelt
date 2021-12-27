package com.joaquin.blockbelt;

import co.aikar.commands.BukkitCommandManager;
import com.joaquin.blockbelt.commands.BlockBeltCommand;
import com.joaquin.blockbelt.events.PlayerSwapItemsListener;
import com.joaquin.blockbelt.menu.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Logger;


public final class BlockBelt extends JavaPlugin {

    private HashMap<String, String> materialHash = new HashMap<>();
    private final HashSet<UUID> toggledPlayers = new HashSet<>();
    private final HashSet<UUID> hotkeyToggled = new HashSet<>();
    public static final Set<InventoryView> menuCache = new HashSet<>();
    private boolean enabledByDefault;
    private boolean quickBelt;

    BukkitCommandManager manager;

    @Override
    public void onEnable() {
        Logger logger = Bukkit.getLogger();
        PluginDescriptionFile pluginDescriptionFile = this.getDescription();
        logger.info(pluginDescriptionFile.getName() + " Version: " +
                pluginDescriptionFile.getVersion() + " enabled!");

        saveDefaultConfig();
        createMaterialHash();
        updateEnabledByDefault();
        updateQuickBelt();

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

    public HashSet<UUID> getToggledPlayers() {
        return this.toggledPlayers;
    }

    public void addToggledPlayer(Player player) {
        this.toggledPlayers.add(player.getUniqueId());
    }

    public void removeToggledPlayer(Player player) {
        this.toggledPlayers.remove(player.getUniqueId());
    }

    public boolean toggledPlayersContains(Player player) {
        return this.toggledPlayers.contains(player.getUniqueId());
    }

    public void addHotkeyToggled(Player player){
        this.hotkeyToggled.add(player.getUniqueId());
    }

    public void removeHotkeyToggled(Player player) {
        this.hotkeyToggled.remove(player.getUniqueId());
    }

    public boolean hotKeyToggledContains(Player player) {
        return this.hotkeyToggled.contains(player.getUniqueId());
    }

    private void createMaterialHash() {
        HashMap<String, String> newMaterialMap = new HashMap<>();
        Set<String> keys = Objects.requireNonNull(
                this.getConfig().getConfigurationSection("BlockBelts"),
                        "BlockBelts Section not found in the config")
                .getKeys(false);

        for(String key: keys) {
            List<String> list = getConfig().getStringList("BlockBelts."+ key + ".Materials");
            for(String materialString: list) newMaterialMap.put(materialString, key);
        }
        this.materialHash = newMaterialMap;
    }

    public HashMap<String, String> getMaterialHash() {
        return this.materialHash;
    }

    public boolean getEnabledByDefault() {
        return this.enabledByDefault;
    }

    private void updateEnabledByDefault() {
        this.enabledByDefault = getConfig().getBoolean("Settings.Enabled by Default");
    }
    
    public boolean getQuickBelt() {
        return this.quickBelt;
    }
    
    private void updateQuickBelt(){
        this.quickBelt = getConfig().getBoolean("Settings.Quick Belt by Default");
    }

    public void reloadPluginConfig() {
        reloadConfig();
        saveConfig();
        createMaterialHash();
        updateEnabledByDefault();
        this.toggledPlayers.clear();
        updateQuickBelt();
        this.hotkeyToggled.clear();

    }
}
