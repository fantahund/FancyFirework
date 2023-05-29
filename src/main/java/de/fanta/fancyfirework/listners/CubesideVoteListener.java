package de.fanta.fancyfirework.listners;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.utils.ChatUtil;
import de.iani.playerUUIDCache.CachedPlayer;
import de.iani.playerUUIDCache.PlayerUUIDCache;
import de.iani.treasurechest.TreasureChest;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CubesideVoteListener implements Listener {
    private final FancyFirework plugin = FancyFirework.getPlugin();

    private static final long TWELVE_HOURS_IN_MILLIS = 1000L * 60 * 60 * 12;
    private final HashMap<UUID, HashMap<String, Long>> votes = new HashMap<>();

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerVote(VotifierEvent e) {
        PlayerUUIDCache playerUUIDCache = (PlayerUUIDCache) Bukkit.getPluginManager().getPlugin("PlayerUUIDCache");
        TreasureChest treasureChest = (TreasureChest) Bukkit.getPluginManager().getPlugin("TreasureChest");
        Vote vote = e.getVote();

        ItemStack customfirework = plugin.getRegistry().getRandomHalloweenFireWorkItem();
        if (!plugin.getConfig().getBoolean("votereward")) {
            return;
        }
        if (playerUUIDCache == null) {
            return;
        }
        if (treasureChest == null) {
            return;
        }
        CachedPlayer cp = playerUUIDCache.getPlayerFromNameOrUUID(vote.getUsername().trim());

        if (cp != null && customfirework != null) {
            HashMap<String, Long> lastVotes = votes.get(cp.getUniqueId());
            if (lastVotes == null) {
                lastVotes = new HashMap<>();
                votes.put(cp.getUniqueId(), lastVotes);
            }
            Long lastTime = lastVotes.get(vote.getServiceName());
            if (lastTime != null && lastTime + TWELVE_HOURS_IN_MILLIS > System.currentTimeMillis()) {
                return;
            }
            lastVotes.put(vote.getServiceName(), System.currentTimeMillis());

            ItemStack displayItem = new ItemStack(Material.FIREWORK_ROCKET, 1);
            ItemMeta displayItemMeta = displayItem.getItemMeta();
            displayItemMeta.setDisplayName(ChatUtil.GREEN + "FancyFirework");
            displayItem.setItemMeta(displayItemMeta);

            treasureChest.addItem(cp.getName(), displayItem, new ItemStack[] { customfirework.clone() }, 0);
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "tell " + cp.getName() + " " + FancyFirework.PREFIX + ChatUtil.GREEN + " Du hast " + customfirework.getItemMeta().getDisplayName() + ChatUtil.GREEN + " erhalten und kannst diese in der Schatztruhe am Spawn abholen!");
        }
    }
}
