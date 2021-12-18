package com.joaquin.blockbelt;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Logger;


public final class BlockBelt extends JavaPlugin implements Listener {

    private final HashMap<String, String> materialHash = new HashMap<>();
    private final HashSet<UUID> enabledPlayers = new HashSet<>();
    static public final Set<InventoryView> menuCache = new HashSet<>();

    @Override
    public void onEnable() {
        Logger logger = Bukkit.getLogger();
        PluginDescriptionFile pluginDescriptionFile = this.getDescription();
        logger.info(pluginDescriptionFile.getName() + " Version: " +
                pluginDescriptionFile.getVersion() + " enabled!");

        saveDefaultConfig();

        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new MenuListener(this), this);

        Objects.requireNonNull(getCommand("blockbelt")).setExecutor(new BeltToggleCommand(this));

        Set<String> keys = this.getConfig().getKeys(false);
        for(String key: keys) {
            List<String> list = this.getConfig().getStringList(key);
            for(String materialString: list) {
                materialHash.put(materialString, key);
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerSwapItem(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (player.isSneaking() || player.getGameMode() != GameMode.CREATIVE ||
                !enabledPlayers.contains(player.getUniqueId())) return;

        event.setCancelled(true);

        String itemMaterial = player.getInventory().getItemInMainHand().getType().toString();
        String beltString = materialHash.get(itemMaterial);

        if (beltString == null) {
            player.sendMessage("This item is not part of any belt");
            return;
        }
        List<String> list = this.getConfig().getStringList(beltString);
        MenuFlyweightBuilder menuBuilder = MenuFlyweightBuilder.getInstance();
        Menu menu = menuBuilder.createMenu(list);
        menuCache.add(menu.applyMenu(player));
    }

    public HashSet<UUID> getEnabledPlayers() {
        return this.enabledPlayers;
    }

    public void addEnabledPlayer(UUID uuid) {
        this.enabledPlayers.add(uuid);
    }

    public void removeEnabledPlayer(UUID uuid) {
        this.enabledPlayers.remove(uuid);
    }
}
