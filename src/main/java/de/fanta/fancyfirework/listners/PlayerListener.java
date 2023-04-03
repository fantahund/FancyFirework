package de.fanta.fancyfirework.listners;

import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.FireWorkWorks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    private final FancyFirework plugin = FancyFirework.getPlugin();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        FireWorkWorks fireWorkWorks = plugin.getFireWorkWorks();
        if (fireWorkWorks.enabled()) {
            fireWorkWorks.startTaskFor(e.getPlayer());
        }
    }
}
