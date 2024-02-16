package com.aaronhowser1.ariadnesthread.event

import com.aaronhowser1.ariadnesthread.AriadnesThread
import com.aaronhowser1.ariadnesthread.utils.ModScheduler
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(
    modid = AriadnesThread.MOD_ID
)
object ModCommonEvents {

    @SubscribeEvent
    fun serverTick(event: TickEvent) {
        if (event.side.isClient) return
        if (event.phase == TickEvent.Phase.END) ModScheduler.tick++
    }

}