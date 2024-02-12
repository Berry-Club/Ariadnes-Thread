package com.aaronhowser1.ariadnesthread.client

import com.aaronhowser1.ariadnesthread.config.ClientConfig
import com.aaronhowser1.ariadnesthread.item.ThreadItem
import com.aaronhowser1.ariadnesthread.utils.Location
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.*
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.client.event.RenderLevelStageEvent
import org.joml.Matrix4f

@Suppress("SameParameterValue")
object ModRenderer {

    private var vertexBuffer: VertexBuffer? = null

    private var reloadNeeded = false

    var histories: List<ThreadItem.Companion.History> = emptyList()
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
        vertexBuffer = VertexBuffer(VertexBuffer.Usage.STATIC)
        reloadNeeded = false

        val tesselator: Tesselator = Tesselator.getInstance()
        val buffer: BufferBuilder = tesselator.builder

        buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR)

        for (history in histories) {
            for (i in 0 until history.size - 1) {
                val loc1 = history[i]
                val loc2 = history[i + 1]

                val percentDone = i.toFloat() / history.size

                val red = 1f - percentDone
                val green = percentDone
                val blue = 0f

                if (i == 0) {
                    renderCube(buffer, loc1, red, green, blue)
                }

                if (loc1.closerThan(loc2, ClientConfig.TELEPORT_DISTANCE)) {
                    renderLine(buffer, loc1, loc2, red, green, blue)
                } else {
                    renderCube(buffer, loc1, red, green, blue)
                    renderCube(buffer, loc2, red, green, blue)
                }
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

        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.disableDepthTest()

        RenderSystem.setShader(GameRenderer::getPositionColorShader)

        val matrix: PoseStack = event.poseStack

        matrix.pushPose()
        matrix.translate(-view.x, -view.y, -view.z)

        vertexBuffer!!.apply {
            bind()
            drawWithShader(
                matrix.last().pose(),
                Matrix4f(event.projectionMatrix),
                RenderSystem.getShader()!!
            )
        }

        VertexBuffer.unbind()
        matrix.popPose()

        RenderSystem.enableDepthTest()
        RenderSystem.disableBlend()
    }

    private fun renderLine(
        buffer: BufferBuilder,
        loc1: Location,
        loc2: Location,
        red: Float,
        green: Float,
        blue: Float
    ) {
        val x1 = loc1.x.toDouble()
        val y1 = loc1.y.toDouble()
        val z1 = loc1.z.toDouble()
        val x2 = loc2.x.toDouble()
        val y2 = loc2.y.toDouble()
        val z2 = loc2.z.toDouble()

        val alpha = ClientConfig.ALPHA

        buffer.vertex(x1, y1, z1).color(red, green, blue, alpha).endVertex()
        buffer.vertex(x2, y2, z2).color(red, green, blue, alpha).endVertex()
    }

    private fun renderCube(
        buffer: BufferBuilder,
        loc: Location,
        red: Float,
        green: Float,
        blue: Float
    ) {

        val cubeSize = 0.5
        val alpha = ClientConfig.ALPHA

        fun connectPoints(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double) {
            buffer.vertex(x1, y1, z1).color(red, green, blue, alpha).endVertex()
            buffer.vertex(x2, y2, z2).color(red, green, blue, alpha).endVertex()
        }

        val x = loc.x - cubeSize / 2
        val y = loc.y - cubeSize / 2
        val z = loc.z - cubeSize / 2

        connectPoints(x, y, z, x + cubeSize, y, z)
        connectPoints(x, y, z, x, y + cubeSize, z)
        connectPoints(x, y, z, x, y, z + cubeSize)
        connectPoints(x + cubeSize, y, z, x + cubeSize, y + cubeSize, z)
        connectPoints(x + cubeSize, y, z, x + cubeSize, y, z + cubeSize)
        connectPoints(x, y + cubeSize, z, x + cubeSize, y + cubeSize, z)
        connectPoints(x, y + cubeSize, z, x, y + cubeSize, z + cubeSize)
        connectPoints(x, y, z + cubeSize, x + cubeSize, y, z + cubeSize)
        connectPoints(x, y, z + cubeSize, x, y + cubeSize, z + cubeSize)
        connectPoints(x, y + cubeSize, z + cubeSize, x + cubeSize, y + cubeSize, z + cubeSize)
        connectPoints(x + cubeSize, y, z + cubeSize, x + cubeSize, y + cubeSize, z + cubeSize)
        connectPoints(x + cubeSize, y + cubeSize, z, x + cubeSize, y + cubeSize, z + cubeSize)
    }
}