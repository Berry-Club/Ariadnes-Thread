package dev.aaronhowser.mods.ariadnesthread.item

import dev.aaronhowser.mods.ariadnesthread.config.ClientConfig
import dev.aaronhowser.mods.ariadnesthread.config.ServerConfig
import dev.aaronhowser.mods.ariadnesthread.utils.Location
import dev.aaronhowser.mods.ariadnesthread.utils.Location.Companion.toLocation
import dev.aaronhowser.mods.ariadnesthread.utils.TextUtils.tooltipLiteral
import dev.aaronhowser.mods.ariadnesthread.utils.TextUtils.tooltipTranslatable
import io.netty.buffer.Unpooled
import net.minecraft.ChatFormatting
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.network.FriendlyByteBuf
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

class ThreadItem : Item(properties) {

    companion object {
        private const val IS_RECORDING = "ariadnesthread.isRecording"
        private const val HISTORY = "ariadnesthread.history"
        private const val STARTING_DIMENSION = "ariadnesthread.startingDimension"

        val properties: Properties
            get() = Properties().stacksTo(1).rarity(Rarity.UNCOMMON)

        // Recording functions

        private fun isRecording(itemStack: ItemStack): Boolean {
            val tag = itemStack.tag ?: return false

            return tag.getBoolean(IS_RECORDING)
        }

        private fun startRecording(itemStack: ItemStack, player: Player) {

            if (itemStack.tag?.getBoolean(IS_RECORDING) == true) return

            itemStack.tag = itemStack.tag ?: CompoundTag()
            itemStack.tag?.apply {
                putBoolean(IS_RECORDING, true)
                putString(STARTING_DIMENSION, player.level().dimension().location().toString())

                if (!hasHistory(itemStack)) {
                    val emptyList = ListTag()
                    put(HISTORY, emptyList)
                }
            }

            addLocation(itemStack, player)
        }

        private fun stopRecording(itemStack: ItemStack) {
            itemStack.tag = itemStack.tag ?: CompoundTag()
            itemStack.tag?.putBoolean(IS_RECORDING, false)
        }

        // History functions

        class History(private val innerList: List<Location>) : AbstractList<Location>() {

            override val size: Int
                get() = innerList.size

            override fun get(index: Int): Location {
                return innerList[index]
            }
        }

        private fun hasHistory(itemStack: ItemStack): Boolean {
            return itemStack.tag?.contains(HISTORY) ?: false
        }

        fun getHistory(itemStack: ItemStack): History {
            val list = itemStack.tag?.getList(HISTORY, 10) ?: return History(emptyList())

            return History(list.map { Location(it as CompoundTag) })
        }

        private fun clearHistory(itemStack: ItemStack) {
            itemStack.tag = itemStack.tag ?: CompoundTag()

            itemStack.tag?.apply {
                remove(IS_RECORDING)
                remove(HISTORY)
                remove(STARTING_DIMENSION)
            }
        }

        // Other functions


        private fun addLocation(itemStack: ItemStack, player: Player) {
            val height = player.boundingBox.ysize.toFloat()
            val footPos = player.position().toVector3f()

            val location = Location(
                footPos.x(),
                footPos.y() + height / 2,
                footPos.z()
            )

            addLocation(itemStack, location)
        }

        private fun addLocation(itemStack: ItemStack, location: Location) {
            val list = itemStack.tag?.getList(HISTORY, 10) ?: return

            val stopBecauseTooMany = ServerConfig.shouldCheckLocations && list.size >= ServerConfig.MAX_LOCATIONS
            val stopBecauseNbtSize = ServerConfig.shouldCheckNbt && getNbtSize(itemStack) >= ServerConfig.MAX_NBT_SIZE

            if (stopBecauseTooMany || stopBecauseNbtSize) {
                stopRecording(itemStack)
            }

            if (list.isNotEmpty()) {
                val mostRecentTag = list.lastOrNull() ?: error("List is not empty, but has no last element.")
                val mostRecentLocation = Location(mostRecentTag as CompoundTag)

                val tooClose = mostRecentLocation.closerThan(location, ServerConfig.MIN_DISTANCE)
                if (tooClose) return
            }

            list.add(location.toTag())
        }

        private fun getStartingDimension(itemStack: ItemStack): String? {
            return itemStack.tag?.getString(STARTING_DIMENSION)
        }

        fun inStartingDimension(itemStack: ItemStack, level: Level?): Boolean {
            return getStartingDimension(itemStack) == level?.dimension()?.location()?.toString()
        }

        fun getNbtSize(itemStack: ItemStack): Int {
            return getNbtSize(itemStack.tag ?: CompoundTag())
        }

        fun getNbtSize(nbt: CompoundTag): Int {
            val buffer = FriendlyByteBuf(Unpooled.buffer())
            buffer.writeNbt(nbt)
            buffer.release()
            return buffer.writerIndex()
        }
    }

