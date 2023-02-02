package com.aaronhowser1.ariadnesthread.item;

import com.aaronhowser1.ariadnesthread.config.ServerConfigs;
import com.aaronhowser1.ariadnesthread.utils.ModScheduler;
import com.aaronhowser1.ariadnesthread.utils.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ThreadItem extends Item {


    public ThreadItem() {
        super(new Item.Properties().stacksTo(1));
    }

//    @Override
//    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
//
//        ItemStack thisItemStack = pPlayer.getItemInHand(pUsedHand);
//        if (pPlayer.isShiftKeyDown()) {
//            if (isRecording) {
//                stopRecording();
//            } else {
//                return InteractionResultHolder.fail(thisItemStack);
//            }
//        } else {
//            if (isRecording) {
//                return InteractionResultHolder.fail(thisItemStack);
//            } else {
//                startRecording(pPlayer);
//            }
//        }
//        return InteractionResultHolder.success(thisItemStack);
//    }
}
