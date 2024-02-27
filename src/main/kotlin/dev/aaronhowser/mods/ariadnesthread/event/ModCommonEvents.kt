package dev.aaronhowser.mods.ariadnesthread.event

import dev.aaronhowser.mods.ariadnesthread.AriadnesThread
import dev.aaronhowser.mods.ariadnesthread.utils.ModScheduler.tick
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.event.TickEvent

@Mod.EventBusSubscriber(
    modid = AriadnesThread.MOD_ID
)
object ModCommonEvents {

    @SubscribeEvent
    fun serverTick(event: TickEvent.ServerTickEvent) {
        if (event.phase == TickEvent.Phase.END) {
            tick++
        }
    }

}