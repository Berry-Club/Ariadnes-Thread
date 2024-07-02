package dev.aaronhowser.mods.ariadnesthread.datagen

import dev.aaronhowser.mods.ariadnesthread.AriadnesThread
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.LanguageProvider

class ModLanguageProvider(
    output: PackOutput
) : LanguageProvider(output, AriadnesThread.ID, "en_us") {

    object Item {
        const val ARIADNES_THREAD = "item.ariadnesthread.ariadnes_thread"
    }

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

        add(Item.ARIADNES_THREAD, "Ariadne's Thread")

        add(Tooltip.RECORDING_1, "☑ Recording!")
        add(Tooltip.RECORDING_2, "Stop with a shift right-click")
        add(Tooltip.NOT_RECORDING_1, "☒ Not recording!")
        add(Tooltip.NOT_RECORDING_2, "Start with a right-click")
        add(Tooltip.CLEAR, "Clear history with a sneak right-click")
        add(Tooltip.NOT_IN_STARTING_DIMENSION, "Not recording! Return to your starting dimension!")
        add(Tooltip.STARTING_DIMENSION, "Starting dimension: %s")
        add(Tooltip.LOCATIONS, "There are %s saved locations")

    }

}