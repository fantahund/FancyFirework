package de.fanta.fancyfirework.events;

import org.bukkit.damage.DamageSource;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class FireworkDeathEvent extends EntityDeathEvent implements Cancellable {

    private boolean cancelled;

    public FireworkDeathEvent(ArmorStand stand, List<ItemStack> drops, DamageSource damageSource) {
        super(stand, damageSource, drops);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
