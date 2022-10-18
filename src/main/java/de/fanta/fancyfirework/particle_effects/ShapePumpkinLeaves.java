package de.fanta.fancyfirework.particle_effects;

import org.bukkit.util.Vector;

public class ShapePumpkinLeaves extends Shape {
    private final double size;
    private final double increment;
    private final double amount;

    public ShapePumpkinLeaves(double size, int amount, double increment) {
        this.size = size;
        this.amount = amount;
        this.increment = increment;
    }

    public ShapePumpkinLeaves(double size) {
        this.size = size;
        this.amount = size * 30;
        this.increment = (1 * Math.PI) / amount;
    }

    @Override
    public void createVectors() {
        double yStart = size * 22;
        for (int i = 0; i < amount; i++) {
            double t = i * increment;

            double y = yStart + size * 4 * t;
            double x = Math.sin(t * 2.5) * size * 0.5 * Math.sqrt(t);
            vectors.add(new Vector(x, y, 0));
        }
        for (int i = 0; i < amount; i++) {
            double t = i * increment / 20;

            // double r = 1 + 0.7 / Math.cosh(2.75 * t);
            double x = size * 5 * Math.sin(t);
            double cos = Math.cos(t);
            double y = size * 5 * cos - 1;
            vectors.add(new Vector(x, y + yStart, 0));
            vectors.add(new Vector(-x, y + yStart, 0));
        }
    }
}
