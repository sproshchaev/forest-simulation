package com.javarush.forest.simulation;

import com.javarush.forest.animal.*;
import com.javarush.forest.config.SimulationConfig;
import com.javarush.forest.model.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class SimpleSimulation {
    private final Island island;
    private final SimulationConfig config;

    public SimpleSimulation(SimulationConfig config) {
        this.config = config;
        this.island = new Island(config.getIslandWidth(), config.getIslandHeight());
    }

    public void initialize() {
        // Размещаем волков
        for (int i = 0; i < config.getInitialWolves(); i++) {
            int x = ThreadLocalRandom.current().nextInt(config.getIslandWidth());
            int y = ThreadLocalRandom.current().nextInt(config.getIslandHeight());
            Wolf wolf = new Wolf();
            island.getLocation(x, y).addAnimal(wolf);
        }
        // Кролики
        for (int i = 0; i < config.getInitialRabbits(); i++) {
            int x = ThreadLocalRandom.current().nextInt(config.getIslandWidth());
            int y = ThreadLocalRandom.current().nextInt(config.getIslandHeight());
            Rabbit rabbit = new Rabbit();
            island.getLocation(x, y).addAnimal(rabbit);
        }
        // Олени
        for (int i = 0; i < config.getInitialDeer(); i++) {
            int x = ThreadLocalRandom.current().nextInt(config.getIslandWidth());
            int y = ThreadLocalRandom.current().nextInt(config.getIslandHeight());
            Deer deer = new Deer();
            island.getLocation(x, y).addAnimal(deer);
        }
        // Начальные растения (по 5 в каждой клетке)
        for (int y = 0; y < island.getHeight(); y++) {
            for (int x = 0; x < island.getWidth(); x++) {
                Location loc = island.getLocation(x, y);
                for (int p = 0; p < 5; p++) {
                    loc.addPlant(new Plant());
                }
            }
        }
        log.info("Инициализация завершена. Животные и растения размещены.");
    }

    public void tick() {
        // Рост растений
        for (int y = 0; y < island.getHeight(); y++) {
            for (int x = 0; x < island.getWidth(); x++) {
                Location loc = island.getLocation(x, y);
                for (int i = 0; i < config.getPlantsPerCell(); i++) {
                    loc.addPlant(new Plant());
                }
            }
        }

        // Обработка животных (проходим по всем клеткам)
        for (int y = 0; y < island.getHeight(); y++) {
            for (int x = 0; x < island.getWidth(); x++) {
                Location loc = island.getLocation(x, y);
                // Копируем список, чтобы избежать ConcurrentModification при удалении (в однопоточке необязательно, но оставим)
                List<Animal> animals = List.copyOf(loc.getAnimals());
                for (Animal animal : animals) {
                    if (!animal.isAlive()) continue;
                    animal.eat(loc);
                    animal.move(island, x, y);
                    animal.reproduce(loc);
                    // Уменьшаем сытость (условно)
                    animal.setCurrentSatiety(animal.getCurrentSatiety() - 1);
                    if (animal.getCurrentSatiety() <= 0) {
                        animal.die();
                        loc.removeAnimal(animal);
                    }
                }
            }
        }
        printStatistics();
    }

    public void printStatistics() {
        int wolves = 0, rabbits = 0, deer = 0, plants = 0;
        for (int y = 0; y < island.getHeight(); y++) {
            for (int x = 0; x < island.getWidth(); x++) {
                Location loc = island.getLocation(x, y);
                for (Animal a : loc.getAnimals()) {
                    if (a instanceof Wolf) wolves++;
                    else if (a instanceof Rabbit) rabbits++;
                    else if (a instanceof Deer) deer++;
                }
                plants += loc.getPlants().size();
            }
        }
        log.info("Статистика: Волки={}, Кролики={}, Олени={}, Растения={}", wolves, rabbits, deer, plants);
    }

    public void run(int ticks) throws InterruptedException {
        for (int i = 0; i < ticks; i++) {
            log.info("Такт {}", i + 1);
            tick();
            Thread.sleep(1000);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SimulationConfig config = SimulationConfig.builder()
                .islandWidth(5)
                .islandHeight(5)
                .initialWolves(2)
                .initialRabbits(10)
                .initialDeer(5)
                .plantsPerCell(1)
                .build();
        SimpleSimulation sim = new SimpleSimulation(config);
        sim.initialize();
        sim.run(10);
    }
}