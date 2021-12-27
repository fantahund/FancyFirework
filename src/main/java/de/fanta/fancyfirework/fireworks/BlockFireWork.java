package de.fanta.fancyfirework.fireworks;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class BlockFireWork extends AbstractFireWork {

    protected BlockFireWork(@NotNull NamespacedKey key) {
        super(key);
    }

    public abstract void onPlace(Block block, Player player);

    public abstract void onLit(Block block, Player player);

}
