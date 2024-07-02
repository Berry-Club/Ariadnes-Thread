package dev.aaronhowser.mods.ariadnesthread.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.ariadnesthread.config.ServerConfig
import dev.aaronhowser.mods.ariadnesthread.registry.ModDataComponents
import io.netty.buffer.ByteBuf
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class HistoryItemComponent(
    val history: List<LocationItemComponent>
) {

    constructor() : this(emptyList())

    companion object {

        val CODEC: Codec<HistoryItemComponent> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    LocationItemComponent.CODEC.listOf().fieldOf("history").forGetter(HistoryItemComponent::history)
                ).apply(instance, ::HistoryItemComponent)
            }

        val STREAM_CODEC: StreamCodec<ByteBuf, HistoryItemComponent> =
            ByteBufCodecs.fromCodec(CODEC)

        val historyComponent: DataComponentType<HistoryItemComponent> by lazy {
            ModDataComponents.HISTORY_COMPONENT.get()
        }

    }

    fun canAddLocation(location: LocationItemComponent): Boolean {
        if (history.isEmpty()) return true

        val last = history.last()
        return location.closerThan(last, ServerConfig.minDistance.get())
    }

}