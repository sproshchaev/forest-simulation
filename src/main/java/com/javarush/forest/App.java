package com.javarush.forest;

import com.javarush.forest.config.SimulationConfig;
import com.javarush.forest.simulation.SimpleSimulation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

    private static final int SIMPLE_SIMULATION_TICKS = 10;

    public static void main(String[] args) {

        // см аннотация @Builder на классе SimulationConfig
        SimulationConfig config = SimulationConfig.builder()
                .islandWidth(5) // Размеры Леса (ширина)
                .islandHeight(5) // Размера Леса (высота)
                .initialWolves(2) // Популяция Волков
                .initialRabbits(10) // Популяция Кроликов
                .initialDeer(5) // Популяция Оленей
                .plantsPerCell(1) // Число растений на 1 ячейку
                .build();

        // Однопоточная симуляция
        SimpleSimulation simpleSimulation = new SimpleSimulation(config);
        simpleSimulation.initialize();

        // Выводим сконфигурированное состояние: err, info, debug
        log.info("Начальное состояние симуляции:");
        simpleSimulation.printStatistics();

        try {
            simpleSimulation.run(SIMPLE_SIMULATION_TICKS);
        } catch (InterruptedException e) {
            log.error("Ошибка при работе simpleSimulation");
            throw new RuntimeException(e);
        }

        log.info("Работа Симуляции завершена!");

    }
}
