package de.fanta.fancyfirework;

import com.google.common.base.Preconditions;
import de.fanta.fancyfirework.fireworks.AbstractFireWork;
import de.fanta.fancyfirework.fireworks.BlockFireWork;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Marker;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        Preconditions.checkArgument(!fireWorkMap.containsKey(key), "A firework with the key "+key.toString()+" has already been registered!");
        fireWorkMap.put(key, fireWork);
    }

    @Nullable
    public AbstractFireWork get(NamespacedKey key) {
        return fireWorkMap.get(key);
    }

    @Nullable
    public AbstractFireWork getByItemStack(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if(meta != null) {
            String keyString = meta.getPersistentDataContainer().get(AbstractFireWork.FIREWORK_ID, PersistentDataType.STRING);
            if(keyString != null) {
                return get(NamespacedKey.fromString(keyString));
            }
        }
        return null;
    }

    public AbstractFireWork getAtBlock(Block block) {
        Collection<Marker> markers = block.getLocation().getNearbyEntitiesByType(Marker.class, 0d);
        for (Marker marker : markers) {
            for (MetadataValue metadataValue : marker.getMetadata(FIREWORK_META_KEY)) {
                if (Objects.equals(metadataValue.getOwningPlugin(), plugin)) {
                    NamespacedKey key = NamespacedKey.fromString(metadataValue.asString());
                    return get(key);
                }
            }
        }
        return null;
    }

    public List<NamespacedKey> getKeys() {
        return List.copyOf(fireWorkMap.keySet());
    }

}