    // Override functions

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

    override fun onDroppedByPlayer(item: ItemStack?, player: Player?): Boolean {
        stopRecording(item!!)
        return super.onDroppedByPlayer(item, player)
    }


    override fun inventoryTick(itemStack: ItemStack, level: Level, player: Entity, slotId: Int, isSelected: Boolean) {

        if (player !is Player) return

        if (inStartingDimension(itemStack, level)) {

            if (level.isClientSide) {
                return
            }

            val goodTick = level.gameTime % ServerConfig.CHECK_INTERVAL == 0L
            if (goodTick && isRecording(itemStack)) {
                addLocation(itemStack, player)
            }
        }

        super.inventoryTick(itemStack, level, player, slotId, isSelected)
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

        tooltipTranslatable(
            tooltipComponents,
            if (isRecording(itemStack)) "tooltip.ariadnesthread.recording_1" else "tooltip.ariadnesthread.not_recording_1"
        ) { it.withStyle(ChatFormatting.GRAY) }

        tooltipTranslatable(
            tooltipComponents,
            if (isRecording(itemStack)) "tooltip.ariadnesthread.recording_2" else "tooltip.ariadnesthread.not_recording_2"
        ) { it.withStyle(ChatFormatting.GRAY) }

        if (!isRecording(itemStack) && hasHistory(itemStack)) tooltipTranslatable(
            tooltipComponents,
            "tooltip.ariadnesthread.clear"
        ) { it.withStyle(ChatFormatting.RED) }

        if (isRecording(itemStack) && !inStartingDimension(itemStack, level)) {
            tooltipTranslatable(
                tooltipComponents,
                "tooltip.ariadnesthread.not_in_starting_dimension"
            ) { it.withStyle(ChatFormatting.RED) }

            tooltipTranslatable(
                tooltipComponents,
                "tooltip.ariadnesthread.starting_dimension",
                getStartingDimension(itemStack)
            ) { it.withStyle(ChatFormatting.RED) }
        }

        if (tooltipFlag.isAdvanced && hasHistory(itemStack)) {
            if (ServerConfig.shouldCheckLocations) {
                tooltipTranslatable(
                    tooltipComponents,
                    "tooltip.ariadnesthread.locations",
                    getHistory(itemStack).size
                ) {
                    it.withStyle(ChatFormatting.GRAY)
                }
            }

            if (ServerConfig.shouldCheckNbt) {
                tooltipLiteral(
                    tooltipComponents,
                    "NBT Size: ${getNbtSize(itemStack)} bytes"
                ) { it.withStyle(ChatFormatting.GRAY) }
            }
        }

        if (tooltipFlag.isAdvanced && ClientConfig.SHOW_NBT_TOOLTIP) tooltipLiteral(
            tooltipComponents,
            itemStack.tag.toString()
        ) { it.withStyle(ChatFormatting.GRAY) }

        super.appendHoverText(itemStack, level, tooltipComponents, tooltipFlag)
    }

}