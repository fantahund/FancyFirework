package de.fanta.fancyfirework.commands;

import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.fireworks.AbstractFireWork;
import de.fanta.fancyfirework.utils.ChatUtil;
import de.iani.cubesideutils.bukkit.commands.SubCommand;
import de.iani.cubesideutils.commands.ArgsParser;
import org.bukkit.Bukkit;
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
        if (!(sender instanceof Player p)) {
            ChatUtil.sendErrorMessage(sender, "You are not a Player :>");
            return true;
        }

        /*if (args.remaining() != 1 && args.remaining() != 2) {
            ChatUtil.sendWarningMessage(p, "/fancyfirework give [key] <amount>");
            return true;
        }*/

        Player givePlayer = p;
        if (args.hasNext()) {
            String key = args.getNext();
            AbstractFireWork fireWork = plugin.getRegistry().get(NamespacedKey.fromString(key));
            int amount;
            if (args.hasNext()) {
                int next = args.getNext(1);
                if (next < 1 || next > 64) {
                    ChatUtil.sendErrorMessage(p, "Quantity must be between 1-64.");
                    return true;
                }
                amount = next;

                if (args.hasNext()) {
                    if (!p.hasPermission(FancyFirework.ADMIN_PERMISSION)) {
                        ChatUtil.sendErrorMessage(p, "You do not have the permission to give fireworks to other players.");
                        return true;
                    }
                    String giveName = args.getNext();
                    givePlayer = Bukkit.getPlayer(giveName);

                    if (givePlayer == null) {
                        ChatUtil.sendErrorMessage(p, "Player " + giveName + " is not online.");
                        return true;
                    }
                }
            } else {
                ChatUtil.sendWarningMessage(p, "/fancyfirework give [key] <amount> <PlayerName>");
                return true;
            }
            if (fireWork != null) {
                Inventory inv = givePlayer.getInventory();
                ItemStack itemStack = fireWork.getItemStack();
                itemStack.setAmount(amount);
                if (inv.firstEmpty() == -1) {
                    ChatUtil.sendErrorMessage(p, "Item could not be added. Your inventory is full!");
                } else {
                    givePlayer.getInventory().addItem(itemStack);
                    ChatUtil.sendNormalMessage(givePlayer, amount + "x " + itemStack.getItemMeta().getDisplayName() + ChatUtil.GREEN + " has been added to your inventory");
                    if (!p.getUniqueId().equals(givePlayer.getUniqueId())) {
                        ChatUtil.sendNormalMessage(p, amount + "x " + itemStack.getItemMeta().getDisplayName() + ChatUtil.GREEN + " has been added to " + givePlayer.getName() + "'s inventory");
                    }
                }
            } else {
                ChatUtil.sendErrorMessage(p, "Firework " + key + " not available.");
            }
        } else {
            ChatUtil.sendWarningMessage(p, "/fancyfirework give [key] <amount> <PlayerName>");
            return true;
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
            if (sender.hasPermission(FancyFirework.ADMIN_PERMISSION)) {
                if (i == 3) {
                    ArrayList<String> li = new ArrayList<>();
                    Bukkit.getOnlinePlayers().forEach((player) -> li.add(player.getName()));
                    return li;
                }
            }
            return new ArrayList<>();
        }
        return null;
    }

    @Override
    public String getRequiredPermission() {
        return FancyFirework.MOD_PERMISSION;
    }
}
