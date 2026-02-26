package com.javarush.forest;

import com.javarush.forest.config.SimulationConfig;
import com.javarush.forest.simulation.MultithreadedSimulation;
import com.javarush.forest.simulation.SimpleSimulation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

    private static final int SIMPLE_SIMULATION_TICKS = 10;

    public static void main(String[] args) {

        // см аннотация @Builder на классе SimulationConfig
        SimulationConfig config = SimulationConfig.builder()
                .islandWidth(10) // Размеры Леса (ширина)
                .islandHeight(10) // Размера Леса (высота)
                .initialWolves(5) // Популяция Волков
                .initialRabbits(50) // Популяция Кроликов
                .initialDeer(20) // Популяция Оленей
                .plantsPerCell(3) // Число растений на 1 ячейку
                .tickDurationMs(2000)
                .build();

        // Однопоточная симуляция
        // SimpleSimulation simpleSimulation = new SimpleSimulation(config);
        // simpleSimulation.initialize();

        // Выводим сконфигурированное состояние: err, info, debug
        // log.info("Начальное состояние симуляции:");
        // simpleSimulation.printStatistics();

        // try {
        //     simpleSimulation.run(SIMPLE_SIMULATION_TICKS);
        // } catch (InterruptedException e) {
        //    log.error("Ошибка при работе simpleSimulation");
        //    throw new RuntimeException(e);
        // }

        MultithreadedSimulation multithreadedSimulation = new MultithreadedSimulation(config);
        multithreadedSimulation.initialize();

        log.info("Начальное состояние: ");
        multithreadedSimulation.printStatistics();

        multithreadedSimulation.start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        multithreadedSimulation.stop();

        log.info("Работа Симуляции завершена!");

    }
}
