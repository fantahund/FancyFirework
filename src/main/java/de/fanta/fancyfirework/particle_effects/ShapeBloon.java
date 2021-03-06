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
            double t = i * increment - Math.PI; // -pi...pi

            double r = 1 + 0.7 / Math.cosh(2.75 * t);
            double x = size * 16 * r * -Math.sin(t);
            double y = size * 16 * r * -Math.cos(t);
            vectors.add(new Vector(x, y, 0));
        }
    }

}
