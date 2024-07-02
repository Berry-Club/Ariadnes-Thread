package dev.aaronhowser.mods.ariadnesthread

import dev.aaronhowser.mods.ariadnesthread.config.ClientConfig
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

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
    }
}