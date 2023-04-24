package com.aaronhowser1.ariadnesthread.config

import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.fml.common.Mod


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
object Configs {

    var CLIENT_SPEC: ForgeConfigSpec
        private set
    var CLIENT = ClientConfig
        private set

    init {
        with(ForgeConfigSpec.Builder().configure {ClientConfig(it)}) {
            CLIENT = left
            CLIENT_SPEC = right
        }
    }

}