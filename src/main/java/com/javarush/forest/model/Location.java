package com.javarush.forest.model;

import com.javarush.forest.animal.Animal;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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

    // В однопоточной версии удаление растения простое
    public Plant removePlant() {
        if (!plants.isEmpty()) {
            return plants.remove(plants.size() - 1);
        }
        return null;
    }
}