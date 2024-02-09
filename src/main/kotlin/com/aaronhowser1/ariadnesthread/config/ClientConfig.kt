package com.aaronhowser1.ariadnesthread.config

import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue

//TODO:
//  - Figure out how to convert a "#RRGGBB" string to a color
//  - Figure out how to lerp between them
object ClientConfig {
    private val BUILDER = ForgeConfigSpec.Builder()

    private val startColor: ConfigValue<String>
    private val endColor: ConfigValue<String>
    private val threadTransparency: ConfigValue<Double>

    val SPEC: ForgeConfigSpec

    val COLOR_START: String
        get() = startColor.get()
    val COLOR_END: String
        get() = endColor.get()
    val THREAD_TRANSPARENCY: Double
        get() = threadTransparency.get()

    init {
        BUILDER.push(" Client configs for Ariadne's Thread")

        startColor = BUILDER
            .comment(" The RGB color at the start of the thread.")
            .define("Start Color", "#68D97A")
        endColor = BUILDER
            .comment(" The RGB color at the end of the thread.")
            .define("End Color", "#7177CC")
        threadTransparency = BUILDER
            .comment(" The transparency of the thread.")
            .defineInRange("Thread Transparency", 0.9, 0.0, 1.0)

        BUILDER.pop()
        SPEC = BUILDER.build()
    }
}