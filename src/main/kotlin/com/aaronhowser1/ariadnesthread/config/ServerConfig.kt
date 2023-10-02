package com.aaronhowser1.ariadnesthread.config

import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue


object ServerConfig {

    private val BUILDER = ForgeConfigSpec.Builder()
    val SPEC: ForgeConfigSpec

    private val waitTime: ConfigValue<Int>
    private val minDistance: ConfigValue<Float>
    private val teleportDistance: ConfigValue<Float>

    val WAIT_TIME: Int
        get() = waitTime.get()
    val MIN_DISTANCE: Float
        get() = minDistance.get()
    val TELEPORT_DISTANCE: Float
        get() = teleportDistance.get()

    init {
        BUILDER.push(" Server configs for Ariadne's Thread")

        waitTime = BUILDER
            .comment(" The time in ticks to wait between checking location.")
            .define("Wait time", 60)

        minDistance = BUILDER
            .comment(" The minimum distance between points.\nIf you haven't moved more than this distance from your last point, it isn't saved.")
            .define("Minimum Distance", 10F)

        teleportDistance = BUILDER
            .comment(" The minimum distance between points to count as a teleport.")
            .define("Teleport Distance", 100F)


        BUILDER.pop()
        SPEC = BUILDER.build()
    }

}