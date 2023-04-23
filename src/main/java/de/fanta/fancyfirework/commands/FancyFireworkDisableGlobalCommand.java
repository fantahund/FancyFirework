package de.fanta.fancyfirework.commands;

import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.utils.ChatUtil;
import de.iani.cubesideutils.bukkit.commands.SubCommand;
import de.iani.cubesideutils.commands.ArgsParser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FancyFireworkDisableGlobalCommand extends SubCommand {

    private final FancyFirework plugin;
    private final boolean enable;

    public FancyFireworkDisableGlobalCommand(FancyFirework plugin, boolean enable) {
        this.plugin = plugin;
        this.enable = enable;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String commandString, ArgsParser args) {
        if (!(sender instanceof Player player)) {
            ChatUtil.sendErrorMessage(sender, "You are not a Player :>");
            return true;
        }

        if (enable) {
            if (plugin.isDisableFireWorkUse()) {
                plugin.setDisableFireWorkUse(false);
                ChatUtil.sendNormalMessage(player, "Fireworks can be used now.");
            } else {
                ChatUtil.sendErrorMessage(player, "Fireworks use is already turned on.");
                return true;
            }
        } else {
            if (!plugin.isDisableFireWorkUse()) {
                plugin.setDisableFireWorkUse(true);
                ChatUtil.sendNormalMessage(player, "Fireworks cannot be used now.");
            } else {
                ChatUtil.sendErrorMessage(player, "Fireworks use is already turned off.");
                return true;
            }
        }
        return true;
    }

    @Override
    public String getRequiredPermission() {
        return FancyFirework.ADMIN_PERMISSION;
    }
}

