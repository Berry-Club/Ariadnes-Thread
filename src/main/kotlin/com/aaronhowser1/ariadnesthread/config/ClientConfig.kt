package com.aaronhowser1.ariadnesthread.config

import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue

object ClientConfig {
    private val BUILDER = ForgeConfigSpec.Builder()

    private val startColor: ConfigValue<String>
    private val endColor: ConfigValue<String>
    private val threadTransparency: ConfigValue<Double>

    val SPEC: ForgeConfigSpec

    val COLOR_START: Int
        get() = startColor.get().substring(1).toInt(16)
    val COLOR_END: Int
        get() = endColor.get().substring(1).toInt(16)
    val THREAD_TRANSPARENCY: Double
        get() = threadTransparency.get()

    init {
        BUILDER.push(" Client configs for Ariadne's Thread")

        startColor = BUILDER
            .comment(" The RGB color at the start of the thread.")
            .define("Start Color", "#FF68D97A")
        endColor = BUILDER
            .comment(" The RGB color at the end of the thread.")
            .define("End Color", "#FF7177CC")
        threadTransparency = BUILDER
            .comment(" The transparency of the thread.")
            .defineInRange("Thread Transparency", 0.9, 0.0, 1.0)

        BUILDER.pop()
        SPEC = BUILDER.build()
    }
}