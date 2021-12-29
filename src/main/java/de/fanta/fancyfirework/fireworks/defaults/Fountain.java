package de.fanta.fancyfirework.fireworks.defaults;

import de.fanta.fancyfirework.fireworks.BlockFireWork;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class Fountain {

    private final List<FountainEffect> fountainEffects = new LinkedList<>();
    private Supplier<List<FountainEffect>> createEffects;

    private final int duration;
    private int tick;

    private int fountainEffectCounter;
    private int fountainEffectInterval;

    public Fountain(int duration, int fountainEffectInterval) {
        this.createEffects = List::of;
        this.duration = duration;
        this.tick = 0;
        this.fountainEffectCounter = 0;
        this.fountainEffectInterval = fountainEffectInterval;
    }

    public void setCreateEffects(Supplier<List<FountainEffect>> createEffects) {
        this.createEffects = createEffects;
    }

    public void onTick(BlockFireWork.Task task) {
        if (fountainEffectCounter >= fountainEffectInterval) {
            fountainEffects.addAll(createEffects.get());
            fountainEffectCounter = 0;
        } else {
            fountainEffectCounter++;
        }
        //Spawn fountain effects
        Iterator<FountainEffect> fountainEffectIterator = fountainEffects.iterator();
        while (fountainEffectIterator.hasNext()) {
            FountainEffect effect = fountainEffectIterator.next();
            effect.spawn(task);
            if (effect.isDone()) {
                fountainEffectIterator.remove();
            }
        }
        tick++;
    }

    public boolean isDone() {
        return tick >= duration;
    }
}
