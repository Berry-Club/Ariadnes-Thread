package com.aaronhowser1.ariadnesthread.utils;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class Position {

    private final int x;
    private final int y;
    private final int z;
    private final ResourceKey<Level> dimension;

    public Position(int x, int y, int z, ResourceKey<Level> dimension) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
    }

}
