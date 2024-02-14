package de.fanta.fancyfirework.fireworks;

import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.schedular.CancellableTask;
import de.iani.cubesideutils.bukkit.items.ItemGroups;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Snow;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This firework can be placed into the world and lit by players using flint and steel.<br>
 * If placed on a block it will spawn an ArmorStand with a persistent tag to mark it as a firework. <br>
 * The ArmorStand will then be equipped with a helmet that is the {@link org.bukkit.inventory.ItemStack} from the firework (See {@link #createItemStack()}).<br>
 * <br>
 * There are a couple of methods that will be called on interactions or updates.
 * <ul>
 *     <li>{@link #onPlace(Block, ArmorStand, Player)} - Called when a player places the firework.</li>
 *     <li>{@link #onLit(Entity, Player)} - Called when the firework is lit by a player using flint and steel.</li>
 *     <li>{@link #onTick(Task, boolean)} - Called when a task of this firework executes a tick.</li>
 * </ul>
 */
public abstract class BlockFireWork extends AbstractFireWork {

    private final Map<UUID, Task> activeTasks = new HashMap<>();

    protected BlockFireWork(NamespacedKey key) {
        super(key);
    }

    /**
     * Called when a player places the firework.
     *
     * @param block  The placed block.
     * @param stand  The spawned ArmorStand of the firework.
     * @param player The player that placed the firework.
     */
    public abstract void onPlace(Block block, ArmorStand stand, Player player);

    /**
     * Called when the firework is lit by a player using flint and steel.
     *
     * @param entity The Entity of the firework, that was lit.
     * @param player The player that lit the firework.
     */
    public abstract void onLit(Entity entity, @Nullable Player player);

    /**
     * Called when a task of this firework executes a tick.
     *
     * @param task   The task that executed the tick.
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
                this.applyToEntity(t);
                t.setVisible(false);
                t.setGravity(false);
                t.setHeadPose(new EulerAngle(0, random.nextDouble() * Math.PI * 2, 0));
                t.getEquipment().setHelmet(itemStack);
            });
        }
        return null;
    }

    /**
     * Checks if the {@link Entity} has an active task already running.
     *
     * @param entity The Entity to check for a task.
     * @return True if there is already a task running; False otherwise.
     */
    public boolean hasActiveTask(Entity entity) {
        return activeTasks.containsKey(entity.getUniqueId());
    }

    public Task getActiveTask(Entity entity) {
        return activeTasks.get(entity.getUniqueId());
    }

    private void addTask(Task task) {
        UUID uuid = task.entityUUID;
        if (activeTasks.containsKey(uuid)) {
            activeTasks.get(uuid).stop();
        }
        activeTasks.put(uuid, task);
    }

    private void removeTask(Task task) {
        activeTasks.remove(task.entityUUID);
    }

    /**
     * This task can be created to run a specific task in intervals.
     * Like spawning particles or fireworks, etc.<br>
     * This default implementation has one {@link Runnable} that runs once everytime a period passes.
     * <br>
     * Once the duration has been reached the task will stop and removes the {@link Entity} from the world.
     */
    public class Task {

        protected final Player player;
        protected final UUID entityUUID;

        protected final long duration;
        protected final int delay;
        protected final int period;

        protected long tick;
        protected int counter;
        protected CancellableTask cancellableTask;

        protected final Runnable taskToRun;

        /**
         * Creates a new Task that runs for a specified duration after the specified delay has passed.
         *
         * @param player    The player that executed the task.
         * @param entity    The Entity at which the task runs at.
         * @param duration  The duration in ticks.
         * @param delay     The delay in ticks.
         * @param period    The period between each task.
         * @param taskToRun The task to run each period.
         */
        public Task(Player player, Entity entity, long duration, int delay, int period, Runnable taskToRun) {
            this.player = player;
            this.entityUUID = entity.getUniqueId();
            addTask(this);
            this.duration = duration;
            this.delay = delay;
            this.period = period;
            this.taskToRun = taskToRun;

            this.tick = 0;
            this.cancellableTask = null;
        }

        /**
         * This method is called each tick, even while the delay hasn't passed yet.
         * Therefor the amount of times this method will be called is equal to <pre>duration + delay</pre>
         * <p>
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
            Entity entity = getEntity();
            if (entity == null) {
                stop();
                return;
            }
            this.cancellableTask = FancyFirework.getPlugin().getScheduler().runOnEntityAtFixedRate(entity, () -> {
                if (tick < duration + delay && entity.isValid()) {
                    onTick();
                    tick++;
                } else {
                    stop();
                }
            }, 1, 1);
        }

        /**
         * Stops the internal scheduler if it exists.
         * <br>
         * This removes the entity from the world!
         */
        public void stop() {
            Bukkit.getLogger().info("Tick Stop");
            Entity entity = getEntity();

            if (cancellableTask != null) {
                cancellableTask.cancel();
                cancellableTask = null;
                this.tick = 0;

                //Reset task & remove block + marker entity
                removeTask(this);
                if (entity instanceof Player litPlayer) {
                    ItemStack stack = litPlayer.getInventory().getHelmet();
                    if (stack != null) {
                        AbstractFireWork fireWork = FancyFirework.getPlugin().getRegistry().getByItemStack(stack);
                        if (fireWork != null) {
                            stack.setAmount(stack.getAmount() - 1);
                            litPlayer.getInventory().setHelmet(stack);
                        }
                    }
                } else {
                    if (entity != null) {
                        entity.remove();
                    }
                }
            }
        }

        @Nullable
        public Entity getEntity() {
            return Bukkit.getServer().getEntity(entityUUID);
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
