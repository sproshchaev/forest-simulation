package com.javarush.forest;

import com.javarush.forest.config.SimulationConfig;
import com.javarush.forest.simulation.SimpleSimulation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    public static void main(String[] args) throws InterruptedException {
        SimulationConfig config = SimulationConfig.builder()
                .islandWidth(5)
                .islandHeight(5)
                .initialWolves(2)
                .initialRabbits(10)
                .initialDeer(5)
                .plantsPerCell(1)
                .build();

        SimpleSimulation simulation = new SimpleSimulation(config);
        simulation.initialize();
        simulation.run(10);
        log.info("Симуляция завершена.");
    }
}