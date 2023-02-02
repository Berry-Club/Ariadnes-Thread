package com.aaronhowser1.ariadnesthread.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfigs {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<Integer> WAIT_TIME;
    public static final ForgeConfigSpec.ConfigValue<Float> MIN_DISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Float> TELEPORT_DISTANCE;
    public static final ForgeConfigSpec.ConfigValue<String> COLOR_START;
    public static final ForgeConfigSpec.ConfigValue<String> COLOR_END;
    public static final ForgeConfigSpec.ConfigValue<Float> THREAD_TRANSPARENCY;

    static {
        BUILDER.push(" Client configs for Pitch Perfect");

        WAIT_TIME = BUILDER
                .comment(" The time in ticks to wait between checking location.")
                .define("Wait time", 60);

        MIN_DISTANCE = BUILDER
                .comment(" The minimum distance between points.\nIf you haven't moved more than this distance from your last point, it isn't saved.")
                .define("Minimum Distance",10F);

        TELEPORT_DISTANCE = BUILDER
                .comment(" The minimum distance between points to count as a teleport.")
                .define("Teleport Distance",100F);

        COLOR_START = BUILDER
                .comment(" The RGB color at the start of the thread.")
                .define("Start Color", "#68D97A");

        COLOR_END = BUILDER
                .comment(" The RGB color at the end of the thread.")
                .define("End Color", "#7177CC");

        THREAD_TRANSPARENCY = BUILDER
                .comment(" The transparency of the thread.")
                .defineInRange("Thread Transparency", 0.9F, 0F, 1F, Float.class);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}