package de.fanta.fancyfirework.utils;

import de.fanta.fancyfirework.FancyFirework;
import de.iani.cubesideutils.bukkit.ChatUtilBukkit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatUtil {

    public static final ChatColor GREEN = ChatColor.of("#52ff9d");
    public static final ChatColor ORANGE = ChatColor.of("#ffac4d");
    public static final ChatColor RED = ChatColor.of("#ff6b6b");
    public static final ChatColor BLUE = ChatColor.of("#87f7ea");

    private ChatUtil() {
        // prevent instances
    }

    public static void sendMessage(CommandSender sender, String colors, Object... messageParts) {
        ChatUtilBukkit.sendMessage(sender, FancyFirework.PREFIX, colors, messageParts);
    }

    public static void sendNormalMessage(CommandSender sender, Object... messageParts) {
        sendMessage(sender, GREEN.toString(), messageParts);
    }

    public static void sendWarningMessage(CommandSender sender, Object... messageParts) {
        sendMessage(sender, ORANGE.toString(), messageParts);
    }

    public static void sendErrorMessage(CommandSender sender, Object... messageParts) {
        sendMessage(sender, RED.toString(), messageParts);
    }

    public static void sendDebugMessage(CommandSender sender, Object... messageParts) {
        if (sender.hasPermission("fanta.debug")) {
            if (sender != null) {
                sendMessage(sender, ChatColor.of("#FF04F7").toString(), messageParts);
            }
        }
    }

    public static void sendBrodcastMessage(Object... messageParts) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendMessage(player, GREEN.toString(), messageParts);
        }
    }
}
