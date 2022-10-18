package de.fanta.fancyfirework.particle_effects;

import org.bukkit.util.Vector;

public class ShapePumpkinFace extends Shape {
    private final double size;

    public ShapePumpkinFace(double size) {
        this.size = size;
    }

    @Override
    public void createVectors() {
        int count = 10;
        double d = 2.0 / count;
        for (int i = 0; i <= count; i++) {
            // nose
            vectors.add(new Vector(size * 2 * (i * d - 1), size * -3, 0));
            vectors.add(new Vector(size * 2 * (i * d - 1), size * (-3 + 3 * (1 - Math.abs(i * d - 1))), 0));

            // eyes
            vectors.add(new Vector(size * (3 * (i * d - 1) - 8), size * 8, 0));
            vectors.add(new Vector(size * (3 * (i * d - 1) - 8), size * (8 + 4 * (1 - Math.abs(i * d - 1))), 0));

            vectors.add(new Vector(size * (3 * (i * d - 1) + 8), size * 8, 0));
            vectors.add(new Vector(size * (3 * (i * d - 1) + 8), size * (8 + 4 * (1 - Math.abs(i * d - 1))), 0));

            // mouth
            // x = -9 ... 9
            vectors.add(new Vector(size * (3 * (i * d - 1) - 6), size * (-9 - 4 * (1 - Math.abs(i * d - 1))), 0));
            vectors.add(new Vector(size * (3 * (i * d - 1)), size * (-11 - 4 * (1 - Math.abs(i * d - 1))), 0));
            vectors.add(new Vector(size * (3 * (i * d - 1) + 6), size * (-9 - 4 * (1 - Math.abs(i * d - 1))), 0));
        }
    }
}
