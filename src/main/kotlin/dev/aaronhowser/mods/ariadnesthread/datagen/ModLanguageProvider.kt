package dev.aaronhowser.mods.ariadnesthread.datagen

import dev.aaronhowser.mods.ariadnesthread.AriadnesThread
import dev.aaronhowser.mods.ariadnesthread.config.ClientConfig
import dev.aaronhowser.mods.ariadnesthread.config.ServerConfig
import dev.aaronhowser.mods.ariadnesthread.config.StartupConfig
import dev.aaronhowser.mods.ariadnesthread.registry.ModItems
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue
import net.neoforged.neoforge.common.data.LanguageProvider

class ModLanguageProvider(
    output: PackOutput
) : LanguageProvider(output, AriadnesThread.ID, "en_us") {

    object Tooltip {
        const val RECORDING_1 = "tooltip.ariadnesthread.recording_1"
        const val RECORDING_2 = "tooltip.ariadnesthread.recording_2"
        const val NOT_RECORDING_1 = "tooltip.ariadnesthread.not_recording_1"
        const val NOT_RECORDING_2 = "tooltip.ariadnesthread.not_recording_2"
        const val CLEAR = "tooltip.ariadnesthread.clear"
        const val NOT_IN_STARTING_DIMENSION = "tooltip.ariadnesthread.not_in_starting_dimension"
        const val STARTING_DIMENSION = "tooltip.ariadnesthread.starting_dimension"
        const val LOCATIONS = "tooltip.ariadnesthread.locations"
    }

    override fun addTranslations() {

        addItem(ModItems.ARIADNES_THREAD, "Ariadne's Thread")

        add(Tooltip.RECORDING_1, "§c■§r Recording!")
        add(Tooltip.RECORDING_2, "Stop with a shift right-click")
        add(Tooltip.NOT_RECORDING_1, "§c⏸§r Not recording!")
        add(Tooltip.NOT_RECORDING_2, "Start with a right-click")
        add(Tooltip.CLEAR, "Clear history with a sneak right-click")
        add(Tooltip.NOT_IN_STARTING_DIMENSION, "Not recording! Return to your starting dimension!")
        add(Tooltip.STARTING_DIMENSION, "Starting dimension: %s")
        add(Tooltip.LOCATIONS, "There are %s saved locations")

        addConfig(ClientConfig.LINE_ALPHA, "Line Opacity")

        addConfig(ClientConfig.TELEPORT_DISTANCE, "Distance for Teleport")
        addConfig(ClientConfig.LINE_START_RED, "Line Start Red")
        addConfig(ClientConfig.LINE_START_GREEN, "Line Start Green")
        addConfig(ClientConfig.LINE_START_BLUE, "Line Start Blue")
        addConfig(ClientConfig.LINE_END_RED, "Line End Red")
        addConfig(ClientConfig.LINE_END_GREEN, "Line End Green")
        addConfig(ClientConfig.LINE_END_BLUE, "Line End Blue")

        addConfig(ServerConfig.CHECK_INTERVAL, "Check Interval")
        addConfig(ServerConfig.MIN_DISTANCE, "Minimum Distance")
        addConfig(ServerConfig.MAX_LOCATIONS, "Max Locations")

        addConfig(StartupConfig.LINE_THICKNESS, "Line Thickness")
    }

    private fun addConfig(config: ConfigValue<*>, desc: String) {
        val configString = StringBuilder()
            .append(AriadnesThread.ID)
            .append(".configuration.")
            .append(config.path.last())
            .toString()

        add(configString, desc)
    }

}