package com.aaronhowser1.ariadnesthread.client

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.*
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.world.phys.Vec3
import net.minecraftforge.client.event.RenderLevelStageEvent
import org.lwjgl.opengl.GL11
import thedarkcolour.kotlinforforge.forge.vectorutil.component1
import thedarkcolour.kotlinforforge.forge.vectorutil.component2
import thedarkcolour.kotlinforforge.forge.vectorutil.component3

object ModRenderer {

    private var vertexBuffer: VertexBuffer? = null

    fun renderLines(event: RenderLevelStageEvent) {
        if (LineSegment.lineSegments.isEmpty()) return

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

        for (lineSegment in LineSegment.lineSegments) {
            val (startX, startY, startZ) = lineSegment.start.vec3
            val (endX, endY, endZ) = lineSegment.end.vec3

            val (sizeX, sizeY, sizeZ) = lineSegment.start.vec3.subtract(lineSegment.end.vec3)

            buffer.vertex(startX, startY, startZ).color(1).endVertex()
            buffer.vertex(startX + sizeX, startY + sizeY, startZ).color(1).endVertex()
            buffer.vertex(startX + sizeX, startY + sizeY, startZ + sizeZ).color(1).endVertex()
            buffer.vertex(startX, startY + sizeY, startZ + sizeZ).color(1).endVertex()
            buffer.vertex(startX, startY + sizeY, startZ).color(1).endVertex()
        }

        vertexBuffer!!.apply {
            bind()
            upload(buffer.end())
        }
        VertexBuffer.unbind()

    }

    private fun vBuffNotNull(event: RenderLevelStageEvent) {

        val view: Vec3 = Minecraft.getInstance().entityRenderDispatcher.camera.position

        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        GL11.glDisable(GL11.GL_DEPTH_TEST)

        RenderSystem.setShader(GameRenderer::getPositionColorShader)

        val matrix: PoseStack = event.poseStack

        matrix.pushPose()
        matrix.translate(-view.x, -view.y, -view.z)

        vertexBuffer!!.apply {
            bind()
            drawWithShader(
                matrix.last().pose(),
                event.projectionMatrix.copy(),
                RenderSystem.getShader()!!
            )
        }

        VertexBuffer.unbind()
        matrix.popPose()

        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glDisable(GL11.GL_LINE_SMOOTH)
    }

}