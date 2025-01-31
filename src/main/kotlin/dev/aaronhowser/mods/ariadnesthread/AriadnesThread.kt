package dev.aaronhowser.mods.ariadnesthread

import dev.aaronhowser.mods.ariadnesthread.config.ClientConfig
import dev.aaronhowser.mods.ariadnesthread.config.ServerConfig
import dev.aaronhowser.mods.ariadnesthread.config.StartupConfig
import dev.aaronhowser.mods.ariadnesthread.registry.ModRegistries
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.neoforge.client.gui.ConfigurationScreen
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.runWhenOn

@Mod(AriadnesThread.ID)
class AriadnesThread(
    modContainer: ModContainer
) {

    companion object {
        const val ID = "ariadnesthread"
        val LOGGER: Logger = LogManager.getLogger(ID)
    }

    init {
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.CONFIG_SPEC)
        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.CONFIG_SPEC)
        modContainer.registerConfig(ModConfig.Type.STARTUP, StartupConfig.CONFIG_SPEC)

        runWhenOn(Dist.CLIENT) {
            val screenFactory = IConfigScreenFactory { container, screen -> ConfigurationScreen(container, screen) }
            modContainer.registerExtensionPoint(IConfigScreenFactory::class.java, screenFactory)
        }

        ModRegistries.register(MOD_BUS)
    }
}