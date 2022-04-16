package de.fanta.fancyfirework.fireworks;

import de.fanta.fancyfirework.FancyFirework;
import org.antlr.v4.runtime.misc.NotNull;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.InputStream;
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
     * This key is used to identify the {@link AbstractFireWork}, that an ItemStack or Entity might contain.
     */
    public static final NamespacedKey FIREWORK_ID = new NamespacedKey(FancyFirework.getPlugin(), "firework");

    private final NamespacedKey key;

    protected AbstractFireWork(@NotNull NamespacedKey key) {
        this.key = Objects.requireNonNull(key, "Key must not be null!");
    }

    protected abstract ItemStack createItemStack();

    protected YamlConfiguration getConfig() {
        return FancyFirework.getPlugin().getConfigManager().getConfig(key);
    }

    public InputStream getDefaultResource() {
        return FancyFirework.getPlugin().getResource("fireworks/"+key.getKey()+".yml");
    }

    /**
     * Called when a minecraft firework, that includes the key of this Firework, explodes.
     *
     * @param firework The minecraft firework that exploded.
     */
    public void onExplode(Firework firework) { /* Called when a firework explodes */ }

    public void applyToEntity(Entity entity) {
        applyToPersistentDataContainer(entity.getPersistentDataContainer());
    }

    private void applyToPersistentDataContainer(PersistentDataContainer container) {
        container.set(FIREWORK_ID, PersistentDataType.STRING, getKey().toString());
    }

    private ItemStack initItemStack(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            this.applyToPersistentDataContainer(meta.getPersistentDataContainer());
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

    public ItemStack getItemStack() {
        return initItemStack(Objects.requireNonNull(createItemStack(), "ItemStack must not be null!"));
    }

    @NotNull
    @Override
    public final NamespacedKey getKey() {
        return key;
    }

}
