package com.aaronhowser1.ariadnesthread.item

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.*
import net.minecraft.world.level.Level

class ThreadItem(
    properties : Properties = Properties()
        .tab(CreativeModeTab.TAB_MISC)
        .stacksTo(1)
        .rarity(Rarity.UNCOMMON)
) : Item(properties) {

    private var isRecording = false

    override fun use(pLevel: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val thisItemStack = pPlayer.getItemInHand(pUsedHand)

        //Sneak click to stop recording, fail if already not
        if (pPlayer.isShiftKeyDown) {
            return if (isRecording) {
                stopRecording()
                InteractionResultHolder.pass(thisItemStack)
            } else {
                InteractionResultHolder.fail(thisItemStack)
            }
        //Normal click to start recording, fail if already
        } else {
            return if (isRecording) {
                InteractionResultHolder.fail(thisItemStack);
            } else {
                startRecording()
                InteractionResultHolder.pass(thisItemStack)
            }
        }
    }

    override fun isFoil(pStack: ItemStack): Boolean {
        return this.isRecording
    }

    override fun appendHoverText(
        pStack: ItemStack,
        pLevel: Level?,
        pTooltipComponents: MutableList<Component>,
        pIsAdvanced: TooltipFlag
    ) {
        pTooltipComponents.add(TranslatableComponent(if (isRecording) "tooltip.ariadnesthread.recording1" else "tooltip.ariadnesthread.not_recording1"))
        pTooltipComponents.add(TranslatableComponent(if (isRecording) "tooltip.ariadnesthread.recording2" else "tooltip.ariadnesthread.not_recording2"))

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced)
    }

    private fun startRecording() {
        this.isRecording = true
    }

    private fun stopRecording() {
        this.isRecording = false
    }

}