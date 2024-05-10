package de.fanta.fancyfirework.fireworks.defaults;

import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.fireworks.ItemFireWork;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class SmokeBomb extends ItemFireWork {

    private static final FancyFirework plugin = FancyFirework.getPlugin();

    public SmokeBomb() {
        super(new NamespacedKey(FancyFirework.getPlugin(), "smoke_bomb"));
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack stack = new ItemStack(Material.EGG, 16);
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("" + ChatColor.of("#636363") + ChatColor.BOLD + "Smoke Bomb");
            meta.setLore(FancyFirework.getPlugin().getConfig().getStringList("itemlorebangsnap"));
            meta.setCustomModelData(654654132);
            stack.setItemMeta(meta);
        }
        return stack;
    }

    @Override
    public void onLaunch(Player player, Projectile entity) {

    }

    @Override
    public void onHit(Player player, ProjectileHitEvent event) {
        Location loc = event.getEntity().getLocation();
        World world = loc.getWorld();
        world.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT, 2f, 1f);
        world.spawnParticle(Particle.LARGE_SMOKE, loc, 250, 2, 2, 2, 0.015, null, true);
        world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, loc, 400, 2, 2, 2, 0.015, null, true);
        world.spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, loc, 40, 2, 2, 2, 0.015, null, true);

    }
}
