package dev.aaronhowser.ariadnesthread

import dev.aaronhowser.ariadnesthread.config.ClientConfig
import dev.aaronhowser.ariadnesthread.config.ServerConfig
import dev.aaronhowser.ariadnesthread.item.ModItems
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.runForDist

@Mod(AriadnesThread.MOD_ID)
object AriadnesThread {

    const val MOD_ID = "ariadnesthread"
    val LOGGER: Logger = LogManager.getLogger(MOD_ID)

    init {
        LOGGER.log(Level.INFO, "Ariadne's Thread loaded!")

        ModItems.REGISTRY.register(MOD_BUS)

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC, "ariadnesthread-client.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC, "ariadnesthread-server.toml");

        val obj = runForDist(
            clientTarget = {
                MOD_BUS.addListener(AriadnesThread::onClientSetup)
                Minecraft.getInstance()
            },
            serverTarget = {
                MOD_BUS.addListener(AriadnesThread::onServerSetup)
                "test"
            })

        println(obj)
    }

    private fun onClientSetup(event: FMLClientSetupEvent) {
    }

    private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
    }

}