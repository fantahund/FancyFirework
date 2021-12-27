package de.fanta.fancyfirework.commands;

import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.utils.ChatUtil;
import de.iani.cubesideutils.bukkit.commands.SubCommand;
import de.iani.cubesideutils.commands.ArgsParser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnableToggleCommand extends SubCommand {

    private final FancyFirework plugin;
    private final boolean enable;

    public EnableToggleCommand(FancyFirework plugin, boolean enable) {
        this.plugin = plugin;
        this.enable = enable;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String commandString, ArgsParser args) {
        Player p = (Player) sender;
        if (!p.hasPermission(FancyFirework.ADMIN_PERMISSION)) {
            ChatUtil.sendErrorMessage(p, "Keine Berechtigung");
            return true;
        }
        if (enable) {
            if (!plugin.getFireWorkWorks().enabled()) {
                plugin.getFireWorkWorks().setEnabled(true);
                ChatUtil.sendNormalMessage(p, "Feuerwerk Spawnt jetzt zufällig über Spielern.");
            } else {
                ChatUtil.sendErrorMessage(p, "Feuerwerk ist bereits an.");
                return true;
            }
        } else {
            if (plugin.getFireWorkWorks().enabled()) {
                plugin.getFireWorkWorks().setEnabled(false);
                ChatUtil.sendNormalMessage(p, "Feuerwerk Spawnt jetzt nicht mehr zufällig über Spielern.");
            } else {
                ChatUtil.sendErrorMessage(p, "Feuerwerk ist bereits aus.");
                return true;
            }
        }
        return true;
    }
}

