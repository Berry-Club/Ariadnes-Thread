package dev.aaronhowser.mods.ariadnesthread.event

import dev.aaronhowser.mods.ariadnesthread.AriadnesThread
import dev.aaronhowser.mods.ariadnesthread.utils.ModScheduler.handleSyncScheduledTasks
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.event.TickEvent

@Mod.EventBusSubscriber(
    modid = AriadnesThread.MOD_ID
)
object ModCommonEvents {

    var tick = 0

    @SubscribeEvent
    fun serverTick(event: TickEvent.ServerTickEvent) {
        if (event.phase == TickEvent.Phase.END) {
            tick++
            handleSyncScheduledTasks(tick)
        }
    }

}