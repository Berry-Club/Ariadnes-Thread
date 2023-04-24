package com.aaronhowser1.ariadnesthread

import com.aaronhowser1.ariadnesthread.config.Configs
import com.mojang.logging.LogUtils
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import org.slf4j.Logger

@Mod(AriadnesThread.MOD_ID)
object AriadnesThread {

    const val MOD_ID = "ariadnesthread"
    val LOGGER: Logger = LogUtils.getLogger()

    init {

        LOGGER.log()

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Configs.CLIENT_SPEC)
    }

}