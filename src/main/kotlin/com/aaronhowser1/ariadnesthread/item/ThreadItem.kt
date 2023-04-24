package com.aaronhowser1.ariadnesthread.item

import com.aaronhowser1.ariadnesthread.config.ServerConfig
import com.aaronhowser1.ariadnesthread.utils.ModScheduler
import net.minecraft.ChatFormatting
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.*
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

class ThreadItem(
    properties : Properties = Properties()
        .tab(CreativeModeTab.TAB_MISC)
        .stacksTo(1)
        .rarity(Rarity.UNCOMMON)
) : Item(properties) {

    private fun isRecording(itemStack: ItemStack): Boolean {
        if (!itemStack.hasTag()) return false
        val tag = itemStack.tag!!
        if (!tag.contains("ariadnesthread.isRecording")) return false
        return tag.getBoolean("ariadnesthread.isRecording")
    }

    override fun use(pLevel: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val itemStack = pPlayer.getItemInHand(pUsedHand)

        //Sneak click to stop recording, fail if already not
        if (pPlayer.isShiftKeyDown) {
            return if (isRecording(itemStack)) {
                stopRecording(itemStack)
                InteractionResultHolder.pass(itemStack)
            } else {
                InteractionResultHolder.fail(itemStack)
            }
        //Normal click to start recording, fail if already
        } else {
            return if (isRecording(itemStack)) {
                InteractionResultHolder.fail(itemStack);
            } else {
                startRecording(itemStack)
                InteractionResultHolder.pass(itemStack)
            }
        }
    }

    override fun isFoil(pStack: ItemStack): Boolean {
        return isRecording(pStack)
    }

    override fun appendHoverText(
        pStack: ItemStack,
        pLevel: Level?,
        pTooltipComponents: MutableList<Component>,
        pIsAdvanced: TooltipFlag
    ) {
        pTooltipComponents.add(TranslatableComponent(if (isRecording(pStack)) "tooltip.ariadnesthread.recording1" else "tooltip.ariadnesthread.not_recording1"))
        pTooltipComponents.add(TranslatableComponent(if (isRecording(pStack)) "tooltip.ariadnesthread.recording2" else "tooltip.ariadnesthread.not_recording2"))
        if (!isRecording(pStack)) pTooltipComponents.add(TranslatableComponent("tooltip.ariadnesthread.clear").withStyle(ChatFormatting.RED))

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced)
    }

    private fun startRecording(itemStack: ItemStack) {
        itemStack.tag?.putBoolean("ariadnesthread.isRecording", true)
    }

    private fun stopRecording(itemStack: ItemStack) {
        itemStack.tag?.putBoolean("ariadnesthread.isRecording", false)
    }

    private fun recordPosition(player: Player, itemStack: ItemStack) {
        val currentPos : Vec3 = player.eyePosition

        //TODO: figure out how to add nbt

        //if (farEnough(currentPos)) {
        //TODO: add new location to nbt array
        // NBT Json would look like:
        // [
        //  {
        //   "x":0
        //   "y":0
        //   "z":0
        //   "dim":"minecraft:overworld
        //  },
        //  {
        //   "x":10
        //   "y":0
        //   "z":0
        //   "dim":"minecraft:overworld
        //  }
        // ]
//        }
        if (isRecording(itemStack)) {
            ModScheduler.scheduleSynchronisedTask(
                { recordPosition(player, itemStack) },
                ServerConfig.WAIT_TIME.get()
            )
        }
    }

    private fun clearHistory(itemStack: ItemStack) {
        if (itemStack.hasTag()) itemStack.tag = CompoundTag()
    }

}