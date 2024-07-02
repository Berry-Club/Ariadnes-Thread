package dev.aaronhowser.mods.ariadnesthread.config

import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair

class ServerConfig(
    private val builder: ModConfigSpec.Builder
) {

    companion object {

        private val configPair: Pair<ServerConfig, ModConfigSpec> = ModConfigSpec.Builder().configure(::ServerConfig)

        val CONFIG: ServerConfig = configPair.left
        val CONFIG_SPEC: ModConfigSpec = configPair.right

        lateinit var waitTime: ModConfigSpec.IntValue
        lateinit var minDistance: ModConfigSpec.DoubleValue
        lateinit var maxLocations: ModConfigSpec.IntValue

        val isLimitingLocations: Boolean
            get() = maxLocations.get() > 0
    }


    init {
        serverConfigs()

        builder.build()
    }

    private fun serverConfigs() {
        builder.push("Server")

        waitTime = builder
            .comment("The time in ticks to wait between checking location.")
            .defineInRange("Check interval", 20, 1, Int.MAX_VALUE)

        minDistance = builder
            .comment("The minimum distance between points.\nIf you haven't moved more than this distance from your last point, it isn't saved.")
            .defineInRange("Minimum Distance", 5.0, 0.0, Double.MAX_VALUE)

        maxLocations = builder
            .comment("The maximum number of locations to store. Set to 0 to disable limit.\nEnabling advanced tooltips will show how many locations are stored.")
            .defineInRange("Max Locations", 0, 0, Int.MAX_VALUE)

        builder.pop()
    }


}