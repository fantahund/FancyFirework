package de.fanta.fancyfirework.fireworks.defaults;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public class FountainEffect {

    private double state;
    private boolean done;

    private final double heightFactor;
    private final double distance;
    private final double yAxisCos;
    private final double yAxisSin;

    public FountainEffect(double heightFactor, double distance, double rotationDeg) {
        this.heightFactor = heightFactor;
        this.distance = distance;
        double rotation = Math.toRadians(rotationDeg);
        this.yAxisCos = Math.cos(-rotation);
        this.yAxisSin = Math.sin(-rotation);
        this.state = 0;
        this.done = false;
    }

    public Vector getNextVector() {
        double x = state;
        double y = Math.sin(x * distance) * heightFactor;
        if (x != 0 && y <= 0) {
            this.done = true;
        }
        state += 0.1;
        return rotateAroundAxisY(new Vector(x, y, 0), yAxisCos, yAxisSin);
    }

    public void spawn(FireWorkBattery fireWork, FireWorkBattery.BatteryTask task) {
        Location location = task.getArmorStand().getLocation().add(0, 1.5, 0).add(getNextVector());
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 6, new Particle.DustOptions(fireWork.randomColor(), 1.5f));
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
