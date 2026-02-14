package com.javarush.forest.model;

import lombok.Getter;

public class Island {
    @Getter
    private final int width;
    @Getter
    private final int height;
    private final Location[][] locations;

    public Island(int width, int height) {
        this.width = width;
        this.height = height;
        this.locations = new Location[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                locations[i][j] = new Location();
            }
        }
    }

    public Location getLocation(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("Координаты вне острова");
        }
        return locations[y][x];
    }
}