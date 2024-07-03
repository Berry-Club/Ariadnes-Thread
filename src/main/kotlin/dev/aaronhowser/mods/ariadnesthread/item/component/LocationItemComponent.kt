package dev.aaronhowser.mods.ariadnesthread.item.component

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.ariadnesthread.registry.ModDataComponents
import io.netty.buffer.ByteBuf
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.codec.StreamCodec

data class LocationItemComponent(
    val blockPos: BlockPos
) {

    val x: Int
        get() = blockPos.x

    val y: Int
        get() = blockPos.y

    val z: Int
        get() = blockPos.z

    companion object {

        val CODEC: Codec<LocationItemComponent> =
            BlockPos.CODEC.xmap(::LocationItemComponent, LocationItemComponent::blockPos)

        val STREAM_CODEC: StreamCodec<ByteBuf, LocationItemComponent> =
            BlockPos.STREAM_CODEC.map(::LocationItemComponent, LocationItemComponent::blockPos)

        val locationComponent: DataComponentType<LocationItemComponent> by lazy {
            ModDataComponents.LOCATION_COMPONENT.get()
        }

    }

    fun distanceToSqr(other: LocationItemComponent): Double {
        return blockPos.distSqr(other.blockPos)
    }

    fun closerThan(other: LocationItemComponent, distance: Double): Boolean {
        return distanceToSqr(other) < distance * distance
    }

}