package de.fanta.fancyfirework.listners;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.utils.ChatUtil;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class VoteListener implements Listener {
    private final FancyFirework plugin = FancyFirework.getPlugin();

    private static final long TWELVE_HOURS_IN_MILLIS = 1000L * 60 * 60 * 12;
    private final HashMap<UUID, HashMap<String, Long>> votes = new HashMap<>();

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerVote(VotifierEvent e) {
        Vote vote = e.getVote();
        Player p = Bukkit.getPlayer(vote.getUsername());
        ItemStack customfirework = plugin.getRegistry().getRandomFireWorkItem();
        if (!plugin.getConfig().getBoolean("votereward")) {
            return;
        }
        if (p != null) {
            HashMap<String, Long> lastVotes = votes.get(p.getUniqueId());
            if (lastVotes == null) {
                lastVotes = new HashMap<>();
                votes.put(p.getUniqueId(), lastVotes);
            }
            Long lastTime = lastVotes.get(vote.getServiceName());
            if (lastTime != null && lastTime + TWELVE_HOURS_IN_MILLIS > System.currentTimeMillis()) {
                return;
            }
            lastVotes.put(vote.getServiceName(), System.currentTimeMillis());

            if (p.getInventory().firstEmpty() == -1) {
                ChatUtil.sendErrorMessage(p, "Your inventory is full " + customfirework.getItemMeta().getDisplayName() + ChatUtil.GREEN + " could not be added to your inventory.");
            } else {
                p.getInventory().addItem(customfirework);
                ChatUtil.sendNormalMessage(p, "You got " + customfirework.getItemMeta().getDisplayName() + ChatUtil.GREEN + " by voting");
            }
        }
    }
}
