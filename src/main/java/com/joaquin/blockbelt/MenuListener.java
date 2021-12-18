package com.joaquin.blockbelt;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuListener implements Listener {

    private final BlockBelt controller;

    public MenuListener(BlockBelt controller) {
        this.controller = controller;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (player.getGameMode() != GameMode.CREATIVE) return;

        if (BlockBelt.menuCache.contains(event.getView())) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;
            player.getInventory().setItemInMainHand(event.getCurrentItem());
            event.getView().close();
        }
    }
}
