package com.javarush.forest;

import com.javarush.forest.config.SimulationConfig;
import com.javarush.forest.simulation.MultithreadedSimulation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    public static void main(String[] args) throws InterruptedException {
        SimulationConfig config = SimulationConfig.builder()
                .islandWidth(10)
                .islandHeight(10)
                .initialWolves(5)
                .initialRabbits(50)
                .initialDeer(20)
                .plantsPerCell(3)
                .tickDurationMs(2000) // 2 секунды на такт
                .build();

        MultithreadedSimulation simulation = new MultithreadedSimulation(config);
        simulation.initialize();

        // Выводим начальное состояние
        log.info("Начальное состояние:");
        simulation.printStatistics();

        simulation.start();

        // Даём симуляции поработать 30 секунд
        Thread.sleep(30000);
        simulation.stop();
        log.info("Симуляция завершена.");
    }
}