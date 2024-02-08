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
            vBuffNull()
        } else {
            vBuffNotNull(event)
        }
    }

    private fun vBuffNull() {
        vertexBuffer = VertexBuffer()

        val tesselator: Tesselator = Tesselator.getInstance()
        val buffer: BufferBuilder = tesselator.builder

        buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR)

        for (lineSegment in LineSegment.lineSegments) {
            val (x, y, z) = lineSegment.start.vec3
            val (x1, y1, z1) = lineSegment.end.vec3

            val sizeX = x1 - x
            val sizeY = y1 - y
            val sizeZ = z1 - z

            val red = 1f
            val green = 1f
            val blue = 1f
            val opacity = 1f

            buffer.vertex(x, y + sizeY, z).color(red, green, blue, opacity).endVertex();
            buffer.vertex(x + sizeX, y + sizeY, z).color(red, green, blue, opacity).endVertex();
            buffer.vertex(x + sizeX, y + sizeY, z).color(red, green, blue, opacity).endVertex();
            buffer.vertex(x + sizeX, y + sizeY, z + sizeZ).color(red, green, blue, opacity).endVertex();
            buffer.vertex(x + sizeX, y + sizeY, z + sizeZ).color(red, green, blue, opacity).endVertex();
            buffer.vertex(x, y + sizeY, z + sizeZ).color(red, green, blue, opacity).endVertex();
            buffer.vertex(x, y + sizeY, z + sizeZ).color(red, green, blue, opacity).endVertex();
            buffer.vertex(x, y + sizeY, z).color(red, green, blue, opacity).endVertex();

            // BOTTOM
            buffer.vertex(x + sizeX, y, z).color(red, green, blue, opacity).endVertex();
            buffer.vertex(x + sizeX, y, z + sizeZ).color(red, green, blue, opacity).endVertex();
            buffer.vertex(x + sizeX, y, z + sizeZ).color(red, green, blue, opacity).endVertex();
            buffer.vertex(x, y, z + sizeZ).color(red, green, blue, opacity).endVertex();
            buffer.vertex(x, y, z + sizeZ).color(red, green, blue, opacity).endVertex();
            buffer.vertex(x, y, z).color(red, green, blue, opacity).endVertex();
            buffer.vertex(x, y, z).color(red, green, blue, opacity).endVertex();
            buffer.vertex(x + sizeZ, y, z).color(red, green, blue, opacity).endVertex();

            // Edge 1
            buffer.vertex(x + sizeX, y, z + sizeZ).color(red, green, blue, opacity).endVertex();
            buffer.vertex(x + sizeX, y + sizeY, z + sizeZ).color(red, green, blue, opacity).endVertex();

            // Edge 2
            buffer.vertex(x + sizeX, y, z).color(red, green, blue, opacity).endVertex();
            buffer.vertex(x + sizeX, y + sizeY, z).color(red, green, blue, opacity).endVertex();

            // Edge 3
            buffer.vertex(x, y, z + sizeZ).color(red, green, blue, opacity).endVertex();
            buffer.vertex(x, y + sizeY, z + sizeZ).color(red, green, blue, opacity).endVertex();

            // Edge 4
            buffer.vertex(x, y, z).color(red, green, blue, opacity).endVertex();
            buffer.vertex(x, y + sizeY, z).color(red, green, blue, opacity).endVertex();
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