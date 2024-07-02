package dev.aaronhowser.mods.ariadnesthread.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.ariadnesthread.registry.ModDataComponents
import io.netty.buffer.ByteBuf
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class LocationItemComponent(
    val x: Double,
    val y: Double,
    val z: Double
) {

    companion object {

        val CODEC: Codec<LocationItemComponent> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.DOUBLE.fieldOf("x").forGetter(LocationItemComponent::x),
                Codec.DOUBLE.fieldOf("y").forGetter(LocationItemComponent::y),
                Codec.DOUBLE.fieldOf("z").forGetter(LocationItemComponent::z)
            ).apply(instance, ::LocationItemComponent)
        }

        val STREAM_CODEC: StreamCodec<ByteBuf, LocationItemComponent> = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, LocationItemComponent::x,
            ByteBufCodecs.DOUBLE, LocationItemComponent::y,
            ByteBufCodecs.DOUBLE, LocationItemComponent::z,
            ::LocationItemComponent
        )

        val locationComponent: DataComponentType<LocationItemComponent> by lazy {
            ModDataComponents.LOCATION_COMPONENT.get()
        }

    }

    fun distanceToSqr(other: LocationItemComponent): Double {
        val dx = x - other.x
        val dy = y - other.y
        val dz = z - other.z
        return dx * dx + dy * dy + dz * dz
    }

    fun closerThan(other: LocationItemComponent, distance: Double): Boolean {
        return distanceToSqr(other) < distance * distance
    }

}