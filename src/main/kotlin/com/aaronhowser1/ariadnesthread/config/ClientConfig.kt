package com.aaronhowser1.ariadnesthread.config

import net.minecraftforge.common.ForgeConfigSpec

class ClientConfig(builder: ForgeConfigSpec.Builder) {

    companion object {
        
    }

    val COLOR_START = builder
            .comment(" The RGB color at the start of the thread")
            .define("Start Color", "#68D97A")

    val COLOR_END = builder
        .comment(" The RGB color at the end of the thread.")
        .define("End Color", "#7177CC")

    val THREAD_TRANSPARENCY = builder
        .comment(" The transparency of the thread.")
        .defineInRange("Thread Transparency", 0.9F, 0F, 1F, Float::class.java)
}