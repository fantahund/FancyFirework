package de.fanta.fancyfirework.fireworks;

import de.fanta.fancyfirework.FancyFirework;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * This firework can be any kind of projectile (Egg, Snowball, etc.) and can be thrown by players.<br>
 * It does not support dispensers or other entities!
 *
 * There are a couple of methods that will be called on interactions or updates.
 * <ul>
 *      <li>{@link #onLaunch(Player, Projectile)} - Called when a player throws the firework projectile.</li>
 *      <li>{@link #onHit(Player, ProjectileHitEvent)} - Called when the firework projectile hits a block or entity.</li>
 * </ul>
 *
 */
public abstract class ItemFireWork extends AbstractFireWork {

    protected ItemFireWork(NamespacedKey key) {
        super(key);
    }

    /**
     * Called when a player throws the firework projectile.
     *
     * @param player The player that launched the projectile.
     * @param entity The entity of the launched projectile.
     */
    public abstract void onLaunch(Player player, Projectile entity);

    /**
     * Called when the firework projectile hits a block or entity.<br>
     * This method directly passes along the {@link ProjectileHitEvent} for further customisation.<br>
     * Note that the event won't be cancelled afterwards! If required it needs to be cancelled manually!
     *
     * @param player The player that launched the projectile.
     * @param event The event that was called.
     */
    public abstract void onHit(Player player, ProjectileHitEvent event);

    @Override
    protected Permission createPermission() {
        Permission permission = new Permission(this.getKey().getNamespace() + ".use." + this.getKey().getKey());
        permission.setDefault(PermissionDefault.TRUE);
        FancyFirework.getPlugin().registerPermission(permission);
        return permission;
    }

}
