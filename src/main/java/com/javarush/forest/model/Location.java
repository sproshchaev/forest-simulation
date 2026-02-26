package com.javarush.forest.model;

import com.javarush.forest.animal.Animal;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Класс Локация содержит списки животных и растений.
 * Метод removePlant удаляет последнее растение (без синхронизации в версии 1.0)
 */
public class Location {

    @Getter
    private final List<Animal> animals = new CopyOnWriteArrayList<>(); // многопоточность (1)
    private final List<Plant> plants = new CopyOnWriteArrayList<>();

    public void addAnimal(Animal animal) {
        animals.add(animal);
        animal.setCurrentLocation(this);
    }

    public void removeAnimal(Animal animal) {
        animals.remove(animal);
    }

    public void addPlant(Plant plant) {
        plants.add(plant);
    }

    public Plant removePlant() {
        synchronized (plants) { // многопоточность (2)
            if (!plants.isEmpty()) {
                return plants.remove(plants.size() - 1);
            }
            return null;
        }
    }

    public List<Plant> getPlants() {
        return plants;
    }
}
