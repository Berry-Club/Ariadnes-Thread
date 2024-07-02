package dev.aaronhowser.mods.ariadnesthread.registry

import dev.aaronhowser.mods.ariadnesthread.AriadnesThread
import dev.aaronhowser.mods.ariadnesthread.item.ThreadItem
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

object ModItems {

    val ITEMS_REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(AriadnesThread.ID)

    val ARIADNES_THREAD: DeferredItem<ThreadItem> = ITEMS_REGISTRY.registerItem("ariadnes_thread") { ThreadItem() }

}