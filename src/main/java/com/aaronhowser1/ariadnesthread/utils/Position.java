package com.aaronhowser1.ariadnesthread.utils;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

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

    public Vec3 toVec3() {
        return new Vec3(x, y, z);
    }

    public ResourceKey<Level> getDimension() {
        return dimension;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", dimension=" + dimension +
                '}';
    }
}
