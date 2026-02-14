package com.javarush.forest.animal;

import com.javarush.forest.model.Island;
import com.javarush.forest.model.Location;
import com.javarush.forest.model.Plant;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Rabbit extends Animal {

    public Rabbit() {
        super(2, 1); // вес 2 кг, сытость 1
    }

    @Override
    public void eat(Location location) {
        if (!alive) return;
        Plant plant = location.removePlant();
        if (plant != null) {
            currentSatiety = Math.min(maxSatiety, currentSatiety + plant.getWeight());
            log.debug("Кролик съел растение");
        }
    }

    @Override
    public void move(Island island, int currentX, int currentY) {
        // заглушка
    }

    @Override
    public void reproduce(Location location) {
        // заглушка
    }
}