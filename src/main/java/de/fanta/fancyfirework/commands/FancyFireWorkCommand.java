package de.fanta.fancyfirework.commands;

import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.utils.ChatUtil;
import de.iani.cubesideutils.bukkit.commands.SubCommand;
import de.iani.cubesideutils.commands.ArgsParser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class FancyFireWorkCommand extends SubCommand {
    private final FancyFirework plugin;

    public FancyFireWorkCommand(FancyFirework plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String commandString, ArgsParser args) {
        ChatUtil.sendNormalMessage(sender, "FancyFirework v" + plugin.getDescription().getVersion() + " by " + plugin.getDescription().getAuthors());
        return true;
    }
}
