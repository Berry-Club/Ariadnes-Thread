package dev.aaronhowser.mods.ariadnesthread.client

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.*
import dev.aaronhowser.mods.ariadnesthread.config.ClientConfig
import dev.aaronhowser.mods.ariadnesthread.item.component.HistoryItemComponent
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.BlockPos
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.client.event.RenderLevelStageEvent
import org.lwjgl.opengl.GL11

/**
 * I got a lot of the code for this from the mod [Advanced Xray](https://github.com/AdvancedXRay/XRay-Mod/blob/mc/neoforge/1.21/src/main/java/pro/mikey/xray/xray/Render.java)
 */
object ModRenderer {

    private var vertexBuffer: VertexBuffer? = null
    private var reloadNeeded = false

    var histories: List<HistoryItemComponent> = emptyList()
        set(value) {
            field = value
            reloadNeeded = true
        }

    fun renderLines(event: RenderLevelStageEvent) {
        if (!this.reloadNeeded && this.histories.isEmpty()) return

        if (this.vertexBuffer == null || this.reloadNeeded) {
            rebuildBuffer()
        }

        renderBuffer(event)
    }

    private fun renderBuffer(event: RenderLevelStageEvent) {
        val playerView: Vec3 = event.camera.position
        val vertexBuffer = this.vertexBuffer ?: return

        RenderSystem.depthMask(false)
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()

        val poseStack: PoseStack = event.poseStack
        poseStack.pushPose()

        RenderSystem.setShader(GameRenderer::getPositionColorShader)
        RenderSystem.applyModelViewMatrix()
        RenderSystem.depthFunc(GL11.GL_ALWAYS)

        poseStack.mulPose(event.modelViewMatrix)
        poseStack.translate(-playerView.x, -playerView.y, -playerView.z)

        vertexBuffer.bind()
        vertexBuffer.drawWithShader(
            poseStack.last().pose(),
            event.projectionMatrix,
            RenderSystem.getShader()!!
        )

        VertexBuffer.unbind()
        RenderSystem.depthFunc(GL11.GL_LEQUAL)

        poseStack.popPose()
        RenderSystem.applyModelViewMatrix()

    }

    private fun rebuildBuffer() {
        this.vertexBuffer = VertexBuffer(VertexBuffer.Usage.STATIC)
        this.reloadNeeded = false

        val vertexBuffer = this.vertexBuffer ?: return

        val buffer: VertexConsumer = Minecraft.getInstance()
            .renderBuffers()
            .bufferSource()
            .getBuffer(RenderType.debugLineStrip(3.0))

        val alpha = ClientConfig.LINE_ALPHA.get().toFloat()
        val startRed = ClientConfig.LINE_START_RED.get().toFloat()
        val startGreen = ClientConfig.LINE_START_GREEN.get().toFloat()
        val startBlue = ClientConfig.LINE_START_BLUE.get().toFloat()
        val endRed = ClientConfig.LINE_END_RED.get().toFloat()
        val endGreen = ClientConfig.LINE_END_GREEN.get().toFloat()
        val endBlue = ClientConfig.LINE_END_BLUE.get().toFloat()

        for (history in histories.map { it.locations }) {
            for (i in 0 until history.size - 1) {
                val percentDone = i.toFloat() / history.size

                val red = Mth.lerp(percentDone, startRed, endRed)
                val green = Mth.lerp(percentDone, startGreen, endGreen)
                val blue = Mth.lerp(percentDone, startBlue, endBlue)

                val startPos = history[i]
                val endPos = history[i + 1]

                if (i == 0) {
                    renderCube(buffer, startPos, alpha, red, green, blue)
                }

                if (startPos.closerThan(endPos, ClientConfig.teleportDistance.get())) {
                    renderLine(buffer, startPos, endPos, alpha, red, green, blue)
                } else {
                    renderCube(buffer, startPos, alpha, red, green, blue)
                    renderCube(buffer, endPos, alpha, red, green, blue)
                }
            }
        }

        val build = buffer.build()
        if (build == null) {
            this.vertexBuffer = null
            return
        }

        vertexBuffer.bind()
        vertexBuffer.upload(build)
        VertexBuffer.unbind()
    }

    private fun renderLine(
        buffer: VertexConsumer,
        x1: Float, y1: Float, z1: Float,
        x2: Float, y2: Float, z2: Float,
        alpha: Float,
        red: Float,
        green: Float,
        blue: Float
    ) {
        buffer.addVertex(x1, y1, z1).setColor(red, green, blue, alpha)
        buffer.addVertex(x2, y2, z2).setColor(red, green, blue, alpha)
    }

    private fun renderLine(
        buffer: VertexConsumer,
        blockPos1: BlockPos,
        blockPos2: BlockPos,
        alpha: Float,
        red: Float,
        green: Float,
        blue: Float
    ) {
        renderLine(
            buffer,
            blockPos1.x.toFloat(),
            blockPos1.y.toFloat(),
            blockPos1.z.toFloat(),
            blockPos2.x.toFloat(),
            blockPos2.y.toFloat(),
            blockPos2.z.toFloat(),
            alpha,
            red,
            green,
            blue
        )
    }

    private fun renderCube(
        buffer: VertexConsumer,
        blockPos: BlockPos,
        alpha: Float,
        red: Float,
        green: Float,
        blue: Float
    ) {
        val cubeSize = 0.5f
        val x1 = blockPos.x - cubeSize / 2
        val y1 = blockPos.y - cubeSize / 2
        val z1 = blockPos.z - cubeSize / 2
        val x2 = blockPos.x + cubeSize / 2
        val y2 = blockPos.y + cubeSize / 2
        val z2 = blockPos.z + cubeSize / 2

        renderLine(buffer, x1, y1, z1, x2, y1, z1, alpha, red, green, blue)
        renderLine(buffer, x1, y1, z1, x1, y2, z1, alpha, red, green, blue)
        renderLine(buffer, x1, y1, z1, x1, y1, z2, alpha, red, green, blue)

        renderLine(buffer, x2, y1, z1, x2, y2, z1, alpha, red, green, blue)
        renderLine(buffer, x2, y1, z1, x2, y1, z2, alpha, red, green, blue)
        renderLine(buffer, x1, y2, z1, x2, y2, z1, alpha, red, green, blue)

        renderLine(buffer, x1, y2, z1, x1, y2, z2, alpha, red, green, blue)
        renderLine(buffer, x1, y1, z2, x2, y1, z2, alpha, red, green, blue)
        renderLine(buffer, x1, y1, z2, x1, y2, z2, alpha, red, green, blue)

        renderLine(buffer, x1, y2, z2, x2, y2, z2, alpha, red, green, blue)
        renderLine(buffer, x2, y1, z2, x2, y2, z2, alpha, red, green, blue)
        renderLine(buffer, x2, y2, z1, x2, y2, z2, alpha, red, green, blue)
    }

}