package de.fanta.fancyfirework.fireworks;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class ItemFireWork extends AbstractFireWork {

    protected ItemFireWork(@NotNull NamespacedKey key) {
        super(key);
    }

    public abstract void execute(Player player);
}
