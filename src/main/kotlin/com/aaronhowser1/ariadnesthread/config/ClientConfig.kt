package com.aaronhowser1.ariadnesthread.config

import net.minecraftforge.common.ForgeConfigSpec

object ClientConfig {
    private val BUILDER = ForgeConfigSpec.Builder()

    val SPEC : ForgeConfigSpec
    val COLOR_START : ForgeConfigSpec.ConfigValue<String>
    val COLOR_END : ForgeConfigSpec.ConfigValue<String>
    val THREAD_TRANSPARENCY : ForgeConfigSpec.ConfigValue<Float>

    init {
        BUILDER.push(" Client configs for Ariadne's Thread")
        COLOR_START = BUILDER
            .comment(" The RGB color at the start of the thread.")
            .define("Start Color", "#68D97A")
        COLOR_END = BUILDER
            .comment(" The RGB color at the end of the thread.")
            .define("End Color", "#7177CC")
        THREAD_TRANSPARENCY = BUILDER
            .comment(" The transparency of the thread.")
            .defineInRange("Thread Transparency", 0.9f, 0f, 1f, Float::class.java)
        BUILDER.pop()
        SPEC = BUILDER.build()
    }
}