package com.aaronhowser1.ariadnesthread.client

import com.aaronhowser1.ariadnesthread.config.ClientConfig
import com.aaronhowser1.ariadnesthread.utils.ColorUtils
import com.aaronhowser1.ariadnesthread.utils.ColorUtils.getARGB
import com.aaronhowser1.ariadnesthread.utils.ColorUtils.getBlue
import com.aaronhowser1.ariadnesthread.utils.ColorUtils.getGreen
import com.aaronhowser1.ariadnesthread.utils.ColorUtils.getRed
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

    var locations: List<Location> = emptyList()
        set(value) {
            field = value
            reloadNeeded = true
        }

    fun renderLines(event: RenderLevelStageEvent) {
        if (!reloadNeeded && locations.isEmpty()) return

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

        val opacity = ClientConfig.THREAD_TRANSPARENCY.toFloat()
        val startColor = ClientConfig.COLOR_START
        val endColor = ClientConfig.COLOR_END

        for (i in 0 until locations.size - 1) {
            val loc1 = locations[i]
            val loc2 = locations[i + 1]

            val (x1, y1, z1) = loc1.vec3
            val (x2, y2, z2) = loc2.vec3

            val percentDone = i.toFloat() / locations.size

            val color = ColorUtils.interpolateColor(startColor, endColor, percentDone)

            val (alpha, red, green, blue) = color.getARGB()

            buffer.vertex(x1, y1, z1).color(red, green, blue, alpha).endVertex()
            buffer.vertex(x2, y2, z2).color(red, green, blue, alpha).endVertex()
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