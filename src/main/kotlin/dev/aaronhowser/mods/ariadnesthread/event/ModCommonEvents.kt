package dev.aaronhowser.mods.ariadnesthread.event

import dev.aaronhowser.mods.ariadnesthread.AriadnesThread
import dev.aaronhowser.mods.ariadnesthread.utils.ModScheduler
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