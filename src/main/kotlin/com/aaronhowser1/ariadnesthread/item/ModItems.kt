package com.aaronhowser1.ariadnesthread.item

import com.aaronhowser1.ariadnesthread.AriadnesThread
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

object ModItems {
    val REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(AriadnesThread.MOD_ID)

    val THREAD_ITEM: DeferredItem<ThreadItem> = REGISTRY.register("ariadnes_thread") { _ -> ThreadItem() }

}