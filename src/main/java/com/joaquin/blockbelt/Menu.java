package com.joaquin.blockbelt;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

public class Menu {

    Inventory menuGUI;
    List<String> materialsList;

    public Menu(List<String> materialsList) {


        Inventory menu = Bukkit.createInventory(null, 54, ChatColor.GREEN + "Block Belt");

        // ITEM STACK
        int index = 0;
        for(String materialString: materialsList) {
            ItemStack option = new ItemStack(Material.valueOf(materialString), 1);
            menu.setItem(index++, option);
        }
        this.menuGUI = menu;
        this.materialsList = materialsList;
    }

    public InventoryView applyMenu(Player player) {
        return player.openInventory(this.menuGUI);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(materialsList, menu.materialsList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(materialsList);
    }
}
