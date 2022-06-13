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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class FireWorkGiveCommand extends SubCommand {

    private final FancyFirework plugin;

    public FireWorkGiveCommand(FancyFirework plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String s1, ArgsParser args) {
        if (!(sender instanceof Player p)) { //TODO Use Utils Check hasPlayer
            ChatUtil.sendErrorMessage(sender, "You are not a Player :>");
            return true;
        }
        if (!p.hasPermission(FancyFirework.MOD_PERMISSION)) {
            ChatUtil.sendErrorMessage(p, "No Permission!"); //TODO Use Utils Check hasPermission
            return true;
        }

        if (args.remaining() != 1 && args.remaining() != 2) {
            ChatUtil.sendWarningMessage(p, "/fancyfirework give [key] <amount>");
            return true;
        }

        if (args.hasNext()) {
            String key = args.getNext();
            AbstractFireWork fireWork = plugin.getRegistry().get(NamespacedKey.fromString(key));
            int amount = 1;
            if (args.hasNext()) {
                int next = args.getNext(1);
                if (next < 1 || next > 64) {
                    ChatUtil.sendErrorMessage(p, "Quantity must be between 1-64.");
                    return true;
                }
                amount = next;
            }
            if (fireWork != null) {
                Inventory inv = p.getInventory();
                ItemStack itemStack = fireWork.getItemStack();
                itemStack.setAmount(amount);
                if (inv.firstEmpty() == -1) {
                    ChatUtil.sendErrorMessage(p, "Item could not be added. Your inventory is full!");
                } else {
                    p.getInventory().addItem(itemStack);
                    ChatUtil.sendNormalMessage(p, amount + "x " + itemStack.getItemMeta().getDisplayName() + ChatUtil.GREEN + " has been added to your inventory");
                }
            } else {
                ChatUtil.sendErrorMessage(p, "Firework " + key + " not available.");
            }
        }
        return true;
    }

    @Override
    public ArrayList<String> onTabComplete(CommandSender sender, Command command, String alias, ArgsParser args) {
        if (sender.hasPermission(FancyFirework.MOD_PERMISSION)) {
            int i = args.remaining();
            if (i == 1) {
                ArrayList<String> li = new ArrayList<>();
                plugin.getRegistry().getKeys().forEach((x) -> li.add(x.toString()));
                return li;
            }
            return new ArrayList<>();
        }
        return null;
    }
}
