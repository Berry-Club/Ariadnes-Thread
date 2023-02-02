package com.aaronhowser1.ariadnesthread.utils;

import com.aaronhowser1.ariadnesthread.config.ServerConfigs;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;

public class Recorder {

    private final ArrayList<Position> positions = new ArrayList<>();

    public void recordPosition(Player player) {
        Position currentPos = new Position(
                Mth.floor(player.getX()),
                Mth.floor(player.getY()),
                Mth.floor(player.getZ()),
                player.getLevel().dimension()
        );

        if (farEnough(currentPos)) {
            positions.add(currentPos);
        }
    }

    private boolean farEnough(Position position) {
        if (positions.isEmpty()) return true;

        Position mostRecent = positions.get(positions.size()-1);

        if (position.getDimension() != mostRecent.getDimension()) return true;

        double distance = position.toVec3().distanceTo(mostRecent.toVec3());
        return distance > ServerConfigs.MIN_DISTANCE.get();
    }
}
