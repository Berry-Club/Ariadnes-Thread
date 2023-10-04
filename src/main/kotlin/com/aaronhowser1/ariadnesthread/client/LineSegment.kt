package com.aaronhowser1.ariadnesthread.client

import com.aaronhowser1.ariadnesthread.utils.Location
import net.minecraft.world.level.Level
import net.minecraftforge.client.event.RenderLevelStageEvent
import org.lwjgl.opengl.GL11

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

        GL11.glPushMatrix()
        GL11.glLineWidth(2f)
        GL11.glColor3f(1f, 0f, 0f)
        GL11.glBegin(GL11.GL_LINES)

        GL11.glVertex3d(start.x.toDouble(), start.y.toDouble(), start.z.toDouble())
        GL11.glVertex3d(end.x.toDouble(), end.y.toDouble(), end.z.toDouble())

        GL11.glEnd()
        GL11.glPopMatrix()

    }

    init {
        lineSegments.add(this)
    }

    companion object {
        val lineSegments = mutableListOf<LineSegment>()
    }

}