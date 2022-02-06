package de.fanta.fancyfirework.particle_effects;

import de.fanta.fancyfirework.utils.VectorUtils;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.List;

public class ParticleEffect {

    private final Location origin;
    private final Vector rotation;
    private final Shape shape;
    private final ISpawnParticle spawnParticleFunc;
    private List<Vector> shapeVectors;

    public ParticleEffect(Location origin, Vector rotation, Shape shape, ISpawnParticle spawnParticleFunc) {
        this.spawnParticleFunc = spawnParticleFunc;
        this.origin = origin;
        this.rotation = rotation;
        this.shape = shape;
        shape.createVectors();
        shapeVectors = shape.getVectors();
        if (rotation != null) {
            shapeVectors = shapeVectors.stream().map(vector -> VectorUtils.rotateVector(vector.clone(), rotation.getX(), rotation.getY(), rotation.getZ())).toList();
        }
    }

    public void draw() {
        for (Vector vector : shapeVectors) {
            origin.add(vector);
            spawnParticleFunc.spawn(origin);
            origin.subtract(vector);
        }
    }

}
