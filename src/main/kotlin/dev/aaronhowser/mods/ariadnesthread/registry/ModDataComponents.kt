package dev.aaronhowser.mods.ariadnesthread.registry

import dev.aaronhowser.mods.ariadnesthread.AriadnesThread
import dev.aaronhowser.mods.ariadnesthread.item.component.BooleanItemComponent
import dev.aaronhowser.mods.ariadnesthread.item.component.DimensionItemComponent
import dev.aaronhowser.mods.ariadnesthread.item.component.HistoryItemComponent
import dev.aaronhowser.mods.ariadnesthread.item.component.LocationItemComponent
import net.minecraft.core.component.DataComponentType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModDataComponents {

    val DATA_COMPONENT_REGISTRY: DeferredRegister.DataComponents =
        DeferredRegister.createDataComponents(AriadnesThread.ID)

    val IS_RECORDING_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<BooleanItemComponent>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("is_recording") {
            it.persistent(BooleanItemComponent.CODEC).networkSynchronized(BooleanItemComponent.STREAM_CODEC)
        }

    val DIMENSION_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<DimensionItemComponent>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("starting_dimension") {
            it.persistent(DimensionItemComponent.CODEC).networkSynchronized(DimensionItemComponent.STREAM_CODEC)
        }

    val LOCATION_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<LocationItemComponent>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("location") {
            it.persistent(LocationItemComponent.CODEC).networkSynchronized(LocationItemComponent.STREAM_CODEC)
        }

    val HISTORY_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<HistoryItemComponent>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("history") {
            it.persistent(HistoryItemComponent.CODEC).networkSynchronized(HistoryItemComponent.STREAM_CODEC)
        }

}