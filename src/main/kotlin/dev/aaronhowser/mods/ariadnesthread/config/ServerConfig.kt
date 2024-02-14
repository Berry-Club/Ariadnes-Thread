package dev.aaronhowser.mods.ariadnesthread.config

import net.neoforged.neoforge.common.ModConfigSpec

object ServerConfig {

    private val BUILDER = ModConfigSpec.Builder()
    val SPEC: ModConfigSpec

    private val waitTime: ModConfigSpec.IntValue
    private val minDistance: ModConfigSpec.DoubleValue
    private val maxLocations: ModConfigSpec.IntValue
    private val maxNbtSize: ModConfigSpec.IntValue

    val CHECK_INTERVAL: Int
        get() = waitTime.get()
    val MIN_DISTANCE: Double
        get() = minDistance.get()
    val MAX_LOCATIONS: Int
        get() = maxLocations.get()
    val MAX_NBT_SIZE: Int
        get() = maxNbtSize.get()

    val shouldCheckNbt: Boolean
        get() = MAX_NBT_SIZE > 0
    val shouldCheckLocations: Boolean
        get() = MAX_LOCATIONS > 0

    init {
        BUILDER.push("Server")

        waitTime = BUILDER
            .comment(" The time in ticks to wait between checking location.")
            .defineInRange("Check interval", 20, 1, Int.MAX_VALUE)

        minDistance = BUILDER
            .comment(" The minimum distance between points.\n If you haven't moved more than this distance from your last point, it isn't saved.")
            .defineInRange("Minimum Distance", 5.0, 0.0, Double.MAX_VALUE)

        BUILDER.push("Limits")

        maxLocations = BUILDER
            .comment(" The maximum number of locations to store. Set to 0 to disable limit.\n Enabling advanced tooltips will show how many locations are stored.")
            .defineInRange("Max Locations", 5120, 0, Int.MAX_VALUE)

        maxNbtSize = BUILDER
            .comment(" The maximum size of the NBT data for the thread item, in bits. Set to 0 to disable limit.\n Enabling advanced tooltips will show the current size.")
            .defineInRange("Max NBT Size", 65536, 0, Int.MAX_VALUE)

        BUILDER.pop()
        SPEC = BUILDER.build()
    }

}