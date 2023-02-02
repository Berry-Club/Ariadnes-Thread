package com.aaronhowser1.ariadnesthread.utils;

import com.aaronhowser1.ariadnesthread.AriadnesThread;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileUtils {

    public static Path ensureDirectory(Path directory) {
        try {
            return Files.createDirectory(directory);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static Path ensureFile(Path fileName) {
        try {
            return Files.createFile(fileName);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static List<String> getLines(Path path) {
        try {
            return Files.readAllLines(path);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static void addLine() {
//        try {
//
//        } else (Exception e) {
//            System.out.println(e);
//        }
    }

}
