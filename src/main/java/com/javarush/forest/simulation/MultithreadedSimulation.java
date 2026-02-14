package com.javarush.forest.simulation;

import com.javarush.forest.animal.*;
import com.javarush.forest.config.SimulationConfig;
import com.javarush.forest.model.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class MultithreadedSimulation {
    private final Island island;
    private final SimulationConfig config;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final ExecutorService workerPool = Executors.newFixedThreadPool(10);
    private volatile boolean running = true;

    public MultithreadedSimulation(SimulationConfig config) {
        this.config = config;
        this.island = new Island(config.getIslandWidth(), config.getIslandHeight());
    }

    public void initialize() {
        // Аналогично SimpleSimulation
        for (int i = 0; i < config.getInitialWolves(); i++) {
            int x = ThreadLocalRandom.current().nextInt(config.getIslandWidth());
            int y = ThreadLocalRandom.current().nextInt(config.getIslandHeight());
            Wolf wolf = new Wolf();
            island.getLocation(x, y).addAnimal(wolf);
        }
        for (int i = 0; i < config.getInitialRabbits(); i++) {
            int x = ThreadLocalRandom.current().nextInt(config.getIslandWidth());
            int y = ThreadLocalRandom.current().nextInt(config.getIslandHeight());
            Rabbit rabbit = new Rabbit();
            island.getLocation(x, y).addAnimal(rabbit);
        }
        for (int i = 0; i < config.getInitialDeer(); i++) {
            int x = ThreadLocalRandom.current().nextInt(config.getIslandWidth());
            int y = ThreadLocalRandom.current().nextInt(config.getIslandHeight());
            Deer deer = new Deer();
            island.getLocation(x, y).addAnimal(deer);
        }
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

    private void tick() {
        // Рост растений (в главном потоке)
        for (int y = 0; y < island.getHeight(); y++) {
            for (int x = 0; x < island.getWidth(); x++) {
                Location loc = island.getLocation(x, y);
                for (int i = 0; i < config.getPlantsPerCell(); i++) {
                    loc.addPlant(new Plant());
                }
            }
        }

        // Сбор задач для каждого живого животного
        List<Callable<Void>> tasks = new ArrayList<>();
        for (int y = 0; y < island.getHeight(); y++) {
            for (int x = 0; x < island.getWidth(); x++) {
                Location loc = island.getLocation(x, y);
                int finalX = x;
                int finalY = y;
                for (Animal animal : loc.getAnimals()) {
                    if (!animal.isAlive()) continue;
                    tasks.add(() -> {
                        animal.eat(animal.getCurrentLocation()); // используем актуальную локацию
                        animal.move(island, finalX, finalY);
                        animal.reproduce(animal.getCurrentLocation());
                        animal.setCurrentSatiety(animal.getCurrentSatiety() - 1);
                        if (animal.getCurrentSatiety() <= 0) {
                            animal.die();
                            animal.getCurrentLocation().removeAnimal(animal);
                        }
                        return null;
                    });
                }
            }
        }

        try {
            List<Future<Void>> futures = workerPool.invokeAll(tasks);
            for (Future<Void> f : futures) {
                f.get(); // проверяем исключения
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Такт прерван");
        } catch (ExecutionException e) {
            log.error("Ошибка при выполнении задачи животного", e.getCause());
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

    public void start() {
        scheduler.scheduleAtFixedRate(() -> {
            if (running) {
                try {
                    tick();
                } catch (Exception e) {
                    log.error("Ошибка в такте симуляции", e);
                }
            }
        }, 0, config.getTickDurationMs(), TimeUnit.MILLISECONDS);
        log.info("Симуляция запущена с тактом {} мс", config.getTickDurationMs());
    }

    public void stop() {
        running = false;
        scheduler.shutdown();
        workerPool.shutdown();
        try {
            if (!scheduler.awaitTermination(2, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
            if (!workerPool.awaitTermination(2, TimeUnit.SECONDS)) {
                workerPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            workerPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        log.info("Симуляция остановлена.");
    }
}