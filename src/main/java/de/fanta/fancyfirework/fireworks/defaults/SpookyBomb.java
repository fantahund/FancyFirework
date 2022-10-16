package de.fanta.fancyfirework.fireworks.defaults;

import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.fireworks.ItemFireWork;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class SpookyBomb extends ItemFireWork {

    private static final FancyFirework plugin = FancyFirework.getPlugin();
    private final ArrayList<UUID> spookyPlayers = new ArrayList<>();

    public SpookyBomb() {
        super(new NamespacedKey(FancyFirework.getPlugin(), "spooky_bomb"));
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack stack = new ItemStack(Material.EGG, 16);
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("" + ChatColor.of("#16705A") + ChatColor.BOLD + "Spooky Bomb");
            meta.setLore(FancyFirework.getPlugin().getConfig().getStringList("itemlorebangsnap"));
            meta.setCustomModelData(354461);
            stack.setItemMeta(meta);
        }
        return stack;
    }

    @Override
    protected Permission createPermission() {
        Permission permission = new Permission("fancyfirework.use.spooky_bomb");
        permission.setDefault(PermissionDefault.TRUE);
        plugin.registerPermission(permission);
        return permission;
    }

    @Override
    public void onLaunch(Player player, Projectile entity) {

    }

    @Override
    public void onHit(Player player, ProjectileHitEvent event) {
        Location loc = event.getEntity().getLocation();
        World world = loc.getWorld();

        world.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT, 2f, 1f);
        world.spawnParticle(Particle.REDSTONE, loc, 80, 1, 1.5, 1, 0.008, new Particle.DustOptions(Color.fromRGB(22, 112, 90), 2.5f), true);
        world.spawnParticle(Particle.REDSTONE, loc, 80, 1, 1.5, 1, 0.008, new Particle.DustOptions(Color.fromRGB(17, 51, 43), 2.5f), true);
        world.spawnParticle(Particle.REDSTONE, loc, 80, 1, 1.5, 1, 0.008, new Particle.DustOptions(Color.fromRGB(69, 69, 69), 2.5f), true);
        world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, loc, 70, 0.5, 1.5, 0.5, 0.005, null, true);

        for (Entity entity : world.getNearbyEntities(loc, 3, 3, 3)) {
            if (entity instanceof Player spookyPlayer) {
                if (spookyPlayers.contains(spookyPlayer.getUniqueId())) {
                    return;
                }
                if (spookyPlayer.isOnline()) {
                    spookyPlayers.add(spookyPlayer.getUniqueId());
                    spookyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 120, 0, true));
                    spookyPlayer.playSound(spookyPlayer, Sound.ENTITY_GHAST_HURT, 2, 1);
                    AtomicInteger tick = new AtomicInteger();
                    Bukkit.getScheduler().runTaskTimer(plugin, bukkitTask -> {
                        if (spookyPlayer.isOnline()) {
                            if (tick.get() < 5) {
                                tick.getAndIncrement();
                                spookyPlayer.playSound(spookyPlayer, Sound.ENTITY_WARDEN_HEARTBEAT, 1, 1);
                                spookyPlayer.damage(1);
                            } else {
                                spookyPlayers.remove(spookyPlayer.getUniqueId());
                                bukkitTask.cancel();
                            }
                        } else {
                            spookyPlayers.remove(spookyPlayer.getUniqueId());
                        }
                    }, 16, 16);
                }
            }
        }
    }
}
