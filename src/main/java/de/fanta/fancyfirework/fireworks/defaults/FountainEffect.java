package de.fanta.fancyfirework.fireworks.defaults;

import de.fanta.fancyfirework.fireworks.BlockFireWork;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.function.Consumer;

public class FountainEffect {

    private double state;
    private boolean done;

    private final double cutOff;
    private final double heightFactor;
    private final double distance;
    private final double yAxisCos;
    private final double yAxisSin;
    private Consumer<Location> spawnParticle;

    public FountainEffect(double heightFactor, double distance, double rotationDeg, double cutOff) {
        this.heightFactor = heightFactor;
        this.distance = distance;
        double rotation = Math.toRadians(rotationDeg);
        this.yAxisCos = Math.cos(-rotation);
        this.yAxisSin = Math.sin(-rotation);
        this.state = 0;
        this.cutOff = cutOff;
        this.done = false;
        this.spawnParticle = location -> {};
    }

    public void setSpawnParticle(Consumer<Location> spawnParticle) {
        this.spawnParticle = spawnParticle;
    }

    public void spawn(BlockFireWork.Task task) {
        Location location = task.getArmorStand().getLocation().add(0, 1.5, 0).add(getNextVector());
        spawnParticle.accept(location);
    }

    public Vector getNextVector() {
        double x = state;
        double y = Math.sin(x * distance) * heightFactor;
        double zero = Math.PI / distance;
        if (x >= zero - cutOff) {
            this.done = true;
        } else {
            state += 0.1;
        }
        return rotateAroundAxisY(new Vector(x, y, 0), yAxisCos, yAxisSin);
    }

    private Vector rotateAroundAxisY(Vector v, double cos, double sin) {
        double x = v.getX() * cos + v.getZ() * sin;
        double z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }

    public double getState() {
        return state;
    }

    public boolean isDone() {
        return done;
    }
}
