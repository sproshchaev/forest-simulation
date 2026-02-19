package com.javarush.forest.simulation;

import com.javarush.forest.animal.Animal;
import com.javarush.forest.animal.Deer;
import com.javarush.forest.animal.Rabbit;
import com.javarush.forest.animal.Wolf;
import com.javarush.forest.config.SimulationConfig;
import com.javarush.forest.model.Island;
import com.javarush.forest.model.Location;
import com.javarush.forest.model.Plant;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Класс для создания простой однопоточной симуляции
 */
@Slf4j
public class SimpleSimulation {
    private final Island island;
    private final SimulationConfig config;


    public SimpleSimulation(SimulationConfig config) {
        this.config = config;
        this.island = new Island(config.getIslandWidth(), config.getIslandHeight());
    }

    public void initialize() {
        // Размещение Волков
        for (int i = 0; i < config.getInitialWolves(); i++) {
            int x = ThreadLocalRandom.current().nextInt(config.getIslandWidth());
            int y = ThreadLocalRandom.current().nextInt(config.getIslandHeight());
            Wolf wolf = new Wolf();
            island.getLocation(x, y).addAnimal(wolf);
        }
        // Размещение Кроликов
        for (int i = 0; i < config.getInitialRabbits(); i++) {
            int x = ThreadLocalRandom.current().nextInt(config.getIslandWidth());
            int y = ThreadLocalRandom.current().nextInt(config.getIslandHeight());
            Rabbit rabbit = new Rabbit();
            island.getLocation(x, y).addAnimal(rabbit);
        }
        // Размещение Оленей
        for (int i = 0; i < config.getInitialDeer(); i++) {
            int x = ThreadLocalRandom.current().nextInt(config.getIslandWidth());
            int y = ThreadLocalRandom.current().nextInt(config.getIslandHeight());
            Deer deer = new Deer();
            island.getLocation(x, y).addAnimal(deer);
        }
        // Размещаем Растения (по 5 шт. в каждой клетке)
        for (int y = 0; y < island.getHeight(); y++) {
            for (int x = 0; x < island.getWidth(); x++) {
                Location location = island.getLocation(x, y);
                for (int p = 0; p < 5; p++) { // todo заменить магическое число в конфиг
                    location.addPlant(new Plant());
                }
            }
        }
        log.info("Инициализация завершена. Животные и растения размещены.");
    }

    public void tick() {
        // 1) Рост растений
        for (int y = 0; y < island.getHeight(); y++) {
            for (int x = 0; x <island.getWidth(); x++) {
                Location location = island.getLocation(x, y);
                for (int i = 0; i < config.getPlantsPerCell(); i++) {
                    location.addPlant(new Plant());
                }
            }
        }
        // 2) Обработка животных (пройти по всем клеткам)
        for (int y = 0; y < island.getHeight(); y++) {
            for (int x = 0; x < island.getWidth(); x++) {
                Location location = island.getLocation(x, y);
                List<Animal> animals = List.copyOf(location.getAnimals());
                for (Animal animal : animals) {
                    if (!animal.isAlive()) {
                        continue;
                    }
                    animal.eat(location);
                    animal.move(island, x, y);
                    animal.reproduce(location);
                    // Уменьшить сытость
                    animal.setCurrentSatiety(animal.getCurrentSatiety() - 1); // todo магическое число
                    if (animal.getCurrentSatiety() <= 0) {
                        animal.die();
                        location.removeAnimal(animal);
                    }
                }
            }
        }
        printStatistics();
    }

    public void printStatistics() {
        // todo вывести статистику
        int wolves = 0;
        int rabbit = 0;
        int deer = 0;
        int plants = 0;
        for (int y = 0; y < island.getHeight(); y++) {
            for (int x = 0; x < island.getWidth(); x++) {
                Location location = island.getLocation(x, y);
                for (Animal animal : location.getAnimals()) {
                    if (animal instanceof  Wolf) {
                        wolves++;
                    }
                    if (animal instanceof Rabbit) {
                        rabbit++;
                    }
                    if (animal instanceof Deer) {
                        deer++;
                    }
                    plants += location.getPlants().size();
                }
            }
            log.info("Статистика: Волки={}, Кролики={}, Олени={}, Растения={}", wolves, rabbit, deer, plants);
        }
    }

    public void run(int ticks) throws InterruptedException {
        for (int i = 0; i < ticks; i++) {
            log.info("Такт {}", i + 1);
        }
        tick();
        Thread.sleep(1000); // todo магическое число
    }

    public static void main(String[] args) throws InterruptedException {
        SimulationConfig simulationConfig = SimulationConfig.builder()
                .islandWidth(5)
                .islandHeight(5)
                .initialWolves(2)
                .initialRabbits(10)
                .initialDeer(5)
                .plantsPerCell(1)
                .build();
        SimpleSimulation simpleSimulation = new SimpleSimulation(simulationConfig);
        simpleSimulation.initialize();
        simpleSimulation.run(10);
    }



}
