package com.aaronhowser1.ariadnesthread.client

import com.aaronhowser1.ariadnesthread.utils.Location
import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.Tesselator
import com.mojang.blaze3d.vertex.VertexBuffer
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraftforge.client.event.RenderLevelStageEvent
import thedarkcolour.kotlinforforge.forge.vectorutil.component1
import thedarkcolour.kotlinforforge.forge.vectorutil.component2
import thedarkcolour.kotlinforforge.forge.vectorutil.component3

object ModRenderer {

    var locations: List<Location> = emptyList()

    private var vertexBuffer: VertexBuffer? = null

    fun renderLine(event: RenderLevelStageEvent) {
        if (locations.isEmpty()) return

        if (vertexBuffer == null) {
            vBuffNull(event)
        } else {
            vBuffNotNull(event)
        }
    }

    private fun vBuffNull(event: RenderLevelStageEvent) {
        vertexBuffer = VertexBuffer()

        val tesselator: Tesselator = Tesselator.getInstance()
        val buffer: BufferBuilder = tesselator.builder

        var opacity = 1F

        buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR)

        for (location in locations) {
            val (x, y, z) = location.vec3
        }

    }

    private fun vBuffNotNull(event: RenderLevelStageEvent) {
    }

}