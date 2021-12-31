package de.fanta.fancyfirework.fireworks;

import de.fanta.fancyfirework.FancyFirework;
import org.antlr.v4.runtime.misc.NotNull;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;
import java.util.Random;

/**
 * The base firework class that contains all necessary methods to create a firework.<br>
 * For a pre-defined implementation use:
 * <ul>
 *     <li>{@link BlockFireWork} - A firework that can be placed in the world.</li>
 *     <li>{@link ItemFireWork} - A firework that uses a projectile thrown by a player.</li>
 * </ul>
 *
 */
public abstract class AbstractFireWork implements Keyed {

    protected static final Random random = new Random();
    /**
     * This key is used to identify the {@link AbstractFireWork} an ItemStack, Entity might contain.
     */
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
            meta.getPersistentDataContainer().set(FIREWORK_ID, PersistentDataType.STRING, key.toString());
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack.clone();
    }

    @NotNull
    @Override
    public final NamespacedKey getKey() {
        return key;
    }

}
