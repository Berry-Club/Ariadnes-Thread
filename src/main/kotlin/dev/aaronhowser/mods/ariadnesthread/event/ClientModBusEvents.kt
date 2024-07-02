package dev.aaronhowser.mods.ariadnesthread.event

import dev.aaronhowser.mods.ariadnesthread.AriadnesThread
import dev.aaronhowser.mods.ariadnesthread.item.ThreadItem
import dev.aaronhowser.mods.ariadnesthread.util.ClientUtil
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ClientTickEvent

@EventBusSubscriber(
    modid = AriadnesThread.ID,
    bus = EventBusSubscriber.Bus.MOD
)
object ClientModBusEvents {

    @SubscribeEvent
    fun clientTick(event: ClientTickEvent.Post) {
        showThreads()
    }

    private fun showThreads() {
        val player = ClientUtil.localPlayer ?: return
        val level = player.level()

        val threadItems = player.inventory.items.filter {
            ThreadItem.inStartingDimension(it, level)
        }

    }

}