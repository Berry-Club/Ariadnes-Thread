package dev.aaronhowser.mods.ariadnesthread.registry

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.ariadnesthread.AriadnesThread
import dev.aaronhowser.mods.ariadnesthread.item.component.HistoryItemComponent
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModDataComponents {

    val DATA_COMPONENT_REGISTRY: DeferredRegister.DataComponents =
        DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, AriadnesThread.ID)

    val IS_RECORDING: DeferredHolder<DataComponentType<*>, DataComponentType<Boolean>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("is_recording") {
            it.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
        }

    val STARTING_DIMENSION: DeferredHolder<DataComponentType<*>, DataComponentType<ResourceKey<Level>>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("starting_dimension") {
            it.persistent(ResourceKey.codec(Registries.DIMENSION)).networkSynchronized(ResourceKey.streamCodec(Registries.DIMENSION))
        }

    val HISTORY: DeferredHolder<DataComponentType<*>, DataComponentType<HistoryItemComponent>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("history") {
            it.persistent(HistoryItemComponent.CODEC).networkSynchronized(HistoryItemComponent.STREAM_CODEC)
        }

}