package com.aaronhowser1.ariadnesthread.item;

import net.minecraft.world.item.Item;

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
