package com.aaronhowser1.ariadnesthread.client

import com.aaronhowser1.ariadnesthread.utils.Location
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

    private var reloadNeeded = false

    var histories: List<List<Location>> = emptyList()
        set(value) {
            field = value
            reloadNeeded = true
        }

    fun renderLines(event: RenderLevelStageEvent) {
        if (!reloadNeeded && histories.isEmpty()) return

        if (vertexBuffer == null || reloadNeeded) refresh()

        render(event)
    }

    @Suppress("UnnecessaryVariable")
    private fun refresh() {
        vertexBuffer = VertexBuffer()
        reloadNeeded = false

        val tesselator: Tesselator = Tesselator.getInstance()
        val buffer: BufferBuilder = tesselator.builder

        buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR)

        for (history in histories) {
            for (i in 0 until history.size - 1) {
                val loc1 = history[i]
                val loc2 = history[i + 1]

                val (x1, y1, z1) = loc1.vec3
                val (x2, y2, z2) = loc2.vec3

                val percentDone = i.toFloat() / history.size

                val red = 1f - percentDone
                val green = percentDone
                val blue = 0f

                val opacity = 1f

                buffer.vertex(x1, y1, z1).color(red, green, blue, opacity).endVertex()
                buffer.vertex(x2, y2, z2).color(red, green, blue, opacity).endVertex()
            }
        }

        vertexBuffer!!.apply {
            bind()
            upload(buffer.end())
        }
        VertexBuffer.unbind()

    }

    private fun render(event: RenderLevelStageEvent) {

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