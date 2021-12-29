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
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * This firework can be placed into the world and lit by players using flint and steel.<br>
 * If placed on a block it will spawn an ArmorStand with a persistent tag to mark it as a firework. <br>
 * The ArmorStand will then be equipped with a helmet that is the {@link org.bukkit.inventory.ItemStack} from the firework (See {@link #createItemStack()}).<br>
 * <br>
 * There are a couple of methods that will be called on interactions or updates.
 * <ul>
 *     <li>{@link #onPlace(Block, ArmorStand, Player)} - Called when a player places the firework.</li>
 *     <li>{@link #onLit(ArmorStand, Player)} - Called when the firework is lit by a player using flint and steel.</li>
 *     <li>{@link #onTick(Task, boolean)} - Called when a task of this firework executes a tick.</li>
 * </ul>
 */
public abstract class BlockFireWork extends AbstractFireWork {

    private final Map<UUID, Task> activeTasks = new HashMap<>();

    protected BlockFireWork(@NotNull NamespacedKey key) {
        super(key);
    }

    /**
     * Called when a player places the firework.
     *
     * @param block The placed block.
     * @param stand The spawned ArmorStand of the firework.
     * @param player The player that placed the firework.
     */
    public abstract void onPlace(Block block, ArmorStand stand, Player player);

    /**
     * Called when the firework is lit by a player using flint and steel.
     *
     * @param stand The ArmorStand of the firework, that was lit.
     * @param player The player that lit the firework.
     */
    public abstract void onLit(ArmorStand stand, Player player);

    /**
     * Called when a task of this firework executes a tick.
     *
     * @param task The task that executed the tick.
     * @param active If the delay has passed and the task is actively running.
     */
    public abstract void onTick(Task task, boolean active);

    /**
     * Spawns the {@link ArmorStand} of this firework at the block.
     *
     * @param block The block to place the firework at.
     * @return The spawned ArmorStand, that contains the info about the firework.
     */
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
                t.getPersistentDataContainer().set(FIREWORK_ID, PersistentDataType.STRING, getKey().asString());
                t.getEquipment().setHelmet(itemStack);
            });
        }
        return null;
    }

    /**
     * Checks if the {@link ArmorStand} has an active task already running.
     *
     * @param stand The ArmorStand to check for a task.
     * @return True if there is already a task running; False otherwise.
     */
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

    /**
     * This task can be created to run a specific task in intervals.
     * Like spawning particles or fireworks, etc.<br>
     * This default implementation has one {@link Runnable} that runs once everytime a period passes.
     * <br>
     * Once the duration has been reached the task will stop and removes the {@link ArmorStand} from the world.
     */
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

        /**
         * Creates a new Task that runs for a specified duration after the specified delay has passed.
         *
         * @param player The player that executed the task.
         * @param armorStand The ArmorStand at which the task runs at.
         * @param duration The duration in ticks.
         * @param delay The delay in ticks.
         * @param period The period between each task.
         * @param taskToRun The task to run each period.
         */
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

        /**
         * This method is called each tick, even while the delay hasn't passed yet.
         * Therefor the amount of times this method will be called is equal to <pre>duration + delay</pre>
         *
         * To check if the delay is over use {@link #isActive()}!
         */
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

        /**
         * Starts the task and initialises the internal scheduler.
         * Tick and counter are reset.
         */
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

        /**
         * Stops the internal scheduler if it exists.
         * <br>
         * This removes the ArmorStand entity from the world!
         */
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

        /**
         * Checks if the task is active (The delay is over).
         *
         * @return True if the delay is over; else false.
         */
        public boolean isActive() {
            return tick >= delay;
        }

        /**
         * Gets the firework this task belongs to.
         *
         * @return The firework of this task.
         */
        public BlockFireWork getFirework() {
            return BlockFireWork.this;
        }
    }

}
