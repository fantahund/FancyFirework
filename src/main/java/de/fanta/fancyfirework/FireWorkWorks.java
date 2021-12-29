package de.fanta.fancyfirework;

import de.fanta.fancyfirework.utils.ChatUtil;
import de.iani.cubesideutils.StringUtil;
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
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.logging.Level;

public class FireWorkWorks {
    private final Plugin plugin = FancyFirework.getPlugin();
    private boolean enabled;
    private int taskID;
    private Random rand;
    private long lastspawn;

    public FireWorkWorks() {
        enabled = plugin.getConfig().getBoolean("enabled");
        if (enabled) {
            enableTask();
        }

    }

    public void enableTask() {
        rand = new Random();
        taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this::randomFireworkRun, 1L, 1L);
        plugin.getLogger().log(Level.INFO, "Started spawning random firework.");
    }

    public void disableTask() {
        plugin.getServer().getScheduler().cancelTask(taskID);
        plugin.getLogger().log(Level.INFO, "Stopped spawning random firework.");
    }

    public void randomFireworkRun() {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (rand.nextInt(getSpawnRate()) == 0) {
                spawnFirework(p);
            }
        }
    }

    public void spawnFirework(Player p) {
        World world = p.getWorld();
        Location loc = p.getLocation().add(0, 2, 0);
        Location toppos = world.getHighestBlockAt(loc.getBlockX(), loc.getBlockZ(), HeightMap.MOTION_BLOCKING).getLocation();
        boolean sky = loc.getBlockY() > toppos.getBlockY();
        if (sky && p.getGameMode() == GameMode.SURVIVAL && !FancyFirework.getPlugin().isVanish(p)) {
            Firework firework = (Firework) world.spawnEntity(loc, EntityType.FIREWORK);
            firework.setVelocity(new Vector((rand.nextBoolean() ? 1 : -1) * rand.nextDouble(0.01), rand.nextDouble(0.5, 1.5), (rand.nextBoolean() ? 1 : -1) * rand.nextDouble(0.01)));
            FireworkMeta fireworkMeta = firework.getFireworkMeta();
            FireworkEffect effect = FireworkEffect.builder().with(FireworkEffect.Type.values()[rand.nextInt(FireworkEffect.Type.values().length)]).withColor(randomColor()).withFade(randomColor()).withFlicker().withTrail().build();
            fireworkMeta.addEffect(effect);
            fireworkMeta.setPower(rand.nextInt(2) + 1);
            firework.setFireworkMeta(fireworkMeta);
            firework.setRotation(rand.nextInt(20), rand.nextInt(20));
            firework.setMetadata("FancyFirework", new FixedMetadataValue(plugin, "CustomFirework"));
        }
    }

    public boolean enabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (enabled != this.enabled)
            if (enabled) {
                enableTask();
            } else {
                disableTask();
            }

        this.enabled = enabled;
        plugin.getConfig().set("enabled", enabled);
        plugin.saveConfig();
    }

    public int getSpawnRate() {
        return plugin.getConfig().getInt("spawnrate", 1);
    }

    public void setSpawnRate(double spawnRate) {
        plugin.getConfig().set("spawnrate", spawnRate);
        plugin.saveConfig();
    }

    public Color randomColor() {
        int red = (int) (Math.random() * 256);
        int green = (int) (Math.random() * 256);
        int blue = (int) (Math.random() * 256);
        return Color.fromRGB(red, green, blue);
    }
}

