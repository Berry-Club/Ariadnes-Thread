package dev.aaronhowser.mods.ariadnesthread.config

import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair

class StartupConfig(
    private val builder: ModConfigSpec.Builder
) {

    companion object {
        private val configPair: Pair<StartupConfig, ModConfigSpec> = ModConfigSpec.Builder().configure(::StartupConfig)

        val CONFIG: StartupConfig = configPair.left
        val CONFIG_SPEC: ModConfigSpec = configPair.right

        lateinit var LINE_THICKNESS: ModConfigSpec.DoubleValue
    }

    init {
        startupConfigs()

        builder.build()
    }

    private fun startupConfigs() {
        LINE_THICKNESS = builder
            .comment("The thickness of the thread.\n\nOnly matters on client.")
            .defineInRange("Thickness", 2.0, 0.0, Double.MAX_VALUE)
    }

}