package org.hikarinetwork.hikafly;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (Arrays.stream(args).findAny().isEmpty()) {
            if (sender instanceof Player) {
                if (sender.hasPermission("hikafly.command.fly")) {
                    if (((Player) sender).getGameMode().equals(GameMode.CREATIVE)) {
                        return true;
                    }
                    if (toggleFly((Player) sender)) {
                        sender.sendMessage(getConfig().getString("message-player-fly-enabled", "&aFlight mode enabled"));
                        return true;
                    }
                    sender.sendMessage(getConfig().getString("message-player-fly-disabled", "&cFlight mode disabled"));
                    return true;
                }
                sender.sendMessage(getConfig().getString("message-not-have-permission", "&cYou don't have permission to use this command"));
            }
            sender.sendMessage(getConfig().getString("message-console-usage", "&cYou must be in game to use this command"));
            return true;
        }

        Player target = getServer().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(getConfig().getString("message-target-offline", "&cSelected player currently offline"));
            return true;
        }
        if (sender.hasPermission("hikafly.command.fly.other")) {
            if (target.getGameMode().equals(GameMode.CREATIVE)) {
                return true;
            }
            if (toggleFly(target)) {
                sender.sendMessage(getConfig().getString("message-player-fly-enable-target", "&aSuccess enable flight mode to &e%target%").replace("%target%", target.getName()));
                target.sendMessage(getConfig().getString("message-target-fly-enabled", "&e%player% &aenable flying mode to you").replace("%sender%", sender.getName()));
            }else {
                sender.sendMessage(getConfig().getString("message-player-fly-disable-target", "&cSuccess disable flight mode to &e%target%").replace("%target%", target.getName()));
                target.sendMessage(getConfig().getString("message-target-fly-disabled", "&e%player% &cdisable flying mode to you").replace("%player%", sender.getName()));
            }
            return true;
        }
        sender.sendMessage(getConfig().getString("message-not-have-permission", "&cYou don't have permission to use this command"));
        return true;
    }

    public boolean toggleFly(Player player) {
        if (player.isFlying()) {
            player.setAllowFlight(false);
            player.setFlying(false);
            return false;
        }
        player.setAllowFlight(true);
        player.setFlying(true);
        return true;
    }
}
