package com.joaquin.blockbelt.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.joaquin.blockbelt.BlockBelt;
import com.joaquin.blockbelt.utility.ColorUtility;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.logging.Logger;

@CommandAlias("bb|bbelt|blockbelt")
public class BlockBeltCommand extends BaseCommand {

    private final BlockBelt controller;
    private final Logger logger;

    public BlockBeltCommand(BlockBelt controller, Logger logger) {
        this.controller = controller;
        this.logger = logger;
    }

    @HelpCommand
    @CommandPermission("blockbelt.use")
    @Description("Shows the help menu")
    public void sendInformation(CommandSender sender) {
        sender.sendMessage(ColorUtility.colorFormat("&b&lBlockBelt &7- &fA plugin for builders that allows you to easily change between block groups."));
        sender.sendMessage(ColorUtility.colorFormat("&b&lBlockBelt &7- &fVersion: &e" + controller.getDescription().getVersion()));

        if(controller.getDescription().getAuthors().size() == 1) {
            sender.sendMessage(ColorUtility.colorFormat("&b&lBlockBelt &7- &fAuthor: &e" + controller.getDescription().getAuthors().get(0)));
        } else {
            // print all authors in one string
            StringBuilder sb = new StringBuilder();
            for(String author : controller.getDescription().getAuthors()) {
                sb.append(author).append(", ");
            }
            // remove last comma
            sb.delete(sb.length() - 2, sb.length());
            sender.sendMessage(ColorUtility.colorFormat("&b&lBlockBelt &7- &fAuthors: &e" + sb));
        }

        // print all commands
        sender.sendMessage(ColorUtility.colorFormat("\n\n&b&lBlockBelt &7- &fCommands:"));
        sender.sendMessage(ColorUtility.colorFormat("&b&lBlockBelt &7- &f/bb &7- &fShows this prompt."));
        sender.sendMessage(ColorUtility.colorFormat("&b&lBlockBelt &7- &f/bb toggle &7- &fToggle Block Belt."));
        sender.sendMessage(ColorUtility.colorFormat("&b&lBlockBelt &7- &f/bb reload &7- &fReload Block Belt config."));
    }

    @Subcommand("toggle")
    @CommandPermission("blockbelt.use")
    @Description("Toggles the plugin for yourself.")
    public void onToggle(CommandSender sender) {
        if(!(sender instanceof Player)) {
            logger.warning("This command is meant only for players");
            return;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if(controller.getDisabledPlayers().contains(uuid)) {
            controller.removeDisabledPlayer(uuid);
            player.sendMessage(ColorUtility.colorFormat("&f[&6Block Belt&f] &aEnabled."));
        }
        else {
            controller.addDisabledPlayer(uuid);
            player.sendMessage(ColorUtility.colorFormat("&f[&6Block Belt&f] &7Disabled."));
        }
    }

    @Subcommand("reload")
    @CommandPermission("blockbelt.reload")
    @Description("Reloads the plugin config.")
    public void onReload(CommandSender sender) {

        if (sender instanceof Player) {
            reloadPluginConfig();
            sender.sendMessage(ColorUtility.colorFormat("&f[&6Block Belt&f] &aConfig file reloaded."));
        }
        else {
            reloadPluginConfig();
            controller.getLogger().info("Config reloaded!");
        }
    }

    private void reloadPluginConfig() {
        controller.reloadConfig();
        controller.saveConfig();
        controller.createMaterialHash();
    }
}
