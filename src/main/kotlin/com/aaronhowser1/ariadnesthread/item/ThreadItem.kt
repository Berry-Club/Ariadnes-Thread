package com.aaronhowser1.ariadnesthread.item

import com.aaronhowser1.ariadnesthread.config.ServerConfig
import com.aaronhowser1.ariadnesthread.utils.Location
import net.minecraft.ChatFormatting
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.*
import net.minecraft.world.level.Level

class ThreadItem : Item(
    Properties()
        .tab(CreativeModeTab.TAB_MISC)
        .stacksTo(1)
        .rarity(Rarity.UNCOMMON)
) {

    companion object {
        const val IS_RECORDING = "ariadnesthread.isRecording"
        const val HISTORY = "ariadnesthread.history"
    }

    private fun isRecording(itemStack: ItemStack): Boolean {
        val tag = itemStack.tag ?: return false

        return tag.getBoolean(IS_RECORDING)
    }

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {

        if (level.isClientSide) return InteractionResultHolder.pass(player.getItemInHand(hand))

        val itemStack = player.getItemInHand(hand)

        val isSneaking = player.isShiftKeyDown
        val isRecording = isRecording(itemStack)

        if (!isRecording) {
            if (isSneaking) {
                clearHistory(itemStack)
                return InteractionResultHolder.success(itemStack)
            }
            startRecording(itemStack)
            return InteractionResultHolder.success(itemStack)
        }

        if (isSneaking) {
            stopRecording(itemStack)
            return InteractionResultHolder.success(itemStack)
        }

        return InteractionResultHolder.pass(itemStack)
    }

    private fun startRecording(itemStack: ItemStack) {
        itemStack.tag = itemStack.tag ?: CompoundTag()
        itemStack.tag?.apply {
            putBoolean(IS_RECORDING, true)

            val hasHistory = itemStack.tag?.contains(HISTORY) ?: false
            if (!hasHistory) {
                val emptyList = ListTag()
                put(HISTORY, emptyList)
            }

        }
    }

    override fun onDroppedByPlayer(item: ItemStack?, player: Player?): Boolean {
        stopRecording(item!!)
        return super.onDroppedByPlayer(item, player)
    }

    override fun inventoryTick(itemStack: ItemStack, level: Level, entity: Entity, slotId: Int, isSelected: Boolean) {

        if (level.isClientSide) return
        if (level.gameTime % ServerConfig.WAIT_TIME != 0L) return

        if (!isRecording(itemStack)) return

        val blockPos = entity.blockPosition()
        val dimension = entity.level.dimension().location()

        val location = Location(dimension, blockPos)

        addLocation(itemStack, location)

        super.inventoryTick(itemStack, level, entity, slotId, isSelected)
    }

    private fun addLocation(itemStack: ItemStack, location: Location) {
        val list = itemStack.tag?.getList(HISTORY, 10) ?: return

        list.add(location.toTag())
    }

    private fun stopRecording(itemStack: ItemStack) {
        itemStack.tag = itemStack.tag ?: CompoundTag()
        itemStack.tag?.putBoolean(IS_RECORDING, false)
    }

    private fun clearHistory(itemStack: ItemStack) {
        itemStack.tag = itemStack.tag ?: CompoundTag()

        itemStack.tag?.remove(HISTORY)
    }

    override fun isFoil(itemStack: ItemStack): Boolean {
        return isRecording(itemStack)
    }

    override fun appendHoverText(
        itemStack: ItemStack,
        level: Level?,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {

        fun tooltipTranslatable(translatable: String, block: (MutableComponent) -> Unit = {}) {
            tooltipComponents.add(
                Component.translatable(translatable).apply {
                    block(this)
                }
            )
        }

        tooltipTranslatable(if (isRecording(itemStack)) "tooltip.ariadnesthread.recording1" else "tooltip.ariadnesthread.not_recording1")
        tooltipTranslatable(if (isRecording(itemStack)) "tooltip.ariadnesthread.recording2" else "tooltip.ariadnesthread.not_recording2")

        if (!isRecording(itemStack)) tooltipTranslatable("tooltip.ariadnesthread.clear") {
            it.withStyle(ChatFormatting.RED)
        }

        tooltipComponents.add(
            Component.literal(
                itemStack.tag.toString()
                    .replace("{", "{\n")
                    .replace("}", "\n}")
            )
        )

        super.appendHoverText(itemStack, level, tooltipComponents, tooltipFlag)
    }

}