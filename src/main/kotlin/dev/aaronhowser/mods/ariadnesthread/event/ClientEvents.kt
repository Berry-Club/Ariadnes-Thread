package dev.aaronhowser.mods.ariadnesthread.event

import dev.aaronhowser.mods.ariadnesthread.AriadnesThread
import dev.aaronhowser.mods.ariadnesthread.client.ModRenderer
import dev.aaronhowser.mods.ariadnesthread.item.ThreadItem
import dev.aaronhowser.mods.ariadnesthread.util.ClientUtil
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.client.event.RenderLevelStageEvent

@EventBusSubscriber(
    modid = AriadnesThread.ID
)
object ClientEvents {

    @SubscribeEvent
    fun clientTick(event: ClientTickEvent.Post) {
        setHistories()
    }

    private fun setHistories() {
        val player = ClientUtil.localPlayer ?: return
        val level = player.level()

        val threadItems = player.inventory.items.filter {
            ThreadItem.inStartingDimension(it, level)
        }

        val histories = threadItems.map { ThreadItem.getHistory(it) }

        if (ModRenderer.histories.isNotEmpty() != histories.isNotEmpty()) {
            ModRenderer.histories = histories
        }
    }

    @SubscribeEvent
    fun onWorldRenderLast(event: RenderLevelStageEvent) {
        if (event.stage != RenderLevelStageEvent.Stage.AFTER_WEATHER) return

        if (ClientUtil.localPlayer != null) {
            ModRenderer.render(event)
        }
    }

}