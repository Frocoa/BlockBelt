package com.joaquin.blockbelt;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Set;


public final class BlockBelt extends JavaPlugin implements Listener {

    private final HashMap<String, String> materialHash = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("BlockBelt started properly!");
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);



        Set<String> keys = this.getConfig().getKeys(false);

        for(String key: keys) {
            List<String> list = this.getConfig().getStringList(key);
            System.out.println("Belt: " + key + " loading...");
            for(String materialString: list) {
                materialHash.put(materialString, key);
                System.out.println(materialString + " added to " + key + " belt");
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
        if (player.isSneaking() || player.getGameMode() != GameMode.CREATIVE) return;

        event.setCancelled(true);

        String itemMaterial = player.getInventory().getItemInMainHand().getType().toString();
        String beltString = materialHash.get(itemMaterial);

        if (beltString == null) {
            player.sendMessage("This item is not part of any belt");
            return;
        }
        List<String> list = this.getConfig().getStringList(beltString);
        MenuFlyweightBuilder menuBuilder = new MenuFlyweightBuilder();
        Menu menu = menuBuilder.createMenu(list);
        menu.applyMenu(player);
    }
}
