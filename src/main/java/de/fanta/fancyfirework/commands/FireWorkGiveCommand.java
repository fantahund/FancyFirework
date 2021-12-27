package de.fanta.fancyfirework.commands;

import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.fireworks.AbstractFireWork;
import de.fanta.fancyfirework.utils.ChatUtil;
import de.iani.cubesideutils.bukkit.commands.SubCommand;
import de.iani.cubesideutils.commands.ArgsParser;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Steerable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class FireWorkGiveCommand extends SubCommand {

    private final FancyFirework plugin;

    public FireWorkGiveCommand(FancyFirework plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String s1, ArgsParser args) {
        if (sender instanceof Player p) {
            if (args.hasNext()) {
                String key = args.getNext();
                AbstractFireWork fireWork = plugin.getRegistry().get(NamespacedKey.fromString(key));
                if(fireWork != null) {
                    ItemStack itemStack = fireWork.getItemStack();
                    p.getInventory().addItem(itemStack); //TODO fertig machen nur test :>
                }
            }
        }
        return true;
    }

    @Override
    public ArrayList<String> onTabComplete(CommandSender sender, Command command, String alias, ArgsParser args) {
        int i = 0;
        while (args.hasNext()) {
            args.next();
            i++;
        }
        if (i == 1) {
            ArrayList<String> li = new ArrayList<>();
            plugin.getRegistry().getKeys().forEach((x) -> li.add(x.asString()));
            return li;
        }
        return new ArrayList<>();
    }
}
