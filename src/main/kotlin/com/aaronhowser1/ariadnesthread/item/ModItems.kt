package com.aaronhowser1.ariadnesthread.item

import com.aaronhowser1.ariadnesthread.AriadnesThread
import net.neoforged.neoforge.registries.DeferredRegister

object ModItems {
    val REGISTRY: DeferredRegister.Items =
        DeferredRegister.createItems(AriadnesThread.MOD_ID)

    val THREAD_ITEM = REGISTRY.register("thread_item") {
        ThreadItem()
    }

}