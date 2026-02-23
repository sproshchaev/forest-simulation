package com.javarush.forest.config;

import lombok.Builder;
import lombok.Data;

/**
 * Конфигурация приложения
 * Билдер позволяет создавать объекты в стиле SimulationConfig.builder().islandWidth(10)...build()
 */
@Data
@Builder // аннотация из lombok (Паттерн Билдер)
public class SimulationConfig {
    // Размеры Леса (Острова)
    private int islandWidth;
    private int islandHeight;
    // Популяции
    private int initialWolves;
    private int initialRabbits;
    private int initialDeer;
    // Количество растений которые будут добавляться за 1 такт в каждую клетку
    private int plantsPerCell;
    // Дюрация в мс.
    private long tickDurationMs;
    // Вероятности
}
