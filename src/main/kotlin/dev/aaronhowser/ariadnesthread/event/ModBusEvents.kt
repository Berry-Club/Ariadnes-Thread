package dev.aaronhowser.ariadnesthread.event

import dev.aaronhowser.ariadnesthread.AriadnesThread
import dev.aaronhowser.ariadnesthread.item.ModItems
import net.minecraft.world.item.CreativeModeTabs
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent

@Mod.EventBusSubscriber(
    modid = AriadnesThread.MOD_ID,
    bus = Mod.EventBusSubscriber.Bus.MOD
)
object ModBusEvents {

    @SubscribeEvent
    fun creativeTabs(event: BuildCreativeModeTabContentsEvent) {
        if (event.tabKey == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.THREAD_ITEM)
        }
    }

}