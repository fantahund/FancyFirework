package de.fanta.fancyfirework.fireworks.defaults;

import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.utils.CustomFireworkHeads;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
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

public class FireWorkFountainValentine extends FireWorkFountain {

    public FireWorkFountainValentine() {
        super(new NamespacedKey(FancyFirework.getPlugin(), "fountain_valentine"));
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = CustomFireworkHeads.getCustomTextureHead(UUID.fromString("d844b56b-a11a-4d0b-8710-aea25178d1e"), "Heart Block", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGIyNTA1MjcwOWYyYjVlNTFmODcyYzYwNTdjMjc2NmNjNTMyMDZjMTIzNGI3MWVjYTMzOWIzM2Y1YSJ9fX0=");
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.of("#ff5cc3") + "Valentine Fountain");
        meta.setLore(FancyFirework.getPlugin().getConfig().getStringList("itemlore"));
        itemStack.setItemMeta(meta);
        return itemStack;
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
                effect.setSpawnParticle(location -> location.getWorld().spawnParticle(Particle.HEART, location, 1));

                return List.of(effect);
            });
            return fountain;
        });
        batteryTask.start();
    }

    @Override
    public void onTick(Task task, boolean active) {
        Location loc = task.getArmorStand().getLocation().add(0, 1.5, 0);
        loc.getWorld().spawnParticle(Particle.HEART, loc, 1, 0.1, 0.1, 0.1, 0.025);
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
        fireworkMeta.setPower(1);
        firework.setFireworkMeta(fireworkMeta);
    }

    @Override
    public Color randomColor() {
        java.awt.Color color = java.awt.Color.getHSBColor(0.955f, random.nextFloat(0.3f, 1), random.nextFloat(0.3f, 1));
        return Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
    }
}
