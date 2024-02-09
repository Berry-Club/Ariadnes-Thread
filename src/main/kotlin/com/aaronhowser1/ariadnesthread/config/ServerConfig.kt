package com.aaronhowser1.ariadnesthread.config

import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue


object ServerConfig {

    private val BUILDER = ForgeConfigSpec.Builder()
    val SPEC: ForgeConfigSpec

    private val waitTime: ConfigValue<Int>
    private val minDistance: ConfigValue<Double>
    private val teleportDistance: ConfigValue<Double>
    private val maxLocations: ConfigValue<Int>

    val CHECK_INTERVAL: Int
        get() = waitTime.get()
    val MIN_DISTANCE: Double
        get() = minDistance.get()
    val MAX_LOCATIONS: Int
        get() = maxLocations.get()
    val TELEPORT_DISTANCE: Double
        get() = teleportDistance.get()

    init {
        BUILDER.push(" Server configs for Ariadne's Thread")

        waitTime = BUILDER
            .comment(" The time in ticks to wait between checking location.")
            .define("Check interval", 20)

        minDistance = BUILDER
            .comment(" The minimum distance between points.\nIf you haven't moved more than this distance from your last point, it isn't saved.")
            .define("Minimum Distance", 5.0)

        maxLocations = BUILDER
            .comment(" The maximum number of locations to store.")
            .defineInRange("Max Locations", 5120, 0, Int.MAX_VALUE)

        teleportDistance = BUILDER
            .comment(" The minimum distance between points to count as a teleport.")
            .define("Teleport Distance", 100.0)


        BUILDER.pop()
        SPEC = BUILDER.build()
    }

}