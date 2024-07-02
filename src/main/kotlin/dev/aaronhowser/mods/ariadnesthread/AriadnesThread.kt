package dev.aaronhowser.mods.ariadnesthread

import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(AriadnesThread.ID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
object AriadnesThread {
    const val ID = "ariadnesthread"

    // the logger for our mod
    val LOGGER: Logger = LogManager.getLogger(ID)

    init {
        LOGGER.log(Level.INFO, "Hello world!")

    }
}