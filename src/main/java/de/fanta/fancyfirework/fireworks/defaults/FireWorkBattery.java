package de.fanta.fancyfirework.fireworks.defaults;

import de.fanta.fancyfirework.fireworks.BlockFireWork;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public abstract class FireWorkBattery extends BlockFireWork {

    protected FireWorkBattery(NamespacedKey namespacedKey) {
        super(namespacedKey);
    }

    @Override
    public void onLit(ArmorStand stand, Player player) {
        stand.getWorld().playSound(stand.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1f, 1f);
    }

    @Override
    public void onTick(Task task, boolean active) {
        Location loc = task.getArmorStand().getLocation().add(0, 1.5, 0);
        loc.getWorld().spawnParticle(Particle.FLAME, loc, 1, 0, 0, 0, 0.03);
    }

    protected abstract Color randomColor();

    protected void spawnRandomFirework(Location location) {
        Random rand = ThreadLocalRandom.current();
        Firework firework = (Firework) location.getWorld().spawnEntity(location.add(0, 1.5, 0), EntityType.FIREWORK);
        firework.setVelocity(new Vector((rand.nextBoolean() ? 1 : -1) * rand.nextDouble(0.02), rand.nextDouble(0.5, 1.5), (rand.nextBoolean() ? 1 : -1) * rand.nextDouble(0.02)));
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        FireworkEffect effect = FireworkEffect.builder().with(FireworkEffect.Type.values()[rand.nextInt(FireworkEffect.Type.values().length)]).withColor(randomColor()).withFade(randomColor()).withFlicker().withTrail().build();
        fireworkMeta.addEffect(effect);
        fireworkMeta.setPower(rand.nextInt(2) + 1);
        firework.setFireworkMeta(fireworkMeta);
    }

    public class BatteryTask extends Task {

        private final List<FountainEffect> fountainEffects = new LinkedList<>();
        private Function<BatteryTask, List<FountainEffect>> fountainFunction;
        private int fireworkCounter;
        private int fountainCounter;
        private int fireworkInterval;
        private int fountainInterval;

        public BatteryTask(Player player, ArmorStand stand, long duration, int delay, int period, Runnable taskToRun) {
            super(player, stand, duration, delay, period, taskToRun);
            this.fireworkInterval = 20;
            this.fountainInterval = 10;
            this.fireworkCounter = 0;
            this.fountainCounter = 0;
            this.fountainFunction = batteryTask -> List.of();
        }

        public void setFireworkInterval(int fireworkInterval) {
            this.fireworkInterval = fireworkInterval;
        }

        public void setFountainInterval(int fountainInterval) {
            this.fountainInterval = fountainInterval;
        }

        public void setFountainFunction(Function<BatteryTask, List<FountainEffect>> fountainFunction) {
            this.fountainFunction = fountainFunction;
        }

        @Override
        protected void onTick() {
            boolean active = isActive();
            if (active) {
                if (fountainCounter >= fountainInterval) {
                    fountainEffects.addAll(fountainFunction.apply(this)); //Add fountain effects
                    fountainCounter = 0;
                } else {
                    fountainCounter++;
                }
                //Spawn fountain effects
                Iterator<FountainEffect> fountainEffectIterator = fountainEffects.iterator();
                while (fountainEffectIterator.hasNext()) {
                    FountainEffect effect = fountainEffectIterator.next();
                    effect.spawn(FireWorkBattery.this, this);
                    if (effect.isDone()) {
                        fountainEffectIterator.remove();
                    }
                }
                if (fireworkCounter >= fireworkInterval) {
                    taskToRun.run(); //Spawn firework
                    fireworkCounter = 0;
                } else {
                    fireworkCounter++;
                }
            }
            FireWorkBattery.this.onTick(this, active); //Append some other custom things
        }

    }

}

