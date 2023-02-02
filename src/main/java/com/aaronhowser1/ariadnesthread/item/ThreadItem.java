package com.aaronhowser1.ariadnesthread.item;

import com.aaronhowser1.ariadnesthread.config.ClientConfigs;
import com.aaronhowser1.ariadnesthread.config.ServerConfigs;
import com.mojang.math.Vector3d;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ThreadItem extends Item {

    private Boolean isRecording;
    private ArrayList<BlockPos> positions;

    public ThreadItem() {
        super(new Item.Properties().stacksTo(1));
        this.isRecording = false;
        this.positions = new ArrayList<>();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        ItemStack thisIS = pPlayer.getItemInHand(pUsedHand);
        if (pPlayer.isShiftKeyDown()) {
            if (isRecording) {
                stopRecording();
            } else {
                return InteractionResultHolder.fail(thisIS);
            }
        } else {
            if (isRecording) {
                return InteractionResultHolder.fail(thisIS);
            } else {
                startRecording(pPlayer);
            }
        }
        return InteractionResultHolder.success(thisIS);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return this.isRecording;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {

        pTooltipComponents.add(new TextComponent(isRecording ? "Recording!" : "Not recording!"));
        pTooltipComponents.add(new TextComponent(isRecording ? "Stop with a shift right-click" : "Start with a right-click"));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    private void startRecording(Player player) {
        this.isRecording = true;
    }

    private void stopRecording() {
        this.isRecording = false;
    }

    private boolean farEnough(Vec3 position) {

        if (positions.isEmpty()) {
            return true;
        }

        BlockPos mostRecentPos = positions.get(positions.size()-1);
        Vec3 mostRecentVec = new Vec3(mostRecentPos.getX(),mostRecentPos.getY(), mostRecentPos.getZ());

        return position.distanceTo(mostRecentVec) <= ServerConfigs.MIN_DISTANCE.get();
    }
}
