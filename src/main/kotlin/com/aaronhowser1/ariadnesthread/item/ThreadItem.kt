package com.aaronhowser1.ariadnesthread.item

import com.aaronhowser1.ariadnesthread.config.ServerConfig
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
                if (!pLevel.isClientSide) stopRecording(itemStack)
                InteractionResultHolder.pass(itemStack)
            } else {
                InteractionResultHolder.fail(itemStack)
            }
            //Normal click to start recording, fail if already
        } else {
            return if (isRecording(itemStack)) {
                InteractionResultHolder.fail(itemStack)
            } else {
                if (!pLevel.isClientSide) startRecording(pPlayer, itemStack)
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

        fun addTooltip(translatable: String, block: (MutableComponent) -> Unit = {}) {
            pTooltipComponents.add(
                Component.translatable(translatable).apply {
                    block(this)
                }
            )
        }

        addTooltip(if (isRecording(pStack)) "tooltip.ariadnesthread.recording1" else "tooltip.ariadnesthread.not_recording1")
        addTooltip(if (isRecording(pStack)) "tooltip.ariadnesthread.recording2" else "tooltip.ariadnesthread.not_recording2")

        if (!isRecording(pStack)) {
            addTooltip("tooltip.ariadnesthread.clear") {
                it.withStyle(ChatFormatting.RED)
            }
        }

        pTooltipComponents.add(Component.literal(pStack.tag.toString()))

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced)
    }

    private fun startRecording(player: Player, itemStack: ItemStack) {

        if (!itemStack.hasTag()) itemStack.tag = CompoundTag()

        itemStack.tag?.putBoolean("ariadnesthread.isRecording", true)


        recordPosition(player, itemStack)
    }

    private fun stopRecording(itemStack: ItemStack) {
        println("Stopped recording")
        itemStack.tag?.putBoolean("ariadnesthread.isRecording", false)
        itemStack.tag?.remove("ariadnesthread.startingDimension")
    }

    private fun recordPosition(player: Player, itemStack: ItemStack) {

        if (!isRecording(itemStack)) return

        val dimension = player.level.dimension()
        val dimensionString = dimension.location().toString()

        var startingDimension = itemStack.tag?.getString("ariadnesthread.startingDimension")

        if (startingDimension.isNullOrBlank()) {
            println("Setting starting dimension to $dimensionString")
            itemStack.tag?.putString("ariadnesthread.startingDimension", dimensionString)
            startingDimension = dimensionString
        }

        if (startingDimension != dimensionString) {
            println("Changed dimensions, stopping")
            itemStack.tag?.putBoolean("ariadnesthread.isRecording", false)
            itemStack.tag?.remove("ariadnesthread.startingDimension")
            return
        }

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
                ServerConfig.WAIT_TIME
            )
        }
    }

    private fun clearHistory(itemStack: ItemStack) {
        if (itemStack.hasTag()) itemStack.tag = CompoundTag()
    }

}