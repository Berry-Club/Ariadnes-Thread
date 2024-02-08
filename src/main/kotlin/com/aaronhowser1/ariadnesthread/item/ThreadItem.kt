package com.aaronhowser1.ariadnesthread.item

import com.aaronhowser1.ariadnesthread.client.LineSegment
import com.aaronhowser1.ariadnesthread.client.ModRenderer
import com.aaronhowser1.ariadnesthread.config.ServerConfig
import com.aaronhowser1.ariadnesthread.utils.Location
import com.aaronhowser1.ariadnesthread.utils.Location.Companion.toLocation
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
        const val STARTING_DIMENSION = "ariadnesthread.startingDimension"
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

        if (itemStack.tag?.contains(IS_RECORDING) == true) return

        itemStack.tag = itemStack.tag ?: CompoundTag()
        itemStack.tag?.apply {
            putBoolean(IS_RECORDING, true)
            putString(STARTING_DIMENSION, player.level.dimension().location().toString())

            val hasHistory = itemStack.tag?.contains(HISTORY) ?: false
            if (!hasHistory) {
                val emptyList = ListTag()
                put(HISTORY, emptyList)
            }
        }

        addLocation(itemStack, player.eyePosition.toLocation())
    }

    override fun onDroppedByPlayer(item: ItemStack?, player: Player?): Boolean {
        stopRecording(item!!)
        return super.onDroppedByPlayer(item, player)
    }

    private fun getStartingDimension(itemStack: ItemStack): String {
        return itemStack.tag?.getString(STARTING_DIMENSION) ?: error("ItemStack has no starting dimension.")
    }

    private fun inStartingDimension(itemStack: ItemStack, level: Level?): Boolean {
        return getStartingDimension(itemStack) == level?.dimension()?.location()?.toString()
    }

    override fun inventoryTick(itemStack: ItemStack, level: Level, entity: Entity, slotId: Int, isSelected: Boolean) {

        if (entity !is Player) return

        if (level.isClientSide) {
            showHistory(itemStack)
            return
        }

        if (level.gameTime % ServerConfig.WAIT_TIME != 0L) return
        if (!isRecording(itemStack)) return
        if (!inStartingDimension(itemStack, level)) {
            return
        }

        addLocation(itemStack, entity.eyePosition.toLocation())

        super.inventoryTick(itemStack, level, entity, slotId, isSelected)
    }

    private fun addLocation(itemStack: ItemStack, location: Location) {
        val list = itemStack.tag?.getList(HISTORY, 10) ?: return

        if (list.isNotEmpty()) {
            val mostRecentTag = list.lastOrNull() ?: error("List is not empty, but has no last element.")
            val mostRecentLocation = Location(mostRecentTag as CompoundTag)

            val tooClose = mostRecentLocation.closerThan(location, ServerConfig.MIN_DISTANCE)
            if (tooClose) return
        }

        list.add(location.toTag())
    }

    private fun stopRecording(itemStack: ItemStack) {
        itemStack.tag = itemStack.tag ?: CompoundTag()
        itemStack.tag?.putBoolean(IS_RECORDING, false)
    }

    private fun getHistory(itemStack: ItemStack): List<Location> {
        val list = itemStack.tag?.getList(HISTORY, 10) ?: return emptyList()

        return list.map { Location(it as CompoundTag) }
    }

    private fun getLineSegments(itemStack: ItemStack): List<LineSegment> {
        val history = getHistory(itemStack)
        val mutable = mutableListOf<LineSegment>()

        for (i in 0 until history.size - 1) {

            val first = history[i]
            val second = history[i + 1]

            mutable.add(LineSegment(first, second))
        }

        return mutable
    }

    private fun showHistory(itemStack: ItemStack) {

        val lines = getLineSegments(itemStack)
        LineSegment.lineSegments = lines
        ModRenderer.reloadNeeded = true

    }

    private fun hasHistory(itemStack: ItemStack): Boolean {
        return itemStack.tag?.contains(HISTORY) ?: false
    }

    private fun clearHistory(itemStack: ItemStack) {
        itemStack.tag = itemStack.tag ?: CompoundTag()

        itemStack.tag?.apply {
            remove(IS_RECORDING)
            remove(HISTORY)
            remove(STARTING_DIMENSION)
        }
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

        tooltipTranslatable(if (isRecording(itemStack)) "tooltip.ariadnesthread.recording_1" else "tooltip.ariadnesthread.not_recording_1")
        tooltipTranslatable(if (isRecording(itemStack)) "tooltip.ariadnesthread.recording_2" else "tooltip.ariadnesthread.not_recording_2")

        if (!isRecording(itemStack) && hasHistory(itemStack)) tooltipTranslatable("tooltip.ariadnesthread.clear") {
            it.withStyle(ChatFormatting.RED)
        }

        if (isRecording(itemStack) && !inStartingDimension(itemStack, level)) {
            tooltipTranslatable("tooltip.ariadnesthread.not_in_starting_dimension") {
                it.withStyle(ChatFormatting.RED)
            }
        }

        tooltipComponents.add(
            Component.literal(itemStack.tag.toString())
        )

        super.appendHoverText(itemStack, level, tooltipComponents, tooltipFlag)
    }

}