package com.javarush.forest.animal;

import com.javarush.forest.model.Island;
import com.javarush.forest.model.Location;
import com.javarush.forest.model.Plant;
import lombok.extern.slf4j.Slf4j;

/**
 * Deer - травоядное, есть растение (Plant)
 */
@Slf4j
public class Deer extends Animal {

    private static final double WEIGHT = 170;
    private static final double MAX_SATIETY = 50;

    public Deer() {
        super(WEIGHT, MAX_SATIETY);
    }

    @Override
    public void eat(Location location) {
        if (!alive) {
            return;
        }
        Plant plant = location.removePlant();
        if (plant != null) {
            currentSatiety = Math.min(maxSatiety, currentSatiety + plant.getWeight());
            log.debug("Олень съел растение");
        }
    }

    @Override
    public void move(Island island, int currentX, int currentY) {
        // todo пока заглушка
    }

    @Override
    public void reproduce(Location location) {
        // todo пока заглушка
    }
}
