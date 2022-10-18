package de.fanta.fancyfirework.particle_effects;

import org.bukkit.util.Vector;

public class ShapePumpkin extends Shape {
    private final double size;
    private final double increment;
    private final double amount;

    public ShapePumpkin(double size, int amount, double increment) {
        this.size = size;
        this.amount = amount;
        this.increment = increment;
    }

    public ShapePumpkin(double size) {
        this.size = size;
        this.amount = size * 300;
        this.increment = (2 * Math.PI) / amount;
    }

    @Override
    public void createVectors() {
        for (int i = 0; i < amount; i++) {
            double t = i * increment - Math.PI; // -pi...pi

            // double r = 1 + 0.7 / Math.cosh(2.75 * t);
            double x = size * 5 * Math.sin(t);
            double cos = Math.cos(t);
            double y = size * 22 * cos;
            for (int j = -2; j <= 2; j++) {
                if (Math.abs(cos) > 0.8 || (x < 0 && j == -2) || (x > 0 && j == 2)) {
                    vectors.add(new Vector(x * (1 + Math.abs(j) * 0.2) + j * size * 6, y * (1 + Math.abs(j) * 0.05) + Math.abs(j) * 0.5, 0));
                }
            }
        }
    }

}
