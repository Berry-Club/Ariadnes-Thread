package dev.aaronhowser.mods.ariadnesthread.registry

import net.neoforged.bus.api.IEventBus

object ModRegistries {

    private val registries = listOf(
        ModDataComponents.DATA_COMPONENT_REGISTRY
    )

    fun register(modBus: IEventBus) {
        registries.forEach { it.register(modBus) }
    }

}