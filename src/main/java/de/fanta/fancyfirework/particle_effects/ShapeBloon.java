package de.fanta.fancyfirework.particle_effects;

import org.bukkit.util.Vector;

public class ShapeBloon extends Shape {
    private final double size;
    private final double increment;
    private final double amount;

    public ShapeBloon(double size, int amount, double increment) {
        this.size = size;
        this.amount = amount;
        this.increment = increment;
    }

    public ShapeBloon(double size) {
        this.size = size;
        this.amount = size * 300;
        this.increment = (2 * Math.PI) / amount;
    }

    @Override
    public void createVectors() {
        for (int i = 0; i < amount; i++) {
            double t = i * increment;
            double x = size * 16 * Math.pow(Math.sin(t), 3);
            double y = size * (13 * Math.cos(t) - 5 * Math.cos(2 * t) - 2 * Math.cos(3 * t) - Math.cos(4 * t));
            vectors.add(new Vector(x, y, 0));
        }
    }



}
