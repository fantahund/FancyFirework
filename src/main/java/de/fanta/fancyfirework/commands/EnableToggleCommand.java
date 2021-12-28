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
            ChatUtil.sendErrorMessage(p, "No Permission!");
            return true;
        }
        if (enable) {
            if (!plugin.getFireWorkWorks().enabled()) {
                plugin.getFireWorkWorks().setEnabled(true);
                ChatUtil.sendNormalMessage(p, "Fireworks spawning was turned on.");
            } else {
                ChatUtil.sendErrorMessage(p, "Fireworks spawning is already turned on.");
                return true;
            }
        } else {
            if (plugin.getFireWorkWorks().enabled()) {
                plugin.getFireWorkWorks().setEnabled(false);
                ChatUtil.sendNormalMessage(p, "Fireworks spawning was turned off.");
            } else {
                ChatUtil.sendErrorMessage(p, "Fireworks spawning is already turned off.");
                return true;
            }
        }
        return true;
    }
}

