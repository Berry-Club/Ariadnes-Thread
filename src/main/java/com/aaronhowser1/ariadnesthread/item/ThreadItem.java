package com.aaronhowser1.ariadnesthread.item;

import com.aaronhowser1.ariadnesthread.config.ServerConfigs;
import com.aaronhowser1.ariadnesthread.utils.ModScheduler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
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
    private ArrayList<Vec3> positions;

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
        recordPosition(player);
    }

    private void stopRecording() {
        this.isRecording = false;
    }

    private void recordPosition(Player player) {
        Vec3 currentPos = new Vec3(player.getX(), player.getY(), player.getZ());

        if (farEnough(currentPos)) {
            positions.add(currentPos);
        }

        if (this.isRecording) {
            ModScheduler.scheduleSynchronisedTask(() -> recordPosition(player), ServerConfigs.WAIT_TIME.get());
        }
    }

    private boolean farEnough(Vec3 position) {

        if (positions.isEmpty()) {
            return true;
        }

        Vec3 mostRecent = positions.get(positions.size()-1);

        double distance = position.distanceTo(mostRecent);

        return distance > ServerConfigs.MIN_DISTANCE.get();

    }
}
