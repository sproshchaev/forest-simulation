package com.javarush.forest.config;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimulationConfig {
    private int islandWidth;
    private int islandHeight;
    private int initialWolves;
    private int initialRabbits;
    private int initialDeer;
    private int plantsPerCell;      // сколько растений добавляется за такт в каждую клетку
    private long tickDurationMs;     // пока не используется
}