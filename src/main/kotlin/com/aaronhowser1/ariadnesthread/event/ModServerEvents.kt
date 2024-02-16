package com.aaronhowser1.ariadnesthread.event

import com.aaronhowser1.ariadnesthread.AriadnesThread
import com.aaronhowser1.ariadnesthread.utils.ModScheduler
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.TickEvent.ServerTickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(
    modid = AriadnesThread.MOD_ID,
    value = [Dist.DEDICATED_SERVER]
)
object ModServerEvents {
    @SubscribeEvent
    fun serverTick(event: ServerTickEvent) {
        if (event.phase == TickEvent.Phase.END) ModScheduler.tick++
    }
}