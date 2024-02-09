package com.aaronhowser1.ariadnesthread.config

import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue

object ClientConfig {
    private val BUILDER = ForgeConfigSpec.Builder()

    private val teleportDistance: ConfigValue<Double>
    private val alpha: ConfigValue<Double>
    private val debugTooltips: ConfigValue<Boolean>

    val SPEC: ForgeConfigSpec

    val TELEPORT_DISTANCE: Double
        get() = teleportDistance.get()
    val ALPHA: Float
        get() = alpha.get().toFloat()
    val DEBUG_TOOLTIPS: Boolean
        get() = debugTooltips.get()

    init {
        BUILDER.push(" Client configs for Ariadne's Thread")

        alpha = BUILDER
            .comment(" The opacity of the thread.")
            .defineInRange("Alpha", 0.9, 0.0, 1.0)

        teleportDistance = BUILDER
            .comment(" The minimum distance between points to count as a teleport.")
            .defineInRange("Teleport Distance", 30.0, 0.0, Double.MAX_VALUE)

        debugTooltips = BUILDER
            .comment(" Whether or not to show debug information when Advanced Tooltips are enabled.")
            .define("Debug Tooltips", false)

        BUILDER.pop()
        SPEC = BUILDER.build()
    }

    //TODO:
    // - Add configurable color
}