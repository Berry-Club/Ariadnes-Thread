package com.aaronhowser1.ariadnesthread.item;

import com.aaronhowser1.ariadnesthread.config.ServerConfigs;
import com.aaronhowser1.ariadnesthread.utils.ModScheduler;
import com.aaronhowser1.ariadnesthread.utils.Position;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ThreadItem extends Item {

    private Boolean isRecording;
//    private final ArrayList<Position> positions;

    public ThreadItem() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1));
        this.isRecording = false;
//        this.positions = new ArrayList<>();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        ItemStack thisItemStack = pPlayer.getItemInHand(pUsedHand);
        if (pPlayer.isShiftKeyDown()) {
            if (isRecording) {
                stopRecording();
            } else {
                return InteractionResultHolder.fail(thisItemStack);
            }
        } else {
            if (isRecording) {
                return InteractionResultHolder.fail(thisItemStack);
            } else {
                startRecording(pPlayer, thisItemStack);
            }
        }
        return InteractionResultHolder.success(thisItemStack);
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

    private void startRecording(Player player, ItemStack itemStack) {
        this.isRecording = true;
        recordPosition(player, itemStack);
    }

    private void stopRecording() {
        this.isRecording = false;
    }

    private void recordPosition(Player player, ItemStack itemStack) {
        Position currentPos = new Position((int)player.getX(), (int)player.getY(), (int)player.getZ(), player.getLevel().dimension());

        //TODO: figure out how to add nbt

//        if (farEnough(currentPos)) {
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

        if (this.isRecording) {
            ModScheduler.scheduleSynchronisedTask(() -> recordPosition(player, itemStack), ServerConfigs.WAIT_TIME.get());
        }
    }

//    private boolean farEnough(Position position) {
//        if (positions.isEmpty()) return true;
//
//        Position mostRecent = positions.get(positions.size()-1);
//
//        if (position.getDimension() != mostRecent.getDimension()) return true;
//
//        double distance = position.toVec3().distanceTo(mostRecent.toVec3());
//        return distance > ServerConfigs.MIN_DISTANCE.get();
//    }

    private void getNBT(ItemStack itemStack) {

    }

}
