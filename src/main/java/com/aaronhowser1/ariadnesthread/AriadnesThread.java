package com.aaronhowser1.ariadnesthread;

import com.aaronhowser1.ariadnesthread.config.ClientConfigs;
import com.aaronhowser1.ariadnesthread.config.ServerConfigs;
import com.aaronhowser1.ariadnesthread.item.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(AriadnesThread.MOD_ID)
public class AriadnesThread
{

    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "ariadnesthread";

    public AriadnesThread() {
        // Register the setup method for modloading
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(eventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfigs.SPEC, "ariadnesthread-client.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfigs.SPEC, "ariadnesthread-server.toml");
    }
}
