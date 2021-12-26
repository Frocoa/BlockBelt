package com.joaquin.blockbelt.events;

import com.joaquin.blockbelt.BlockBelt;
import com.joaquin.blockbelt.menu.Menu;
import com.joaquin.blockbelt.menu.MenuFlyweightFactory;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.List;

public class PlayerSwapItemsListener implements Listener {

    BlockBelt controller;

    public PlayerSwapItemsListener(BlockBelt controller) {
        this.controller = controller;
    }

    @EventHandler
    public void onPlayerSwapItem(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (!canPlayerUseBelts(player))
            return;

        event.setCancelled(true);

        String itemMaterial = player.getInventory().getItemInMainHand().getType().toString();
        String beltString = controller.getMaterialHash().get(itemMaterial);

        if (beltString == null) {
            player.sendMessage("This item is not part of any belt");
            return;
        }

        List<String> list = controller.getConfig().getStringList("BlockBelts." + beltString + ".Materials");
        MenuFlyweightFactory menuBuilder = MenuFlyweightFactory.getInstance();
        Menu menu = menuBuilder.createMenu(list);
        BlockBelt.menuCache.add(menu.applyMenu(player));
    }

    private boolean canPlayerUseBelts(Player player) {
        boolean notSneaking = !player.isSneaking();
        boolean inCreative = player.getGameMode() == GameMode.CREATIVE;
        boolean beltsEnabled = controller.toggledPlayersContains(player) ^ controller.getEnabledByDefault();
        boolean hasPermission = player.hasPermission("blockbelt.use");

        return notSneaking && inCreative && beltsEnabled && hasPermission;
    }
}
