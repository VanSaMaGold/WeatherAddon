package org.vansama.weatheraddon;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class CommandListener implements CommandExecutor {
    private final JavaPlugin plugin;

    public CommandListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }

        if ("reload".equals(label)) {
            reloadConfig(sender);
            return true;
        } else if (args.length == 0) {
            showHelpMenu(sender);
            return true;
        } else {
            sender.sendMessage("Unknown command. Use /weatheraddon help for more information.");
            return false;
        }
    }

    private void reloadConfig(CommandSender sender) {
        plugin.reloadConfig();
        sender.sendMessage("§aConfiguration reloaded!");
    }

    private void showHelpMenu(CommandSender sender) {
        sender.sendMessage("§6------ WeatherAddon Help Menu ------");
        sender.sendMessage("§e/weatheraddon reload §7- Reload the configuration.");
        sender.sendMessage("§e/wa reload §7- Alias for /weatheraddon reload.");
        sender.sendMessage("§e/weatheraddon help §7- Show this help menu.");
        sender.sendMessage("§e/wa §7- Alias for /weatheraddon help.");
        sender.sendMessage("§6------------------------------------");
    }
}