package com.aaronhowser1.ariadnesthread.event

import com.aaronhowser1.ariadnesthread.AriadnesThread
import com.aaronhowser1.ariadnesthread.client.ModRenderer
import com.aaronhowser1.ariadnesthread.item.ThreadItem
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.RenderLevelStageEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(
    modid = AriadnesThread.MOD_ID,
    value = [Dist.CLIENT]
)
object ModClientEvents {

    @SubscribeEvent
    fun clientTick(event: TickEvent.ClientTickEvent) {
        if (event.phase == TickEvent.Phase.END) showThreads()
    }

    private fun showThreads() {
        val player: LocalPlayer = Minecraft.getInstance().player ?: return
        val level = player.level

        val threadItems =
            player.inventory.items.filter {
                // This returns false for items that don't have the very specific NBT we're looking for,
                // so no need to check if it's a ThreadItem
                ThreadItem.inStartingDimension(it, level)
            }

        val histories: List<ThreadItem.Companion.History> = threadItems.map { ThreadItem.getHistory(it) }

        ModRenderer.histories = histories
    }

    @SubscribeEvent
    fun onWorldRenderLast(event: RenderLevelStageEvent) {
        if (event.stage != RenderLevelStageEvent.Stage.AFTER_WEATHER) return

        if (Minecraft.getInstance().player != null) {
            ModRenderer.renderLines(event)
        }
    }
}