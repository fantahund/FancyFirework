package de.fanta.fancyfirework.fireworks;

import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.FireWorksRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Marker;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFireWork implements Keyed {

    public static final NamespacedKey FIREWORK_ID = new NamespacedKey(FancyFirework.getPlugin(), "firework");

    private final NamespacedKey key;
    protected final ItemStack itemStack;
    private final List<Task> activeTasks = new ArrayList<>();

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

    private void addTask(Task task) {
        if (!activeTasks.contains(task)) {
            activeTasks.add(task);
        }
    }

    private void removeTask(Task task) {
        activeTasks.remove(task);
    }

    protected class Task {

        protected final Player player;
        protected final Block block;

        protected final long duration;
        protected final int delay;
        protected final int period;

        protected long tick;
        protected BukkitTask bukkitTask;
        protected final Runnable taskToRun;

        public Task(Player player, Block block, long duration, int delay, int period, Runnable taskToRun) {
            this.player = player;
            this.block = block;
            addTask(this);
            this.duration = duration;
            this.delay = delay;
            this.period = period;
            this.taskToRun = taskToRun;

            this.tick = 0;
            this.bukkitTask = null;
        }

        public void start() {
            this.tick = 0;
            this.bukkitTask = Bukkit.getScheduler().runTaskTimer(FancyFirework.getPlugin(), () -> {
                if (tick < duration) {
                    //Call custom task
                    taskToRun.run();
                    tick += period;
                } else {
                    stop();
                }
            }, delay, period);
        }

        public void stop() {
            if(bukkitTask != null) {
                bukkitTask.cancel();
                bukkitTask = null;
                this.tick = 0;

                //Reset task & remove block + marker entity
                removeTask(this);
                Collection<Marker> markers = block.getLocation().getNearbyEntitiesByType(Marker.class, 0.1d);
                Marker foundMarker = null;
                for (Marker marker : markers) {
                    for (MetadataValue metadataValue : marker.getMetadata(FireWorksRegistry.FIREWORK_META_KEY)) {
                        if (Objects.equals(metadataValue.getOwningPlugin(), FancyFirework.getPlugin())) {
                            foundMarker = marker;
                            break;
                        }
                    }
                }
                if (foundMarker != null) {
                    foundMarker.remove();
                }
                block.setType(Material.AIR);
            }
        }
    }
}
