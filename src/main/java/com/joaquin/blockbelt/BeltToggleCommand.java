package com.joaquin.blockbelt;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.logging.Logger;

public class BeltToggleCommand implements CommandExecutor {

    private final BlockBelt controller;
    private final Logger logger;

    public BeltToggleCommand(BlockBelt controller) {
        this.controller = controller;
        this.logger = controller.getLogger();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            logger.warning("This command is meant only for players");
            return false;
        }

        if(!sender.hasPermission("blockbelt.use"))
            return false;

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if(controller.getEnabledPlayers().contains(uuid)) {
           controller.removeEnabledPlayer(uuid);
           String message = ColorUtility.colorFormat("&f[&6Block Belt&f] &aEnabled.");
           player.sendMessage(message);
        }
        else {
            controller.addEnabledPlayer(uuid);
            String message = ColorUtility.colorFormat("&f[&6Block Belt&f] &7Disabled.");
            player.sendMessage(message);
        }
        return true;
    }
}
