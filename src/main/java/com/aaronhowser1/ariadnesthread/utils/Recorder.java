package com.aaronhowser1.ariadnesthread.utils;

import com.aaronhowser1.ariadnesthread.AriadnesThread;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Recorder {

    private static final Path localFolder = FileUtils.ensureDirectory(FMLPaths.CONFIGDIR.get().getParent().resolve("local"));
    private static final Path locationsFolder = FileUtils.ensureDirectory(localFolder.resolve(AriadnesThread.MOD_ID));

    public static void recordPositions(MinecraftServer minecraftServer) {
        minecraftServer.getPlayerList().getPlayers().forEach(Recorder::recordPosition);
    }

    private static void recordPosition(Player player) {
        String playerName = player.getScoreboardName();

        Path playerFile = FileUtils.ensureFile(locationsFolder.resolve(playerName+".txt"));

        List<String> positions = FileUtils.getLines(playerFile);
        String mostRecentPosition = positions.get(positions.size()-1);

        String currentPosition = String.format(
                "%i %i %i %s",
                Mth.floor(player.getX()),
                Mth.floor(player.getY()),
                Mth.floor(player.getZ()),
                player.getLevel().dimension()
        );

        if (farEnough(currentPosition, mostRecentPosition)) {
            FileUtils.addLine(playerFile, currentPosition);
        }
    }

    private static boolean farEnough(String pos1, String pos2) {
        return false;
    }

//    private boolean farEnough(Position position) {
//        if (positions.isEmpty()) return true;
//
//        Position mostRecent = positions.get(positions.size()-1);
//
//        if (position.getDimension() != mostRecent.getDimension()) return true;
//
//        double distance = position.toVec3().distanceTo(mostRecent.toVec3());
//        return distance > ServerConfigs.MIN_DISTANCE.get();
//    }
}
