package com.aaronhowser1.ariadnesthread.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<Integer> WAIT_TIME;
    public static final ForgeConfigSpec.ConfigValue<Float> MIN_DISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Float> TELEPORT_DISTANCE;
    static {
        BUILDER.push(" Server configs for Ariadne's Thread");

        WAIT_TIME = BUILDER
                .comment(" The time in ticks to wait between checking location.")
                .define("Wait time", 60);

        MIN_DISTANCE = BUILDER
                .comment(" The minimum distance between points.\nIf you haven't moved more than this distance from your last point, it isn't saved.")
                .define("Minimum Distance",10F);

        TELEPORT_DISTANCE = BUILDER
                .comment(" The minimum distance between points to count as a teleport.")
                .define("Teleport Distance",100F);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
