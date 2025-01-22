package dev.aaronhowser.mods.ariadnesthread.registry

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.ariadnesthread.AriadnesThread
import dev.aaronhowser.mods.ariadnesthread.item.component.DimensionItemComponent
import dev.aaronhowser.mods.ariadnesthread.item.component.HistoryItemComponent
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModDataComponents {

    val DATA_COMPONENT_REGISTRY: DeferredRegister.DataComponents =
        DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, AriadnesThread.ID)

    val IS_RECORDING_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<Boolean>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("is_recording") {
            it.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
        }

    val DIMENSION_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<DimensionItemComponent>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("starting_dimension") {
            it.persistent(DimensionItemComponent.CODEC).networkSynchronized(DimensionItemComponent.STREAM_CODEC)
        }

    val HISTORY_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<HistoryItemComponent>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("history") {
            it.persistent(HistoryItemComponent.CODEC).networkSynchronized(HistoryItemComponent.STREAM_CODEC)
        }

}