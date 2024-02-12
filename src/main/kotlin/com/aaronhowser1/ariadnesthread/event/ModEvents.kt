package com.aaronhowser1.ariadnesthread.event

import com.aaronhowser1.ariadnesthread.AriadnesThread
import com.aaronhowser1.ariadnesthread.client.ModRenderer
import com.aaronhowser1.ariadnesthread.item.ThreadItem
import com.aaronhowser1.ariadnesthread.utils.ModScheduler.handleSyncScheduledTasks
import net.minecraft.client.Minecraft
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.RenderLevelStageEvent
import net.neoforged.neoforge.event.TickEvent

@Mod.EventBusSubscriber(modid = AriadnesThread.MOD_ID)
object ModEvents {

    var tick = 0

    @SubscribeEvent
    fun serverTick(event: TickEvent.ServerTickEvent) {
        if (event.phase == TickEvent.Phase.END) {
            tick++
            handleSyncScheduledTasks(tick)

            showThreads()
        }
    }

    private fun showThreads() {
        val player = Minecraft.getInstance().player ?: return

        val dimension = player.level()

        val threadItems =
            player.inventory.items.filter {
                // This returns false for items that don't have the very specific NBT we're looking for,
                // so no need to check if it's a ThreadItem
                ThreadItem.inStartingDimension(it, dimension)
            }

        val histories: List<ThreadItem.Companion.History> = threadItems.map { ThreadItem.getHistory(it) }

        ModRenderer.histories = histories
    }

    @SubscribeEvent
    fun onWorldRenderLast(event: RenderLevelStageEvent) {
        if (event.stage != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return

        if (Minecraft.getInstance().player != null) {
            ModRenderer.renderLines(event)
        }
    }

}