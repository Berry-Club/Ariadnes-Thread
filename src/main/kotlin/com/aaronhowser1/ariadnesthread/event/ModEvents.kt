package com.aaronhowser1.ariadnesthread.event

import com.aaronhowser1.ariadnesthread.AriadnesThread
import com.aaronhowser1.ariadnesthread.client.LineSegment
import com.aaronhowser1.ariadnesthread.utils.ModScheduler.handleSyncScheduledTasks
import net.minecraftforge.client.event.RenderLevelStageEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.TickEvent.ServerTickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@EventBusSubscriber(modid = AriadnesThread.MOD_ID)
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
    fun renderLevelStageEvent(event: RenderLevelStageEvent) {
        if (event.stage != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return

        LineSegment.lineSegments.forEach {
            it.render(event.poseStack)
        }
    }

}