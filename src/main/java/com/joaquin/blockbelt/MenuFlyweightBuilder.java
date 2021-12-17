package com.joaquin.blockbelt;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

/**
 * Class that represents the Belt
 */
public class MenuFlyweightBuilder {

    private HashMap<Integer, Menu> cache = new HashMap<>();

    private Menu createMenu(Menu menu) {
        int hashCode = menu.hashCode();
        if(!cache.containsKey(menu.hashCode())) {
            cache.put(hashCode, menu);
        }
        return cache.get(hashCode);
    }

    public Menu createMenu(List<String> materialsList) {
        return createMenu(new Menu(materialsList));
    }
}
