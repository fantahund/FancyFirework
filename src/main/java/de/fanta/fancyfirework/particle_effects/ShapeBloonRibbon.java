package de.fanta.fancyfirework.particle_effects;

import org.bukkit.util.Vector;

public class ShapeBloonRibbon extends Shape {
    private final double size;
    private final double increment;
    private final double amount;

    public ShapeBloonRibbon(double size, int amount, double increment) {
        this.size = size;
        this.amount = amount;
        this.increment = increment;
    }

    public ShapeBloonRibbon(double size) {
        this.size = size;
        this.amount = size * 100;
        this.increment = (2 * Math.PI) / amount;
    }

    @Override
    public void createVectors() {
        double yStart = size * 16 * -1.7;
        for (int i = 0; i < amount; i++) {
            double t = i * increment;

            double y = yStart - size * 16 * t;
            double x = Math.sin(t * 2.5) * size * 4 * Math.sqrt(t);
            vectors.add(new Vector(x, y, 0));
        }
    }
}
