package dev.aaronhowser.mods.ariadnesthread.item

import dev.aaronhowser.mods.ariadnesthread.datagen.ModLanguageProvider
import dev.aaronhowser.mods.ariadnesthread.item.component.BooleanItemComponent
import dev.aaronhowser.mods.ariadnesthread.item.component.HistoryItemComponent
import dev.aaronhowser.mods.ariadnesthread.item.component.LocationItemComponent
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class ThreadItem : Item(
    Properties()
        .stacksTo(1)
        .rarity(Rarity.UNCOMMON)
        .component(BooleanItemComponent.isRecordingComponent, BooleanItemComponent(false))
        .component(HistoryItemComponent.historyComponent, HistoryItemComponent())
) {

    companion object {

        fun isRecording(stack: ItemStack): Boolean =
            stack.get(BooleanItemComponent.isRecordingComponent)?.value ?: false

        fun stopRecording(stack: ItemStack) {
            stack.set(BooleanItemComponent.isRecordingComponent, BooleanItemComponent(false))
        }

        fun startRecording(stack: ItemStack) {
            stack.set(BooleanItemComponent.isRecordingComponent, BooleanItemComponent(true))
        }

        fun setHistory(stack: ItemStack, history: List<LocationItemComponent>) {
            stack.set(HistoryItemComponent.historyComponent, HistoryItemComponent(history))
        }

        fun getHistory(stack: ItemStack): List<LocationItemComponent> {
            return stack.get(HistoryItemComponent.historyComponent)?.history ?: emptyList()
        }

        fun hasHistory(stack: ItemStack): Boolean = getHistory(stack).isNotEmpty()

        fun clearHistory(stack: ItemStack) {
            setHistory(stack, emptyList())
        }

        fun addLocation(stack: ItemStack, location: LocationItemComponent) {
            val history = getHistory(stack).toMutableList()
            history.add(location)
            setHistory(stack, history)
        }

    }

    override fun use(pLevel: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        if (pLevel.isClientSide) return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand))

        val stack = pPlayer.getItemInHand(pUsedHand)

        val isSneaking = pPlayer.isCrouching
        val isRecording = isRecording(stack)

        if (!isRecording) {
            if (isSneaking) {
                clearHistory(stack)
                return InteractionResultHolder.success(stack)
            }

            startRecording(stack)
            return InteractionResultHolder.success(stack)
        }

        if (isSneaking) {
            startRecording(stack)
            return InteractionResultHolder.success(stack)
        }

        return InteractionResultHolder.pass(stack)
    }

    override fun onDroppedByPlayer(item: ItemStack, player: Player): Boolean {
        stopRecording(item)
        return super.onDroppedByPlayer(item, player)
    }

    override fun isFoil(pStack: ItemStack): Boolean {
        return isRecording(pStack)
    }

    override fun inventoryTick(
        pStack: ItemStack,
        pLevel: Level,
        player: Entity,
        pSlotId: Int,
        pIsSelected: Boolean
    ) {
        if (player !is Player) return
    }

    override fun appendHoverText(
        pStack: ItemStack,
        pContext: TooltipContext,
        pTooltipComponents: MutableList<Component>,
        pTooltipFlag: TooltipFlag
    ) {

        val isRecording = isRecording(pStack)

        pTooltipComponents.add(
            Component.translatable(
                if (isRecording) ModLanguageProvider.Tooltip.RECORDING_1 else ModLanguageProvider.Tooltip.NOT_RECORDING_1
            ).withStyle(ChatFormatting.GRAY)
        )

        pTooltipComponents.add(
            Component.translatable(
                if (isRecording) ModLanguageProvider.Tooltip.RECORDING_2 else ModLanguageProvider.Tooltip.NOT_RECORDING_2
            ).withStyle(ChatFormatting.GRAY)
        )

        val hasHistory = hasHistory(pStack)

        if (!isRecording && hasHistory) {
            pTooltipComponents.add(
                Component.translatable(
                    ModLanguageProvider.Tooltip.CLEAR
                ).withStyle(ChatFormatting.RED)
            )
        }

    }

}