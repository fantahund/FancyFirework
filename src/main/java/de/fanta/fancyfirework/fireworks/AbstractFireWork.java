package de.fanta.fancyfirework.fireworks;

import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.FireWorksRegistry;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Marker;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class AbstractFireWork implements Keyed {

    protected static final Random random = new Random();
    protected static final String FIREWORK_META_KEY = "firework_type";
    public static final NamespacedKey FIREWORK_ID = new NamespacedKey(FancyFirework.getPlugin(), "firework");

    private final NamespacedKey key;
    protected final ItemStack itemStack;

    protected AbstractFireWork(@NotNull NamespacedKey key) {
        this.key = Objects.requireNonNull(key, "Key must not be null!");
        this.itemStack = initItemStack(Objects.requireNonNull(createItemStack(), "ItemStack must not be null!"));
    }

    protected abstract ItemStack createItemStack();

    private ItemStack initItemStack(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(FIREWORK_ID, PersistentDataType.STRING, key.asString());
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack.clone();
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

}
