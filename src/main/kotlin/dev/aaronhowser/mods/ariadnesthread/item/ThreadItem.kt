package dev.aaronhowser.mods.ariadnesthread.item

import dev.aaronhowser.mods.ariadnesthread.item.component.BooleanItemComponent
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.Level

class ThreadItem : Item(
    Properties()
        .stacksTo(1)
        .rarity(Rarity.UNCOMMON)
        .component(BooleanItemComponent.isRecordingComponent, BooleanItemComponent(false))
) {

    companion object {

        fun isRecording(stack: ItemStack): Boolean =
            stack.get(BooleanItemComponent.isRecordingComponent)?.value ?: false

        fun setRecording(stack: ItemStack, value: Boolean) {
            stack.set(BooleanItemComponent.isRecordingComponent, BooleanItemComponent(value))
        }

    }

    override fun use(pLevel: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        if (pLevel.isClientSide) return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand))

        val stack = pPlayer.getItemInHand(pUsedHand)

        val isSneaking = pPlayer.isCrouching
        val isRecording = isRecording(stack)

        setRecording(stack, !isRecording)

        return InteractionResultHolder.pass(stack)
    }

    override fun onDroppedByPlayer(item: ItemStack, player: Player): Boolean {
        setRecording(item, false)
        return super.onDroppedByPlayer(item, player)
    }

    override fun isFoil(pStack: ItemStack): Boolean {
        return isRecording(pStack)
    }

}