package dev.aaronhowser.mods.ariadnesthread.config

import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair

class ClientConfig(
    private val builder: ModConfigSpec.Builder
) {

    companion object {

        private val configPair: Pair<ClientConfig, ModConfigSpec> = ModConfigSpec.Builder().configure(::ClientConfig)

        val CONFIG: ClientConfig = configPair.left
        val CONFIG_SPEC: ModConfigSpec = configPair.right

        lateinit var TELEPORT_DISTANCE: ModConfigSpec.DoubleValue
        lateinit var LINE_ALPHA: ModConfigSpec.DoubleValue
        lateinit var LINE_START_RED: ModConfigSpec.DoubleValue
        lateinit var LINE_START_GREEN: ModConfigSpec.DoubleValue
        lateinit var LINE_START_BLUE: ModConfigSpec.DoubleValue
        lateinit var LINE_END_RED: ModConfigSpec.DoubleValue
        lateinit var LINE_END_GREEN: ModConfigSpec.DoubleValue
        lateinit var LINE_END_BLUE: ModConfigSpec.DoubleValue
    }


    init {
        clientConfigs()

        builder.build()
    }

    private fun clientConfigs() {
        TELEPORT_DISTANCE = builder
            .comment("The minimum distance between points to count as a teleport.")
            .defineInRange("Teleport Distance", 30.0, 0.0, Double.MAX_VALUE)

        LINE_ALPHA = builder
            .comment("The opacity of the thread.")
            .defineInRange("Alpha", 0.9, 0.0, 1.0)

        LINE_START_RED = builder
            .comment("The red value of the start color.")
            .defineInRange("Start Red", 1.0, 0.0, 1.0)

        LINE_START_GREEN = builder
            .comment("The green value of the start color.")
            .defineInRange("Start Green", 0.0, 0.0, 1.0)

        LINE_START_BLUE = builder
            .comment("The blue value of the start color.")
            .defineInRange("Start Blue", 0.0, 0.0, 1.0)

        LINE_END_RED = builder
            .comment("The red value of the end color.")
            .defineInRange("End Red", 0.0, 0.0, 1.0)

        LINE_END_GREEN = builder
            .comment("The green value of the end color.")
            .defineInRange("End Green", 1.0, 0.0, 1.0)

        LINE_END_BLUE = builder
            .comment("The blue value of the end color.")
            .defineInRange("End Blue", 0.0, 0.0, 1.0)
    }


}