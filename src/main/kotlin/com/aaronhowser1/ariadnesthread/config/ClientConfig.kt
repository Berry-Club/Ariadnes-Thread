package com.aaronhowser1.ariadnesthread.config

import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue

object ClientConfig {
    private val BUILDER = ForgeConfigSpec.Builder()

    private val teleportDistance: ConfigValue<Double>
    private val alpha: ConfigValue<Double>
    private val showNbtInTooltip: ConfigValue<Boolean>
    private val startColorRGB: ConfigValue<List<Double>>
    private val endColorRGB: ConfigValue<List<Double>>

    val SPEC: ForgeConfigSpec

    val TELEPORT_DISTANCE: Double
        get() = teleportDistance.get()
    val ALPHA: Float
        get() = alpha.get().toFloat()
    val START_COLOR: DoubleArray
        get() = startColorRGB.get().toDoubleArray().also { checkValid() }
    val END_COLOR: DoubleArray
        get() = endColorRGB.get().toDoubleArray().also { checkValid() }
    val SHOW_NBT_TOOLTIP: Boolean
        get() = showNbtInTooltip.get()

    init {
        BUILDER.push("Client configs for Ariadne's Thread")

        alpha = BUILDER
            .comment(" The opacity of the thread.")
            .defineInRange("Alpha", 0.9, 0.0, 1.0)

        startColorRGB = BUILDER
            .comment(" The color of the thread when it starts. Must be a 3-long array of numbers between 0 and 1.")
            .define("Start Color", listOf(1.0, 0.0, 0.0))

        endColorRGB = BUILDER
            .comment(" The color of the thread when it ends. Must be a 3-long array of numbers between 0 and 1.")
            .define("End Color", listOf(0.0, 1.0, 0.0))

        teleportDistance = BUILDER
            .comment(" The minimum distance between points to count as a teleport.")
            .defineInRange("Teleport Distance", 30.0, 0.0, Double.MAX_VALUE)

        showNbtInTooltip = BUILDER
            .comment(" Whether or not to show the entire NBT data in the tooltip. This can be very long and is not recommended. Required advanced tooltips.")
            .define("NBT In Tooltips", false)

        BUILDER.pop()
        SPEC = BUILDER.build()
    }

    fun checkValid() {
        startColorRGB.get().apply {
            require(size == 3) { "End Color must be a 3-long array of numbers. Instead, it is $size." }
            require(all { it in 0.0..1.0 }) { "One or more Start Colors are not between 0.0 and 1.0." }
        }
        endColorRGB.get().apply {
            require(size == 3) { "End Color must be a 3-long array of numbers. Instead, it is $size." }
            require(all { it in 0.0..1.0 }) { "One or more End Colors are not between 0.0 and 1.0." }
        }
    }

    //TODO:
    // - Add configurable color
}