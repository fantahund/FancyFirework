package de.fanta.fancyfirework.fireworks.defaults;

import com.google.common.util.concurrent.AtomicDouble;
import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.particle_effects.ISpawnParticle;
import de.fanta.fancyfirework.particle_effects.ParticleEffect;
import de.fanta.fancyfirework.particle_effects.ShapeBloon;
import de.fanta.fancyfirework.particle_effects.ShapeBloonRibbon;
import de.fanta.fancyfirework.utils.ColorUtils;
import de.fanta.fancyfirework.utils.CustomFireworkHeads;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.Vector;

public class FireWorkBatteryBirthday extends FireWorkBattery {

    private static final FancyFirework plugin = FancyFirework.getPlugin();

    public FireWorkBatteryBirthday() {
        super(new NamespacedKey(FancyFirework.getPlugin(), "battery_birthday"));
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = CustomFireworkHeads.getCustomTextureHead(UUID.fromString("f49adfe4-15dd-4672-8457-7963474b0c3d"), "Balloons", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTFiZTQ0ZTg0ZjAxMmY0M2ZhODExNzI3ZDJkNzQ2YTEwYjc1ZGQ5MjQzNzZkZDgwZmJjYjE3NzY4M2QzNTNjZSJ9fX0=");
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.of("#cffe4d") + "Birthday Battery");
        meta.setLore(FancyFirework.getPlugin().getConfig().getStringList("itemlore"));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    protected Permission createPermission() {
        Permission permission = new Permission("fancyfirework.use.battery_birthday");
        permission.setDefault(PermissionDefault.TRUE);
        plugin.registerPermission(permission);
        return permission;
    }

    @Override
    public void onExplode(Firework firework) {
        Location location = firework.getLocation();
        spawn(location);
        location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_BLAST_FAR, SoundCategory.AMBIENT, 8, 1);
        location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, SoundCategory.AMBIENT,8, 1);
    }

    @Override
    protected void spawnRandomFirework(Location location) {
        Random rand = ThreadLocalRandom.current();
        Firework firework = (Firework) location.getWorld().spawnEntity(location.add(0, 1.5, 0), EntityType.FIREWORK);
        firework.setVelocity(new Vector((rand.nextBoolean() ? 1 : -1) * rand.nextDouble(0.02), rand.nextDouble(0.5, 1.5), (rand.nextBoolean() ? 1 : -1) * rand.nextDouble(0.02)));
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        fireworkMeta.setPower(rand.nextInt(2) + 1);
        firework.setFireworkMeta(fireworkMeta);
        this.applyToEntity(firework);
    }

    @Override
    public Color randomColor() {
        java.awt.Color color = java.awt.Color.getHSBColor(random.nextFloat(0.83f, 1), random.nextFloat(0.8f, 1), random.nextFloat(0.8f, 1));
        return Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
    }

    public void spawn(Location origin) {
        Color color = ColorUtils.getColor(plugin.getTime(), 0.2);
        Vector rotation = new Vector(0, random.nextDouble(90), 0);
        ISpawnParticle spawnParticle = location -> location.getWorld().spawnParticle(Particle.REDSTONE, location, 2, 0.2, 0.2, 0.2, 0, new Particle.DustOptions(color, 2), true);
        ISpawnParticle spawnParticleWhite = location -> location.getWorld().spawnParticle(Particle.REDSTONE, location, 2, 0.2, 0.2, 0.2, 0, new Particle.DustOptions(Color.WHITE, 2), true);

        double maxSize = random.nextDouble(0.15, 0.5);
        AtomicDouble size = new AtomicDouble(0.05);
        AtomicInteger counter = new AtomicInteger();
        Bukkit.getScheduler().runTaskTimer(FancyFirework.getPlugin(), bukkitTask -> {
            if (counter.getAndIncrement() < 10) {
                double currentSize = size.get();
                boolean maxSizeReached = false;
                if (currentSize < maxSize) {
                    size.getAndAdd(0.07);
                } else {
                    maxSizeReached = true;
                }
                ParticleEffect effect = new ParticleEffect(origin, rotation, new ShapeBloon(currentSize), spawnParticle);
                effect.draw();
                if (maxSizeReached) {
                    effect = new ParticleEffect(origin, rotation, new ShapeBloonRibbon(currentSize), spawnParticleWhite);
                    effect.draw();
                }
            } else {
                bukkitTask.cancel();
            }
        }, 1, 1);
    }

}


