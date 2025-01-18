package de.fanta.fancyfirework.commands;

import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.utils.ChatUtil;
import de.iani.cubesideutils.bukkit.commands.PlayerSubCommand;
import de.iani.cubesideutils.commands.ArgsParser;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class EnableToggleCommand extends PlayerSubCommand {

    private final FancyFirework plugin;
    private final boolean enable;

    public EnableToggleCommand(FancyFirework plugin, boolean enable) {
        this.plugin = plugin;
        this.enable = enable;
    }

    @Override
    public boolean onCommand(Player player, Command command, String alias, String commandString, ArgsParser args) {
        if (enable) {
            if (!plugin.getFireWorkWorks().enabled()) {
                plugin.getFireWorkWorks().setEnabled(true);
                ChatUtil.sendNormalMessage(player, "Fireworks spawning was turned on.");
            } else {
                ChatUtil.sendErrorMessage(player, "Fireworks spawning is already turned on.");
                return true;
            }
        } else {
            if (plugin.getFireWorkWorks().enabled()) {
                plugin.getFireWorkWorks().setEnabled(false);
                ChatUtil.sendNormalMessage(player, "Fireworks spawning was turned off.");
            } else {
                ChatUtil.sendErrorMessage(player, "Fireworks spawning is already turned off.");
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

