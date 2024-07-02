package dev.aaronhowser.mods.ariadnesthread.registry

import dev.aaronhowser.mods.ariadnesthread.AriadnesThread
import dev.aaronhowser.mods.ariadnesthread.item.component.BooleanItemComponent
import dev.aaronhowser.mods.ariadnesthread.item.component.DimensionItemComponent
import dev.aaronhowser.mods.ariadnesthread.item.component.HistoryItemComponent
import dev.aaronhowser.mods.ariadnesthread.item.component.LocationItemComponent
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModDataComponents {

    val DATA_COMPONENT_REGISTRY: DeferredRegister<DataComponentType<*>> =
        DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, AriadnesThread.ID)

    val IS_RECORDING_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<BooleanItemComponent>> =
        DATA_COMPONENT_REGISTRY.register("is_recording", Supplier {
            DataComponentType.builder<BooleanItemComponent>()
                .persistent(BooleanItemComponent.CODEC)
                .networkSynchronized(BooleanItemComponent.STREAM_CODEC)
                .build()
        })

    val DIMENSION_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<DimensionItemComponent>> =
        DATA_COMPONENT_REGISTRY.register("dimension", Supplier {
            DataComponentType.builder<DimensionItemComponent>()
                .persistent(DimensionItemComponent.CODEC)
                .networkSynchronized(DimensionItemComponent.STREAM_CODEC)
                .build()
        })

    val LOCATION_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<LocationItemComponent>> =
        DATA_COMPONENT_REGISTRY.register("location", Supplier {
            DataComponentType.builder<LocationItemComponent>()
                .persistent(LocationItemComponent.CODEC)
                .networkSynchronized(LocationItemComponent.STREAM_CODEC)
                .build()
        })

    val HISTORY_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<HistoryItemComponent>> =
        DATA_COMPONENT_REGISTRY.register("history", Supplier {
            DataComponentType.builder<HistoryItemComponent>()
                .persistent(HistoryItemComponent.CODEC)
                .networkSynchronized(HistoryItemComponent.STREAM_CODEC)
                .build()
        })

}