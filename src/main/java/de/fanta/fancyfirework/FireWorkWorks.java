package de.fanta.fancyfirework;

import de.fanta.fancyfirework.fireworks.AbstractFireWork;
import de.fanta.fancyfirework.listners.AFKListener;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.logging.Level;

public class FireWorkWorks {
    private final FancyFirework plugin = FancyFirework.getPlugin();
    private boolean enabled;
    private final Random rand;

    public FireWorkWorks() {
        this.enabled = plugin.getConfig().getBoolean("enabled");
        this.rand = new Random();
        if (enabled) {
            enableTask();
        }
    }

    public void enableTask() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            startTaskFor(player);
        }
        plugin.getLogger().log(Level.INFO, "Started spawning random firework.");
    }

    public void disableTask() {
        plugin.getLogger().log(Level.INFO, "Stopped spawning random firework.");
    }

    public void startTaskFor(Player player) {
        if (!enabled) return;
        plugin.getScheduler().runOnEntityAtFixedRate(player, task -> {
            if (player == null || !player.isOnline() || !enabled) {
                task.cancel();
                return;
            }
            if (rand.nextInt(getSpawnRate()) == 0) spawnFirework(player);
        }, 1L, 1L);
    }

    public void spawnFirework(Player p) {
        World world = p.getWorld();
        Location loc = p.getLocation().add(0, 2, 0);
        Location toppos = world.getHighestBlockAt(loc.getBlockX(), loc.getBlockZ(), HeightMap.MOTION_BLOCKING).getLocation();
        boolean sky = loc.getBlockY() > toppos.getBlockY();
        PlayerInfo playerInfo = AFKListener.playerTimes.get(p);
        if (sky && (p.getGameMode() == GameMode.SURVIVAL || p.getGameMode() == GameMode.ADVENTURE) && !FancyFirework.getPlugin().isVanish(p) && !playerInfo.afk) {
            Firework firework = (Firework) world.spawnEntity(loc, EntityType.FIREWORK_ROCKET);
            firework.setVelocity(new Vector((rand.nextBoolean() ? 1 : -1) * rand.nextDouble(0.01), rand.nextDouble(0.5, 1.5), (rand.nextBoolean() ? 1 : -1) * rand.nextDouble(0.01)));
            FireworkMeta fireworkMeta = firework.getFireworkMeta();
            FireworkEffect effect = FireworkEffect.builder().with(FireworkEffect.Type.values()[rand.nextInt(FireworkEffect.Type.values().length)]).withColor(randomColor()).withFade(randomColor()).withFlicker().withTrail().build();
            fireworkMeta.addEffect(effect);
            fireworkMeta.setPower(rand.nextInt(2) + 1);
            firework.setFireworkMeta(fireworkMeta);
            firework.setRotation(rand.nextInt(20), rand.nextInt(20));
            firework.setCustomName("FancyFirework");
        }
    }

    public boolean enabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (enabled != this.enabled) {
            this.enabled = enabled;
            if (enabled) {
                enableTask();
            } else {
                disableTask();
            }
        }
        plugin.getConfig().set("enabled", enabled);
        plugin.saveConfig();
    }

    public int getSpawnRate() {
        return plugin.getConfig().getInt("spawnrate", 100);
    }

    public void setSpawnRate(int spawnRate) {
        plugin.getConfig().set("spawnrate", spawnRate);
        plugin.saveConfig();
    }

    public Color randomColor() {
        int red = (int) (Math.random() * 256);
        int green = (int) (Math.random() * 256);
        int blue = (int) (Math.random() * 256);
        return Color.fromRGB(red, green, blue);
    }

    public ItemStack fixFirework(ItemStack item) {
        if (item == null) {
            return item;
        }
        AbstractFireWork fireWork = plugin.getRegistry().getByItemStack(item);
        if (fireWork != null) {
            ItemStack fireWorkItem = fireWork.getItemStack();
            fireWorkItem.setAmount(item.getAmount());
            return fireWorkItem;
        }
       return item;
    }
}

