package com.javarush.forest.animal;

import com.javarush.forest.model.Island;
import com.javarush.forest.model.Location;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Wolf хищник
 */
@Slf4j
public class Wolf extends Animal {

    private static final double WEIGTH = 50;
    private static final double MAX_SATIETY = 30;
    /**
     * Для хищника определена карта вероятности (v1, v2)
     */
    private static final Map<Class<? extends Animal>, Integer> EATING_PROBABILITIES = Map.of(Rabbit.class, 60, Deer.class, 80); //todo заменить магические числа!

    public Wolf() {
        super(WEIGTH, MAX_SATIETY);
        this.eatingProbabilities = EATING_PROBABILITIES;
    }

    @Override
    public void eat(Location location) {
        // Жив ли объект?
        if (!alive) {
            return;
        }
        for (Animal prey : location.getAnimals()) {
            if (prey == this || !prey.isAlive()) continue;
            Integer prob = eatingProbabilities.get(prey.getClass());
            if (prob != null && ThreadLocalRandom.current().nextInt(100) < prob) {
                location.removeAnimal(prey);
                prey.die();
                currentSatiety = Math.min(maxSatiety, currentSatiety + prey.getWeight());
                log.debug("Волк съел {}", prey.getClass().getSimpleName());
                break;
            }
        }
    }

    @Override
    public void move(Island island, int currentX, int currentY) {
        //todo Пока заглушка
    }

    @Override
    public void reproduce(Location location) {
        //todo Пока заглушка
    }
}
