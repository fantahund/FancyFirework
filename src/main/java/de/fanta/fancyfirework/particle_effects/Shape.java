package de.fanta.fancyfirework.particle_effects;

import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public abstract class Shape {

    protected List<Vector> vectors;

    protected Shape() {
        this.vectors = new ArrayList<>();
    }

    public abstract void createVectors();

    public List<Vector> getVectors() {
        return vectors;
    }
}
