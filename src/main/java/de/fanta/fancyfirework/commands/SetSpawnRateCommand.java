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
        if (args.hasNext()) {
            int spawnrate = args.getNext(1);
            if (spawnrate != 0) {
                plugin.getFireWorkWorks().setSpawnRate(spawnrate);
                ChatUtil.sendNormalMessage(sender, "Firework spawn rate is now " + spawnrate + ".");
            } else {
                ChatUtil.sendWarningMessage(sender, "Spawn rate must be greater than 0.");
            }
        } else {
            ChatUtil.sendWarningMessage(sender, "/fancyfirework setspawnrate [amount]");
        }
        return true;
    }

    @Override
    public String getRequiredPermission() {
        return FancyFirework.ADMIN_PERMISSION;
    }
}
