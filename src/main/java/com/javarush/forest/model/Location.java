package com.javarush.forest.model;

import com.javarush.forest.animal.Animal;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс Локация содержит списки животных и растений.
 * Метод removePlant удаляет последнее растение (без синхронизации в версии 1.0)
 */
public class Location {

    @Getter
    private final List<Animal> animals = new ArrayList<>();

    @Getter
    private final List<Plant> plants = new ArrayList<>();

    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    public void removeAnimal(Animal animal) {
        animals.remove(animal);
    }

    public void addPlant(Plant plant) {
        plants.add(plant);
    }

    // Для однопоточной версии простое удаление растения
    public Plant removePlant() {
        if (!plants.isEmpty()) {
            return plants.remove(plants.size() - 1);
        }
        return null;
    }

}
