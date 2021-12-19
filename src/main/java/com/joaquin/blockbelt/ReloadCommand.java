package com.joaquin.blockbelt;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class ReloadCommand implements CommandExecutor {

    private final BlockBelt controller;

    public ReloadCommand(BlockBelt controller) {
        this.controller = controller;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command,
                             @Nonnull String label, @Nonnull String[] args) {

        if (sender instanceof Player) {
            if (sender.hasPermission("blockbelt.reload")) {
                reloadPluginConfig();
                String message = ColorUtility.colorFormat("&f[&6Block Belt&f] &aConfig file reloaded.");
                sender.sendMessage(message);
                return true;
            }
        }
        else {
            reloadPluginConfig();
            controller.getLogger().info("Config reloaded!");
            return  true;
        }
        return false;
    }

    private void reloadPluginConfig() {
        controller.reloadConfig();
        controller.saveConfig();
        controller.createMaterialHash();
    }
}
