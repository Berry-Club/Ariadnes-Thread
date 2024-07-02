package dev.aaronhowser.mods.ariadnesthread.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.ariadnesthread.registry.ModDataComponents
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

class BooleanItemComponent(
    val value: Boolean
) {
    companion object {

        val CODEC: Codec<BooleanItemComponent> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.BOOL.fieldOf("value").forGetter(BooleanItemComponent::value)
            ).apply(instance, ::BooleanItemComponent)
        }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, BooleanItemComponent> = StreamCodec.composite(
            ByteBufCodecs.BOOL, BooleanItemComponent::value,
            ::BooleanItemComponent
        )

        val isRecordingComponent: DataComponentType<BooleanItemComponent> by lazy { ModDataComponents.IS_RECORDING_COMPONENT.get() }

    }

}