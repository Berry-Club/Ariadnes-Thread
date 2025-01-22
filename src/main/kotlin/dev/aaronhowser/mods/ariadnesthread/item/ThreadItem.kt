package dev.aaronhowser.mods.ariadnesthread.item

import dev.aaronhowser.mods.ariadnesthread.config.ServerConfig
import dev.aaronhowser.mods.ariadnesthread.datagen.ModLanguageProvider
import dev.aaronhowser.mods.ariadnesthread.item.component.HistoryItemComponent
import dev.aaronhowser.mods.ariadnesthread.registry.ModDataComponents
import dev.aaronhowser.mods.ariadnesthread.util.ClientUtil
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
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
        .component(ModDataComponents.IS_RECORDING, false)
        .component(ModDataComponents.HISTORY, HistoryItemComponent())
) {

    companion object {

        fun isRecording(stack: ItemStack): Boolean =
            stack.get(ModDataComponents.IS_RECORDING) ?: false

        fun stopRecording(stack: ItemStack) {
            stack.set(ModDataComponents.IS_RECORDING, false)
        }

        fun startRecording(stack: ItemStack, player: Player) {
            stack.set(ModDataComponents.IS_RECORDING, true)

            if (getStartingDimension(stack) == null) {
                setStartingDimension(stack, player.level().dimension())
            }
        }

        fun setHistory(stack: ItemStack, history: List<BlockPos>) {
            stack.set(ModDataComponents.HISTORY, HistoryItemComponent(history))
        }

        fun getHistory(stack: ItemStack): HistoryItemComponent {
            return stack.get(ModDataComponents.HISTORY) ?: HistoryItemComponent()
        }

        fun hasHistory(stack: ItemStack): Boolean = getHistory(stack).locations.isNotEmpty()

        fun clearHistory(stack: ItemStack) {
            setHistory(stack, emptyList())
        }

        fun addBlockPos(stack: ItemStack, location: BlockPos) {
            val history = getHistory(stack)
            val canAdd = history.canAddBlockPos(location)

            if (!canAdd) return

            val list = history.locations.toMutableList()
            list.add(location)
            setHistory(stack, list)
        }

        fun getStartingDimension(stack: ItemStack): ResourceKey<Level>? {
            return stack.get(ModDataComponents.STARTING_DIMENSION)
        }

        fun inStartingDimension(stack: ItemStack, level: Level): Boolean {
            val startingDimension = getStartingDimension(stack) ?: return false

            return level.dimension() == startingDimension
        }

        fun setStartingDimension(stack: ItemStack, dimension: ResourceKey<Level>) {
            stack.set(ModDataComponents.STARTING_DIMENSION, dimension)
        }

    }

    override fun use(
        pLevel: Level,
        pPlayer: Player,
        pUsedHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {
        if (pLevel.isClientSide) return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand))

        val itemStack = pPlayer.getItemInHand(pUsedHand)

        val isSneaking = pPlayer.isSecondaryUseActive
        val isRecording = isRecording(itemStack)

        // Start recording by regular-clicking while not recording
        // Clear history by sneak-clicking while not recording
        // Stop recording by sneak-clicking while recording
        if (!isRecording) {
            if (isSneaking) {
                clearHistory(itemStack)
                return InteractionResultHolder.success(itemStack)
            }

            startRecording(itemStack, pPlayer)
            return InteractionResultHolder.success(itemStack)
        }

        if (isSneaking) {
            stopRecording(itemStack)
            return InteractionResultHolder.success(itemStack)
        }

        return InteractionResultHolder.pass(itemStack)
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
        if (pLevel.isClientSide) return
        if (player !is Player) return
        if (!isRecording(pStack)) return
        if (!inStartingDimension(pStack, player.level())) return

        if (pLevel.gameTime % ServerConfig.waitTime.get() != 0L) return

        val blockPos = player.blockPosition().atY(player.eyeY.toInt())

        addBlockPos(pStack, blockPos)
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

        val startingDimension = getStartingDimension(pStack)
        if (startingDimension != null && !inStartingDimension(pStack, ClientUtil.localPlayer!!.level())) {
            pTooltipComponents.add(
                Component.translatable(
                    ModLanguageProvider.Tooltip.NOT_IN_STARTING_DIMENSION
                ).withStyle(ChatFormatting.RED)
            )

            pTooltipComponents.add(
                Component.translatable(
                    ModLanguageProvider.Tooltip.STARTING_DIMENSION,
                    startingDimension.toString()
                ).withStyle(ChatFormatting.RED)
            )
        }

        if (pTooltipFlag.isAdvanced) {
            val history = getHistory(pStack).locations
            if (history.isNotEmpty()) {
                pTooltipComponents.add(
                    Component.translatable(
                        ModLanguageProvider.Tooltip.LOCATIONS,
                        history.size.toString()
                    ).withStyle(ChatFormatting.GRAY)
                )
            }
        }

    }

}