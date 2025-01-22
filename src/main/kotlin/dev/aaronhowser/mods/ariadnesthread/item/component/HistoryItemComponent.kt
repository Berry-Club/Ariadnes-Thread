package dev.aaronhowser.mods.ariadnesthread.item.component

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.ariadnesthread.config.ServerConfig
import io.netty.buffer.ByteBuf
import net.minecraft.core.BlockPos
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class HistoryItemComponent(
    val locations: List<BlockPos>
) {

    constructor() : this(emptyList())

    companion object {

        val CODEC: Codec<HistoryItemComponent> =
            BlockPos.CODEC.listOf().xmap(::HistoryItemComponent, HistoryItemComponent::locations)

        val STREAM_CODEC: StreamCodec<ByteBuf, HistoryItemComponent> =
            BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list())
                .map(::HistoryItemComponent, HistoryItemComponent::locations)

    }

    fun canAddBlockPos(location: BlockPos): Boolean {
        if (locations.isEmpty()) return true

        if (ServerConfig.isLimitingLocations && locations.size + 1 >= ServerConfig.maxLocations.get()) return false

        val last = locations.last()
        return !location.closerThan(last, ServerConfig.minDistance.get())
    }

}