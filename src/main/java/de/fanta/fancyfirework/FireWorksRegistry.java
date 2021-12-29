package de.fanta.fancyfirework;

import com.google.common.base.Preconditions;
import de.fanta.fancyfirework.fireworks.AbstractFireWork;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FireWorksRegistry {

    public static final String FIREWORK_META_KEY = "firework_type";

    private final Map<NamespacedKey, AbstractFireWork> fireWorkMap;

    private final FancyFirework plugin;

    FireWorksRegistry(FancyFirework plugin) {
        this.fireWorkMap = new HashMap<>();
        this.plugin = plugin;
    }

    public void register(@NotNull AbstractFireWork fireWork) {
        Preconditions.checkArgument(fireWork != null, "Firework cannot be null!");
        NamespacedKey key = fireWork.getKey();
        Preconditions.checkArgument(!fireWorkMap.containsKey(key), "A firework with the key " + key.toString() + " has already been registered!");
        fireWorkMap.put(key, fireWork);
    }

    @Nullable
    public AbstractFireWork get(NamespacedKey key) {
        return fireWorkMap.get(key);
    }

    @Nullable
    public AbstractFireWork getByItemStack(ItemStack itemStack) {
        if (itemStack != null && !itemStack.getType().isAir()) {
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                String keyString = meta.getPersistentDataContainer().get(AbstractFireWork.FIREWORK_ID, PersistentDataType.STRING);
                if (keyString != null) {
                    return get(NamespacedKey.fromString(keyString));
                }
            }
        }
        return null;
    }

    public AbstractFireWork getByEntity(@NotNull Entity armorStand) {
        String keyString = armorStand.getPersistentDataContainer().get(AbstractFireWork.FIREWORK_ID, PersistentDataType.STRING);
        if (keyString != null) {
            return get(NamespacedKey.fromString(keyString));
        }
        return null;
    }

    public List<NamespacedKey> getKeys() {
        return List.copyOf(fireWorkMap.keySet());
    }

    public ItemStack getRandomFireWorkItem() {
        Random rand = new Random();
        NamespacedKey randomkey = getKeys().get(rand.nextInt(getKeys().size()));
        AbstractFireWork fireWork = get(randomkey);
        assert fireWork != null;
        return fireWork.getItemStack().clone();
    }

}
