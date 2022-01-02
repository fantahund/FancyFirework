package de.fanta.fancyfirework.listners;

import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.PlayerInfo;
import de.fanta.fancyfirework.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashMap;

public class AFKListener implements Listener {

    public static int maxIdleTime = 10 * 1000;
    public static HashMap<Player, PlayerInfo> playerTimes;

    public AFKListener() {
        playerTimes = new HashMap<>();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent e) {
        handleJoin(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent e) {
        handleLogout(e.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onAsynchPlayerChatEvent(AsyncPlayerChatEvent e) {
        handleMoved(e.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent e) {
        handleMoved(e.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void PlayerPlayerDropItemEvent(PlayerDropItemEvent e) {
        handleMoved(e.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerEditBookEvent(PlayerEditBookEvent e) {
        handleMoved(e.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
        handleMoved(e.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent e) {
        handleMoved(e.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent e) {
        handleMoved(e.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent e) {
        handleMoved(e.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent e) {
        handleMoved(e.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryCloseEvent(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player player)) {
            return;
        }
        handleMoved(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryOpenEvent(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }
        handleMoved(player);
    }


    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent e) {
        Location from = e.getFrom();
        Location to = e.getTo();
        if (to == null) {
            return;
        }
        if (from.getYaw() != to.getYaw() || from.getPitch() != to.getPitch()) {
            handleMoved(e.getPlayer());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSneak(PlayerToggleSneakEvent e) {
        if (e.isSneaking()) {
            handleMoved(e.getPlayer());
        }
    }

    private void handleMoved(Player p) {
        PlayerInfo playerInfo = playerTimes.get(p);
        if (playerInfo != null) {
            playerInfo.last_move_time = System.currentTimeMillis();
            if (playerInfo.afk) {
                playerInfo.afk = false;
                playerInfo.last_check_time = playerInfo.last_move_time;
            }
        }
    }


    public static void handleJoin(final Player p) {
        PlayerInfo playerInfo = new PlayerInfo();
        playerTimes.put(p, playerInfo);
    }

    public static void onTimer() {
        long t = System.currentTimeMillis();
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            handleTimer(p, t);
        }
    }

    private static void handleTimer(Player p, long time) {
        PlayerInfo playerInfo = playerTimes.get(p);
        if (playerInfo != null && !playerInfo.afk) {
            long addTime;
            if (time < playerInfo.last_move_time + maxIdleTime) {
                addTime = time - playerInfo.last_check_time;
            } else {
                addTime = (playerInfo.last_move_time + maxIdleTime) - playerInfo.last_check_time;
                playerInfo.afk = true;
            }
            playerInfo.accounted_time += Math.max(addTime, 0);
            playerInfo.last_check_time = time;
        }
    }

    private void handleLogout(Player p) {
        playerTimes.remove(p);
    }
}
