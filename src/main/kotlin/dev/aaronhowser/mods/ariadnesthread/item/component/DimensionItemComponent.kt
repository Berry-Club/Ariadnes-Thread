package dev.aaronhowser.mods.ariadnesthread.item.component

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.ariadnesthread.registry.ModDataComponents
import io.netty.buffer.ByteBuf
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation

data class DimensionItemComponent(
    val dimensionRl: ResourceLocation
) {

    companion object {
        val CODEC: Codec<DimensionItemComponent> =
            ResourceLocation.CODEC.xmap(::DimensionItemComponent, DimensionItemComponent::dimensionRl)

        val STREAM_CODEC: StreamCodec<ByteBuf, DimensionItemComponent> =
            ResourceLocation.STREAM_CODEC.map(::DimensionItemComponent, DimensionItemComponent::dimensionRl)

        val dimensionComponent: DataComponentType<DimensionItemComponent> by lazy {
            ModDataComponents.DIMENSION_COMPONENT.get()
        }
    }

}