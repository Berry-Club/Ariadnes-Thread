package com.aaronhowser1.ariadnesthread.config

import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue


object ServerConfig {

    val BUILDER = ForgeConfigSpec.Builder()
    lateinit var SPEC: ForgeConfigSpec
    lateinit var WAIT_TIME: ConfigValue<Int>
    lateinit var MIN_DISTANCE: ConfigValue<Float>
    lateinit var TELEPORT_DISTANCE: ConfigValue<Float>

    init {
        BUILDER.push(" Server configs for Ariadne's Thread")

        WAIT_TIME = BUILDER
            .comment(" The time in ticks to wait between checking location.")
            .define("Wait time", 60)

        MIN_DISTANCE = BUILDER
            .comment(" The minimum distance between points.\nIf you haven't moved more than this distance from your last point, it isn't saved.")
            .define("Minimum Distance", 10F)

        TELEPORT_DISTANCE = BUILDER
            .comment(" The minimum distance between points to count as a teleport.")
            .define("Teleport Distance", 100F)


        BUILDER.pop()
        SPEC = BUILDER.build()
    }

}