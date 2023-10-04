package com.aaronhowser1.ariadnesthread.client

import com.aaronhowser1.ariadnesthread.utils.Location
import net.minecraft.world.level.Level
import net.minecraftforge.client.event.RenderLevelStageEvent

data class LineSegment(
    val start: Location,
    val end: Location,
    val level: Level,
    val colorHex: String
) {
    // TODO:
    //  - Render a line between these two points
    //  - The line should be visible through blocks
    //  - Lerp the color based on its position in the list
    //  - If the distance between is beyond the configured teleport distance, either don't render at all or render with like a spiral or something

    fun render(event: RenderLevelStageEvent) {

    }

    init {
        lineSegments.add(this)
    }

    companion object {
        val lineSegments = mutableListOf<LineSegment>()
    }

}