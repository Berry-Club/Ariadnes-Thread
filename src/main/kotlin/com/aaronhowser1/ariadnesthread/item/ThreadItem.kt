package com.aaronhowser1.ariadnesthread.item

import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item

class ThreadItem(
    properties : Properties = Properties()
        .tab(CreativeModeTab.TAB_MISC)
        .stacksTo(1)
) : Item(properties) {

}