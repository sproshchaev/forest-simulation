package com.javarush.forest.animal;

import com.javarush.forest.model.Island;
import com.javarush.forest.model.Location;

import java.util.Map;

public abstract class Animal {
    protected double weight;
    protected double maxSatiety; // максимальная сытость
    protected double currentSatiety; // текущая сытость
    protected boolean alive = true; // животное живое
    protected int speed = 1; // скорость перемещения

    // Карта вероятности поедания других животных
    protected Map<Class<? extends Animal>, Integer> eatingProbabilities;

    public Animal(double weight, double maxSatiety, double currentSatiety) {
        this.weight = weight;
        this.maxSatiety = maxSatiety;
        this.currentSatiety = currentSatiety;
    }

    // eat, move, reproduce
    public abstract void eat(Location location);

    public abstract void move(Island island, int currentX, int currentY);

    public abstract void reproduce(Location location);

    public void die() {
        this.alive = false;
    }

}
