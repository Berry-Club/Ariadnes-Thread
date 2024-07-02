package dev.aaronhowser.mods.ariadnesthread.event

import dev.aaronhowser.mods.ariadnesthread.AriadnesThread
import dev.aaronhowser.mods.ariadnesthread.registry.ModItems
import net.minecraft.world.item.CreativeModeTabs
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent

@EventBusSubscriber(
    modid = AriadnesThread.ID,
    bus = EventBusSubscriber.Bus.MOD
)
object ModBusEvents {

    @SubscribeEvent
    fun onCreativeTab(event: BuildCreativeModeTabContentsEvent) {
        if (event.tab == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.ARIADNES_THREAD.get())
        }
    }

}