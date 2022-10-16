package de.fanta.fancyfirework.fireworks.defaults;

import com.google.common.util.concurrent.AtomicDouble;
import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.fireworks.BlockFireWork;
import de.fanta.fancyfirework.particle_effects.ISpawnParticle;
import de.fanta.fancyfirework.particle_effects.ParticleEffect;
import de.fanta.fancyfirework.particle_effects.Shape;
import de.fanta.fancyfirework.particle_effects.ShapeHeart;
import de.fanta.fancyfirework.utils.CustomFireworkHeads;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
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
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class FireWorkBatteryValentine extends FireWorkBattery {

    public FireWorkBatteryValentine() {
        super(new NamespacedKey(FancyFirework.getPlugin(), "battery_valentine"));
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = CustomFireworkHeads.getCustomTextureHead(UUID.fromString("eecce1f7-01ce-4e89-b98c-62417243bd08"), "Hearts", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmM4ZmI2MzdkNmUxYTdiYThmYTk3ZWU5ZDI5MTVlODQzZThlYzc5MGQ4YjdiZjYwNDhiZTYyMWVlNGQ1OWZiYSJ9fX0=");
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.of("#ff5cc3") + "Valentine Battery");
        meta.setLore(FancyFirework.getPlugin().getConfig().getStringList("itemlore"));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    protected Permission createPermission() {
        Permission permission = new Permission("fancyfirework.use.battery_valentine");
        permission.setDefault(PermissionDefault.TRUE);
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
        Color color = randomColor();
        Vector rotation = new Vector(0, random.nextDouble(90), 0);
        ISpawnParticle spawnParticle = location -> location.getWorld().spawnParticle(Particle.REDSTONE, location, 2, 0.2, 0.2, 0.2, 0, new Particle.DustOptions(color, 2), true);

        double maxSize = random.nextDouble(0.15, 0.5);
        AtomicDouble size = new AtomicDouble(0.05);
        AtomicInteger counter = new AtomicInteger();
        Bukkit.getScheduler().runTaskTimer(FancyFirework.getPlugin(), bukkitTask -> {
            if (counter.getAndIncrement() < 10) {
                double currentSize = size.get();
                if (currentSize < maxSize) {
                    size.getAndAdd(0.07);
                }
                ParticleEffect effect = new ParticleEffect(origin, rotation, new ShapeHeart(currentSize), spawnParticle);
                effect.draw();
            } else {
                bukkitTask.cancel();
            }
        }, 1, 1);
    }

}


