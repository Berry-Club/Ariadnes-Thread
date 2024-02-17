package dev.aaronhowser.ariadnesthread.config

import net.neoforged.neoforge.common.ModConfigSpec

object ClientConfig {
    private val BUILDER = ModConfigSpec.Builder()

    private val teleportDistance: ModConfigSpec.DoubleValue
    private val alpha: ModConfigSpec.DoubleValue
    private val showNbtInTooltip: ModConfigSpec.BooleanValue

    val SPEC: ModConfigSpec

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