package dev.aaronhowser.mods.ariadnesthread.event

import dev.aaronhowser.mods.ariadnesthread.AriadnesThread
import dev.aaronhowser.mods.ariadnesthread.client.ModRenderer
import dev.aaronhowser.mods.ariadnesthread.item.ThreadItem
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.RenderLevelStageEvent
import net.neoforged.neoforge.event.TickEvent

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
        val level = player.level()

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