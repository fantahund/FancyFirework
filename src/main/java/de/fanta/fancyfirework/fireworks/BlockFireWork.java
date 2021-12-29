package de.fanta.fancyfirework.fireworks;

import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.fireworks.defaults.FountainEffect;
import de.iani.cubesideutils.bukkit.items.ItemGroups;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Snow;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class BlockFireWork extends AbstractFireWork {

    private final Map<UUID, Task> activeTasks = new HashMap<>();

    protected BlockFireWork(@NotNull NamespacedKey key) {
        super(key);
    }

    public abstract void onPlace(Block block, ArmorStand stand, Player player);

    public abstract void onLit(ArmorStand stand, Player player);

    public abstract void onTick(Task task, boolean active);

    public ArmorStand spawnAtBlock(Block block) {
        World world = block.getWorld();
        double y = block.getY();
        Material type = block.getType();
        String typeName = type.name();
        double placeableY;
        if (typeName.endsWith("_SLAB")) {
            Slab slab = (Slab) block.getBlockData();
            placeableY = y - 0.44;
            if (slab.getType() == Slab.Type.BOTTOM) {
                placeableY -= 0.5;
            }
        } else if (typeName.endsWith("_STAIRS") || type.isOccluding() || type == Material.GLASS || ItemGroups.isStainedGlass(type) || type == Material.ICE || type == Material.PACKED_ICE || type == Material.BLUE_ICE || ItemGroups.isLeaves(type)) {
            placeableY = y - 0.44;
        } else if (ItemGroups.isCarpet(type)) {
            placeableY = y - 1.44 + (1 / 16.0);
        } else if (type == Material.SNOW) {
            Snow snow = (Snow) block.getBlockData();
            int layers = snow.getLayers();
            placeableY = y - 1.44 + (layers / 8.0);
        } else {
            placeableY = y - 0.44;
        }
        if (!Double.isNaN(placeableY)) {
            return world.spawn(new Location(world, block.getX() + 0.5, placeableY, block.getZ() + 0.5), ArmorStand.class, t -> {
                t.setVisible(false);
                t.setGravity(false);
                t.setHeadPose(new EulerAngle(0, random.nextDouble() * Math.PI * 2, 0));
                t.setHelmet(itemStack);
            });
        }
        return null;
    }

    public boolean hasActiveTask(ArmorStand stand) {
        return activeTasks.containsKey(stand.getUniqueId());
    }

    private void addTask(Task task) {
        UUID uuid = task.armorStand.getUniqueId();
        if (activeTasks.containsKey(uuid)) {
            activeTasks.get(uuid).stop();
        }
        activeTasks.put(uuid, task);
    }

    private void removeTask(Task task) {
        activeTasks.remove(task.armorStand.getUniqueId());
    }

    public class Task {

        protected final Player player;
        protected final ArmorStand armorStand;

        protected final long duration;
        protected final int delay;
        protected final int period;

        protected long tick;
        protected int counter;
        protected BukkitTask bukkitTask;

        protected final Runnable taskToRun;

        public Task(Player player, ArmorStand armorStand, long duration, int delay, int period, Runnable taskToRun) {
            this.player = player;
            this.armorStand = armorStand;
            addTask(this);
            this.duration = duration;
            this.delay = delay;
            this.period = period;
            this.taskToRun = taskToRun;

            this.tick = 0;
            this.bukkitTask = null;
        }

        protected void onTick() {
            boolean active = isActive();
            BlockFireWork.this.onTick(this, active);
            if (active) {
                if (counter >= period) {
                    //Spawn firework
                    taskToRun.run();
                    counter = 0;
                    //Change interval?!
                } else {
                    counter++;
                }
            }
        }

        public void start() {
            this.tick = 0;
            this.counter = 0;
            this.bukkitTask = Bukkit.getScheduler().runTaskTimer(FancyFirework.getPlugin(), () -> {
                if (tick < duration + delay) {
                    onTick();
                    tick++;
                } else {
                    stop();
                }
            }, 0, 1);
        }

        public void stop() {
            if(bukkitTask != null) {
                bukkitTask.cancel();
                bukkitTask = null;
                this.tick = 0;

                //Reset task & remove block + marker entity
                removeTask(this);
                armorStand.remove();
            }
        }

        public ArmorStand getArmorStand() {
            return armorStand;
        }

        public long getTick() {
            return tick;
        }

        public long getDuration() {
            return duration;
        }

        public int getDelay() {
            return delay;
        }

        public int getPeriod() {
            return period;
        }

        public Player getPlayer() {
            return player;
        }

        public boolean isActive() {
            return tick >= delay;
        }
    }

}
