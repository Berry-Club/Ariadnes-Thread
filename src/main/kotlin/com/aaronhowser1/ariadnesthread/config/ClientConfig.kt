package com.aaronhowser1.ariadnesthread.config

import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue

object ClientConfig {
    private val BUILDER = ForgeConfigSpec.Builder()

    private val teleportDistance: ConfigValue<Double>
    private val alpha: ConfigValue<Double>
    private val showNbtInTooltip: ConfigValue<Boolean>

    val SPEC: ForgeConfigSpec

    val TELEPORT_DISTANCE: Double
        get() = teleportDistance.get()
    val ALPHA: Float
        get() = alpha.get().toFloat()
    val SHOW_NBT_TOOLTIP: Boolean
        get() = showNbtInTooltip.get()

    init {
        BUILDER.push(" Client configs for Ariadne's Thread")

        alpha = BUILDER
            .comment(" The opacity of the thread.")
            .defineInRange("Alpha", 0.9, 0.0, 1.0)

        teleportDistance = BUILDER
            .comment(" The minimum distance between points to count as a teleport.")
            .defineInRange("Teleport Distance", 30.0, 0.0, Double.MAX_VALUE)

        showNbtInTooltip = BUILDER
            .comment(" Whether or not to show the entire NBT data in the tooltip. This can be very long and is not recommended. Required advanced tooltips.")
            .define("NBT In Tooltips", false)

        BUILDER.pop()
        SPEC = BUILDER.build()
    }

    //TODO:
    // - Add configurable color
}