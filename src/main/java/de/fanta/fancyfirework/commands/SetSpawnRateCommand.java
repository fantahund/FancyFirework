package de.fanta.fancyfirework.commands;

import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.utils.ChatUtil;
import de.iani.cubesideutils.bukkit.commands.SubCommand;
import de.iani.cubesideutils.commands.ArgsParser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SetSpawnRateCommand extends SubCommand {
    private final FancyFirework plugin;

    public SetSpawnRateCommand(FancyFirework plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String s1, ArgsParser args) {
        if (!sender.hasPermission(FancyFirework.ADMIN_PERMISSION)) {
            ChatUtil.sendErrorMessage(sender, "No Permission!");
            return true;
        }
        if (args.hasNext()) {
            double spawnrate = args.getNext(0.0);
            if (spawnrate >= 0.001 && spawnrate < 101) {
                plugin.getFireWorkWorks().setSpawnRate(spawnrate);
                ChatUtil.sendNormalMessage(sender, "Firework spawn rate is now " + spawnrate + ".");
            } else {
                ChatUtil.sendErrorMessage(sender, "Spawn rate must be higher than 0.001 and lower than 100.");
            }
        } else {
            ChatUtil.sendWarningMessage(sender, "/fancyfirework setspawnrate [amount]");
        }
        return true;
    }
}
