package dev.aaronhowser.mods.ariadnesthread.config

import net.neoforged.neoforge.common.ModConfigSpec

object ClientConfig {
    private val BUILDER = ModConfigSpec.Builder()

    private val teleportDistance: ModConfigSpec.DoubleValue
    private val alpha: ModConfigSpec.DoubleValue
    private val startRed: ModConfigSpec.DoubleValue
    private val startGreen: ModConfigSpec.DoubleValue
    private val startBlue: ModConfigSpec.DoubleValue
    private val endRed: ModConfigSpec.DoubleValue
    private val endGreen: ModConfigSpec.DoubleValue
    private val endBlue: ModConfigSpec.DoubleValue
    private val showNbtInTooltip: ModConfigSpec.BooleanValue

    val SPEC: ModConfigSpec

    val TELEPORT_DISTANCE: Double
        get() = teleportDistance.get()
    val ALPHA: Float
        get() = alpha.get().toFloat()

    val START_RGB: List<Float>
        get() = listOf(
            startRed.get().toFloat(),
            startGreen.get().toFloat(),
            startBlue.get().toFloat()
        )

    val END_RGB: List<Float>
        get() = listOf(
            endRed.get().toFloat(),
            endGreen.get().toFloat(),
            endBlue.get().toFloat()
        )

    val SHOW_NBT_TOOLTIP: Boolean
        get() = showNbtInTooltip.get()

    init {
        BUILDER.push("Client")

        teleportDistance = BUILDER
            .comment(" The minimum distance between points to count as a teleport.")
            .defineInRange("Teleport Distance", 30.0, 0.0, Double.MAX_VALUE)

        showNbtInTooltip = BUILDER
            .comment(" Whether or not to show the entire NBT data in the tooltip. This can be very long and is not recommended. Required advanced tooltips.")
            .define("NBT In Tooltips", false)

        BUILDER.push("Appearance")

        alpha = BUILDER
            .comment(" The opacity of the thread.")
            .defineInRange("Alpha", 0.9, 0.0, 1.0)

        startRed = BUILDER
            .comment(" The red value of the start color.")
            .defineInRange("Start Red", 1.0, 0.0, 1.0)

        startGreen = BUILDER
            .comment(" The green value of the start color.")
            .defineInRange("Start Green", 0.0, 0.0, 1.0)

        startBlue = BUILDER
            .comment(" The blue value of the start color.")
            .defineInRange("Start Blue", 0.0, 0.0, 1.0)

        endRed = BUILDER
            .comment(" The red value of the end color.")
            .defineInRange("End Red", 0.0, 0.0, 1.0)

        endGreen = BUILDER
            .comment(" The green value of the end color.")
            .defineInRange("End Green", 1.0, 0.0, 1.0)

        endBlue = BUILDER
            .comment(" The blue value of the end color.")
            .defineInRange("End Blue", 0.0, 0.0, 1.0)

        BUILDER.pop()
        SPEC = BUILDER.build()
    }
}