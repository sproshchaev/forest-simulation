package com.javarush.forest.animal;

import com.javarush.forest.model.Island;
import com.javarush.forest.model.Location;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Data
@NoArgsConstructor
@Slf4j
public abstract class Animal {
    protected double weight;
    protected double maxSatiety;
    protected double currentSatiety;
    protected boolean alive = true;
    protected int speed = 1; // клеток за ход
    protected volatile Location currentLocation; // где животное сейчас

    protected Map<Class<? extends Animal>, Integer> eatingProbabilities;

    public Animal(double weight, double maxSatiety) {
        this.weight = weight;
        this.maxSatiety = maxSatiety;
        this.currentSatiety = maxSatiety;
    }

    public abstract void eat(Location location);

    public void move(Island island, int currentX, int currentY) {
        if (!alive) return;
        if (currentLocation == null) {
            log.warn("Животное {} не имеет текущей локации, перемещение невозможно", this);
            return;
        }

        int direction = ThreadLocalRandom.current().nextInt(4);
        int newX = currentX;
        int newY = currentY;

        switch (direction) {
            case 0: newY = Math.max(0, currentY - 1); break; // вверх
            case 1: newX = Math.min(island.getWidth() - 1, currentX + 1); break; // вправо
            case 2: newY = Math.min(island.getHeight() - 1, currentY + 1); break; // вниз
            case 3: newX = Math.max(0, currentX - 1); break; // влево
        }

        if (newX != currentX || newY != currentY) {
            Location newLoc = island.getLocation(newX, newY);
            currentLocation.removeAnimal(this);
            newLoc.addAnimal(this);
            // currentLocation обновится в addAnimal
        }
    }

    public void reproduce(Location location) {
        if (!alive) return;
        long sameSpeciesCount = location.getAnimals().stream()
                .filter(a -> a.getClass() == this.getClass() && a != this && a.isAlive())
                .count();
        if (sameSpeciesCount > 0 && ThreadLocalRandom.current().nextInt(100) < 30) {
            try {
                Animal baby = this.getClass().getDeclaredConstructor().newInstance();
                baby.setCurrentSatiety(baby.getMaxSatiety() / 2);
                location.addAnimal(baby);
                log.debug("Родилось новое животное {}", baby.getClass().getSimpleName());
            } catch (Exception e) {
                log.error("Ошибка при создании детёныша", e);
            }
        }
    }

    public void die() {
        this.alive = false;
    }
}