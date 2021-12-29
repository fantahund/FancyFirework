package de.fanta.fancyfirework.fireworks;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jetbrains.annotations.NotNull;

public abstract class ItemFireWork extends AbstractFireWork {

    protected ItemFireWork(@NotNull NamespacedKey key) {
        super(key);
    }

    public abstract void onLaunch(Player player, Projectile entity);

    public abstract void onHit(Player player, ProjectileHitEvent event);

}
