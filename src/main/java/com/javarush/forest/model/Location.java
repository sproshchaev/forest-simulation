package com.javarush.forest.model;

import com.javarush.forest.animal.Animal;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Location {
    @Getter
    private final List<Animal> animals = new CopyOnWriteArrayList<>();
    private final List<Plant> plants = new CopyOnWriteArrayList<>(); // геттер оставим отдельно

    public void addAnimal(Animal animal) {
        animals.add(animal);
        animal.setCurrentLocation(this); // сразу устанавливаем локацию
    }

    public void removeAnimal(Animal animal) {
        animals.remove(animal);
    }

    public void addPlant(Plant plant) {
        plants.add(plant);
    }

    public Plant removePlant() {
        synchronized (plants) {
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