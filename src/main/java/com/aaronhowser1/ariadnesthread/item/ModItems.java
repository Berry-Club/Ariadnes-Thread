package com.aaronhowser1.ariadnesthread.item;

import com.aaronhowser1.ariadnesthread.AriadnesThread;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AriadnesThread.MOD_ID);

    public static final RegistryObject<Item> THREAD = ITEMS.register("thread", ThreadItem::new);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
