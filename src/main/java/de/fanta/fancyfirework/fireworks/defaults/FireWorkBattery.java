package de.fanta.fancyfirework.fireworks.defaults;

import de.fanta.fancyfirework.FancyFirework;
import de.fanta.fancyfirework.fireworks.BlockFireWork;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class FireWorkBattery extends BlockFireWork {

    protected FireWorkBattery(NamespacedKey namespacedKey) {
        super(namespacedKey);
    }

    @Override
    public void onPlace(Block block, ArmorStand stand, Player player) {
        //Nothing
    }

    @Override
    public void onLit(Entity entity, Player player) {
        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_CREEPER_PRIMED, SoundCategory.AMBIENT, 1f, 1f);

        BatteryTask batteryTask = new BatteryTask(player, entity, 20 * 60, 20 * 5, 20, random.nextInt(20 * 5, 20 * 13));
        batteryTask.setSpawnFireworkTask(task -> spawnRandomFirework(entity.getLocation()));
        batteryTask.setSpawnFountainTask(task -> {
            //Create fountain
            Fountain fountain = new Fountain(random.nextInt(20 * 6, 20 * 8), random.nextInt(5, 10));
            fountain.setCreateEffects(() -> {
                //Create next fountain effect/s
                entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_GHAST_SCREAM, SoundCategory.AMBIENT, 2f, 1.5f);

                FountainEffect effect = new FountainEffect(random.nextInt(6, 20), random.nextDouble(0.4, 1), random.nextDouble(359), random.nextDouble(6));
                effect.setSpawnParticle(location -> location.getWorld().spawnParticle(Particle.DUST, location, 6, new Particle.DustOptions(randomColor(), 1.5f)));

                return List.of(effect);
            });
            return fountain;
        });
        batteryTask.start();
    }

    @Override
    public void onTick(Task task, boolean active) {
        Location loc = task.getEntity().getLocation().add(0, 1.5, 0);
        loc.getWorld().spawnParticle(Particle.FLAME, loc, 1, 0, 0, 0, 0.025);

        if (task instanceof BatteryTask batteryTask) {
            if (batteryTask.fountainCounter == 0) {
                batteryTask.fountainCooldown = random.nextInt(20 * 4, 20 * 8);
            }
        }
    }

    @Override
    protected Permission createPermission() {
        Permission permission = new Permission(this.getKey().getNamespace() + ".use." + this.getKey().getKey());
        permission.setDefault(PermissionDefault.TRUE);
        FancyFirework.getPlugin().registerPermission(permission);
        return permission;
    }

    protected abstract Color randomColor();

    protected void spawnRandomFirework(Location location) {
        Random rand = ThreadLocalRandom.current();
        Firework firework = (Firework) location.getWorld().spawnEntity(location.add(0, 1.5, 0), EntityType.FIREWORK_ROCKET);
        firework.setVelocity(new Vector((rand.nextBoolean() ? 1 : -1) * rand.nextDouble(0.02), rand.nextDouble(0.5, 1.5), (rand.nextBoolean() ? 1 : -1) * rand.nextDouble(0.02)));
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        FireworkEffect effect = FireworkEffect.builder().with(FireworkEffect.Type.values()[rand.nextInt(FireworkEffect.Type.values().length)]).withColor(randomColor()).withFade(randomColor()).withFlicker().withTrail().build();
        fireworkMeta.addEffect(effect);
        fireworkMeta.setPower(rand.nextInt(2) + 1);
        firework.setFireworkMeta(fireworkMeta);
        this.applyToEntity(firework);
    }

    public class BatteryTask extends Task {

        private Consumer<Task> spawnFirework;
        private Function<Task, Fountain> spawnFountain;
        private Fountain fountain = null;
        private int fireworkCounter;
        private int fireworkCooldown;
        private int fountainCounter;
        private int fountainCooldown;

        public BatteryTask(Player player, Entity entity, long duration, int delay, int fireworkCooldown, int fountainCooldown) {
            super(player, entity, duration, delay, 0, null);
            this.fireworkCooldown = fireworkCooldown;
            this.fountainCooldown = fountainCooldown;
            this.fireworkCounter = 0;
            this.fountainCounter = 0;
            this.spawnFountain = task -> new Fountain(40, 10);
            this.spawnFirework = task -> {};
        }

        @Override
        public FireWorkBattery getFirework() {
            return FireWorkBattery.this;
        }

        public int getFireworkCooldown() {
            return fireworkCooldown;
        }

        public int getFireworkCounter() {
            return fireworkCounter;
        }

        public void setFireworkCooldown(int fireworkCooldown) {
            this.fireworkCooldown = fireworkCooldown;
        }

        public int getFountainCooldown() {
            return fountainCooldown;
        }

        public void setFountainCooldown(int fountainCooldown) {
            this.fountainCooldown = fountainCooldown;
        }

        public int getFountainCounter() {
            return fountainCounter;
        }

        public void setSpawnFireworkTask(Consumer<Task> spawnFirework) {
            this.spawnFirework = spawnFirework;
        }

        public void setSpawnFountainTask(Function<Task, Fountain> fountainSupplier) {
            this.spawnFountain = fountainSupplier;
        }

        @Override
        protected void onTick() {
            boolean active = isActive();
            if (active) {
                if (fountainCounter >= fountainCooldown) {
                    if(fountain == null) {
                        this.fountain = spawnFountain.apply(this);
                    } else {
                        fountain.onTick(this);
                        if (fountain.isDone()) {
                            fountainCounter = 0;
                            fountain = null;
                        }
                    }
                } else {
                    fountainCounter++;
                }
                if (fireworkCounter >= fireworkCooldown) {
                    spawnFirework.accept(this); //Spawn firework
                    fireworkCounter = 0;
                } else {
                    fireworkCounter++;
                }
            }
            FireWorkBattery.this.onTick(this, active); //Append some other custom things
        }
    }

}

