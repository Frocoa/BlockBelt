package com.joaquin.blockbelt;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
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
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command,
                             @Nonnull String label, @Nonnull String[] args) {
        if(!(sender instanceof Player)) {
            logger.warning("This command is meant only for players");
            return false;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if(controller.getEnabledPlayers().contains(uuid)) {
           controller.removeEnabledPlayer(uuid);
            player.sendMessage("[BlockBelt] Your block belt has been disabled!");
        }
        else {
            controller.addEnabledPlayer(uuid);
            player.sendMessage("[BlockBelt] Your block belt has been enabled!");
        }
        return true;
    }
}
