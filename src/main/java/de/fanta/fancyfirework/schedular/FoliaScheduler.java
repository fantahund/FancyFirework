package de.fanta.fancyfirework.schedular;

import de.fanta.fancyfirework.FancyFirework;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.function.Consumer;

public class FoliaScheduler implements Scheduler {

    private final FancyFirework plugin;

    public FoliaScheduler(FancyFirework plugin) {
        this.plugin = plugin;
    }

    @Override
    public void runAsync(Runnable task) {
        Bukkit.getServer().getAsyncScheduler().runNow(this.plugin, scheduledTask -> task.run());
    }

    @Override
    public void runGlobalDelayed(Runnable task, long delay) {
        Bukkit.getServer().getGlobalRegionScheduler().runDelayed(this.plugin, scheduledTask -> task.run(), delay);
    }

    @Override
    public void runLocalDelayed(Location location, Runnable task, long delay) {
        Bukkit.getServer().getRegionScheduler().runDelayed(this.plugin, location, scheduledTask -> task.run(), delay);
    }

    @Override
    public CancellableTask runLocalAtFixedRate(Location location, Runnable task, long delay, long period) {
        return Bukkit.getServer().getRegionScheduler().runAtFixedRate(this.plugin, location, scheduledTask -> task.run(), delay, period)::cancel;
    }

    @Override
    public void runLocalAtFixedRate(Location location, Consumer<CancellableTask> taskConsumer, long delay, long period) {
        Bukkit.getServer().getRegionScheduler().runAtFixedRate(this.plugin, location, scheduledTask -> taskConsumer.accept(scheduledTask::cancel), delay, period);
    }

    @Override
    public CancellableTask runGlobalAtFixedRate(Runnable task, long delay, long period) {
        return Bukkit.getServer().getGlobalRegionScheduler().runAtFixedRate(this.plugin, scheduledTask -> task.run(), delay, period)::cancel;
    }

    @Override
    public CancellableTask runOnEntityAtFixedRate(Entity entity, Runnable task, long delay, long period) {
        ScheduledTask createdTask = entity.getScheduler().runAtFixedRate(this.plugin, scheduledTask -> task.run(), null, delay, period);
        return createdTask == null ? null : createdTask::cancel;
    }

    @Override
    public void runOnEntityAtFixedRate(Entity entity, Consumer<CancellableTask> taskConsumer, long delay, long period) {
        entity.getScheduler().runAtFixedRate(this.plugin, scheduledTask -> taskConsumer.accept(scheduledTask::cancel), null, delay, period);
    }

    @Override
    public void runOnEntityDelayed(Entity entity, Runnable task, long delay) {
        entity.getScheduler().runDelayed(this.plugin, scheduledTask -> task.run(), null, delay);
    }
}
