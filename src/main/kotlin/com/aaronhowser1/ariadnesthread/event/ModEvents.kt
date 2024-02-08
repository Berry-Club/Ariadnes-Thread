package com.aaronhowser1.ariadnesthread.event

import com.aaronhowser1.ariadnesthread.utils.ModScheduler.handleSyncScheduledTasks
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.TickEvent.ServerTickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

object ModEvents {

    var tick = 0
    @SubscribeEvent
    fun serverTick(event: ServerTickEvent) {
        if (event.phase == TickEvent.Phase.END) {
            tick++
            handleSyncScheduledTasks(tick)
        }
    }

}