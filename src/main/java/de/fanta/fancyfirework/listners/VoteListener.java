package de.fanta.fancyfirework.listners;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class VoteListener implements Listener {
    private final FancyFirework plugin = FancyFirework.getPlugin();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerVote(VotifierEvent e) {
        Vote vote = e.getVote();
        Player p = Bukkit.getPlayer(vote.getUsername());
        ItemStack customfirework = plugin.getRegistry().getRandomFireWork();
        if (!plugin.getConfig().getBoolean("votereward")) {
            return;
        }
        if (p != null) {
            if (p.getInventory().firstEmpty() == -1) {
                ChatUtil.sendErrorMessage(p, "Your inventory is full " + customfirework.getItemMeta().getDisplayName() + ChatUtil.GREEN + " could not be added to your inventory.");
            } else {
                p.getInventory().addItem(customfirework);
                ChatUtil.sendNormalMessage(p, "You got " + customfirework.getItemMeta().getDisplayName() + ChatUtil.GREEN + " by voting");
            }
        }
    }
}
