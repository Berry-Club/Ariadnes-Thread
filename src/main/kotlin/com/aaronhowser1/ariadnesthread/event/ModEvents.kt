package com.aaronhowser1.ariadnesthread.event

import com.aaronhowser1.ariadnesthread.AriadnesThread
import com.aaronhowser1.ariadnesthread.client.ModRenderer
import com.aaronhowser1.ariadnesthread.utils.ModScheduler.handleSyncScheduledTasks
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.RenderLevelStageEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.TickEvent.ServerTickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(modid = AriadnesThread.MOD_ID)
object ModEvents {

    var tick = 0

    @SubscribeEvent
    fun serverTick(event: ServerTickEvent) {
        if (event.phase == TickEvent.Phase.END) {
            tick++
            handleSyncScheduledTasks(tick)
        }
    }

    @SubscribeEvent
    fun onWorldRenderLast(event: RenderLevelStageEvent) {
        if (event.stage != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return

        if (Minecraft.getInstance().player != null) {
            ModRenderer.renderLines(event)
        }
    }

}