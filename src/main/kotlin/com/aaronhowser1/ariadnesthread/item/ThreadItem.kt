package com.aaronhowser1.ariadnesthread.item

import com.aaronhowser1.ariadnesthread.utils.ModScheduler
import net.minecraft.ChatFormatting
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.*
import net.minecraft.world.level.Level

class ThreadItem : Item(
    Properties()
        .tab(CreativeModeTab.TAB_MISC)
        .stacksTo(1)
        .rarity(Rarity.UNCOMMON)
) {

    private fun isRecording(itemStack: ItemStack): Boolean {
        val tag = itemStack.tag ?: return false

        return tag.getBoolean("ariadnesthread.isRecording")
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
            startRecording(itemStack, player)
            return InteractionResultHolder.success(itemStack)
        }

        if (isSneaking) {
            stopRecording(itemStack)
            return InteractionResultHolder.success(itemStack)
        }

        return InteractionResultHolder.pass(itemStack)
    }

    private fun startRecording(itemStack: ItemStack, player: Player) {
        itemStack.tag = itemStack.tag ?: CompoundTag()
        itemStack.tag?.putBoolean("ariadnesthread.isRecording", true)

        addLocation(itemStack, player)
    }

    override fun onDroppedByPlayer(item: ItemStack?, player: Player?): Boolean {
        stopRecording(item!!)
        return super.onDroppedByPlayer(item, player)
    }

    private fun addLocation(itemStack: ItemStack, player: Player) {
        val blockPos = player.blockPosition()
        val dimension = player.level.dimension().location()

        println(
            """
            Is recording: ${isRecording(itemStack)}
            Tag: ${itemStack.tag}
            Pos: $blockPos
            Dimension: $dimension
            """.trimIndent()
        )

        if (isRecording(itemStack)) {
            ModScheduler.scheduleSynchronisedTask(20) {
                addLocation(itemStack, player)
            }
        }
    }

    private fun stopRecording(itemStack: ItemStack) {
        itemStack.tag = itemStack.tag ?: CompoundTag()
        itemStack.tag?.putBoolean("ariadnesthread.isRecording", false)
    }

    private fun clearHistory(itemStack: ItemStack) {
        itemStack.tag = itemStack.tag ?: CompoundTag()

        itemStack.tag?.remove("ariadnesthread.isRecording")
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

        tooltipComponents.add(Component.literal(itemStack.tag.toString()))

        super.appendHoverText(itemStack, level, tooltipComponents, tooltipFlag)
    }

}