package de.fanta.fancyfirework.fireworks.defaults;

import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.utils.ColorUtils;
import de.iani.cubesideutils.bukkit.items.CustomHeads;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public abstract class FireWorkFountain extends FireWorkBattery {

    private final FancyFirework plugin = FancyFirework.getPlugin();

    public FireWorkFountain(NamespacedKey namespacedKey) {
        super(namespacedKey);
    }

    @Override
    public void onLit(ArmorStand stand, Player player) {
        stand.getWorld().playSound(stand.getLocation(), Sound.ENTITY_CREEPER_PRIMED, SoundCategory.AMBIENT, 1f, 1f);
        BatteryTask batteryTask = new BatteryTask(player, stand, 20 * 40, 20 * 5, 20, 1);
        batteryTask.setSpawnFireworkTask(task -> {
            stand.getWorld().playSound(stand.getLocation(), Sound.ENTITY_GHAST_SCREAM, SoundCategory.AMBIENT, 2f, 1.5f);
            spawnRandomFirework(task.getArmorStand().getLocation());
        });
        batteryTask.setSpawnFountainTask(task -> {
            //Create fountain
            Fountain fountain = new Fountain(20 * 60, random.nextInt(2, 5));
            fountain.setCreateEffects(() -> {
                //Create next fountain effect/s
                var percentage = random.nextDouble();
                if (percentage > 0.7) {
                    stand.getWorld().playSound(stand.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, SoundCategory.AMBIENT, 1.5f, 0.6f);
                }

                FountainEffect effect = new FountainEffect(random.nextInt(6, 20), random.nextDouble(0.4, 1), random.nextDouble(359), random.nextDouble(6));
                effect.setSpawnParticle(location -> location.getWorld().spawnParticle(Particle.REDSTONE, location, 6, new Particle.DustOptions(randomColor(), 1.5f)));

                return List.of(effect);
            });
            return fountain;
        });
        batteryTask.start();
    }

    @Override
    public void onTick(Task task, boolean active) {
        Location loc = task.getArmorStand().getLocation().add(0, 1.5, 0);
        loc.getWorld().spawnParticle(Particle.FLAME, loc, 1, 0, 0, 0, 0.025);
        if (task instanceof BatteryTask batteryTask) {
            if (batteryTask.getFireworkCounter() == 0) {
                batteryTask.setFireworkCooldown(random.nextInt(20 * 4, 20 * 8));
            }
        }
    }

    @Override
    protected void spawnRandomFirework(Location location) {
        Random rand = ThreadLocalRandom.current();
        Firework firework = (Firework) location.getWorld().spawnEntity(location.add(0, 1.5, 0), EntityType.FIREWORK);
        firework.setVelocity(new Vector((rand.nextBoolean() ? 1 : -1) * rand.nextDouble(0.02), rand.nextDouble(0.3, 0.5), (rand.nextBoolean() ? 1 : -1) * rand.nextDouble(0.02)));
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        FireworkEffect effect = FireworkEffect.builder().with(FireworkEffect.Type.values()[rand.nextInt(FireworkEffect.Type.values().length)]).withColor(randomColor()).withFade(randomColor()).withFlicker().withTrail().build();
        fireworkMeta.addEffect(effect);
        fireworkMeta.setPower(1);
        firework.setFireworkMeta(fireworkMeta);
    }
}



