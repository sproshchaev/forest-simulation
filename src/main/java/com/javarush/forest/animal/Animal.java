package com.javarush.forest.animal;

import com.javarush.forest.model.Island;
import com.javarush.forest.model.Location;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Data
@NoArgsConstructor
@Slf4j
public abstract class Animal {
    protected double weight;
    protected double maxSatiety; // максимальная сытость
    protected double currentSatiety; // текущая сытость
    protected boolean alive = true; // животное живое
    protected int speed = 1; // скорость перемещения

    // Карта вероятности поедания других животных
    protected Map<Class<? extends Animal>, Integer> eatingProbabilities;

    public Animal(double weight, double maxSatiety) {
        this.weight = weight;
        this.maxSatiety = maxSatiety;
        this.currentSatiety = maxSatiety;
    }

    // eat, move, reproduce
    public abstract void eat(Location location);

    public abstract void move(Island island, int currentX, int currentY);

    public abstract void reproduce(Location location);

    public void die() {
        this.alive = false;
    }

}
