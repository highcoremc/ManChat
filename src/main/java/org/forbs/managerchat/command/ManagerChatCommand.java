package org.forbs.managerchat.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ManagerChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.helpCommand(sender);

        if (!senderIsPlayer(sender)) {
            sender.sendMessage("This command must be run from user!");
        }

        return true;
    }

    private boolean senderIsPlayer(CommandSender sender)
    {
        return sender instanceof Player;
    }

    private void helpCommand(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "|--------------------------------------------|");
        sender.sendMessage(ChatColor.GREEN + "|---------------- "+ ChatColor.AQUA +
                "Manager Chat" +
                ChatColor.GREEN + " ----------------|");
        sender.sendMessage(ChatColor.GREEN + "|--------------------------------------------|");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.RED + "/chat reload" + ChatColor.GREEN +" - Reload the plugin");
        sender.sendMessage(ChatColor.RED + "/chat clear" + ChatColor.GREEN +" - Reload the plugin");
        sender.sendMessage("");
    }

    private void chatClear() {
        for (int i = 0; i < 20; i++) {
            Bukkit.broadcastMessage("");
        }

        Bukkit.broadcastMessage(ChatColor.GOLD + "|-------------------+====+-------------------|");
        Bukkit.broadcastMessage(ChatColor.BOLD + " The chat has been cleared by a staff member.");
        Bukkit.broadcastMessage(ChatColor.GOLD + "|-------------------+====+-------------------|");
    }
}
