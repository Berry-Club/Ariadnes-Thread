package dev.aaronhowser.mods.ariadnesthread.client

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.blaze3d.vertex.VertexFormat
import dev.aaronhowser.mods.ariadnesthread.AriadnesThread
import dev.aaronhowser.mods.ariadnesthread.config.ClientConfig
import dev.aaronhowser.mods.ariadnesthread.config.StartupConfig
import dev.aaronhowser.mods.ariadnesthread.item.ThreadItem
import dev.aaronhowser.mods.ariadnesthread.registry.ModDataComponents
import dev.aaronhowser.mods.ariadnesthread.util.ClientUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderStateShard.LineStateShard
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.RenderType.*
import net.minecraft.core.BlockPos
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.client.event.RenderLevelStageEvent
import org.joml.Matrix4f
import java.util.*
import kotlin.math.sqrt

@EventBusSubscriber(
    modid = AriadnesThread.ID,
    value = [Dist.CLIENT]
)
object ModRenderer {

    @Suppress("INACCESSIBLE_TYPE")
    private val THREAD_RENDER_TYPE: RenderType = create(
        "${AriadnesThread.ID}:thread",
        DefaultVertexFormat.POSITION_COLOR_NORMAL,
        VertexFormat.Mode.LINES,
        256,
        true,
        false,
        CompositeState.builder()
            .setShaderState(RENDERTYPE_LINES_SHADER)
            .setLineState(LineStateShard(OptionalDouble.of(StartupConfig.LINE_THICKNESS.get())))
            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
            .setOutputState(OUTLINE_TARGET)
            .setWriteMaskState(COLOR_WRITE)
            .setCullState(NO_CULL)
            .createCompositeState(false)
    )

    private var reloadNeeded = false

    private val histories: MutableList<List<BlockPos>> = mutableListOf()

    @SubscribeEvent
    fun afterClientTick(event: ClientTickEvent.Post) {
        val player = ClientUtil.localPlayer ?: return
        val level = player.level()

        val threadItems = player.inventory.items.filter {
            ThreadItem.inStartingDimension(it, level)
        }

        val newHistories = threadItems.mapNotNull { it.get(ModDataComponents.HISTORY) }

        if (this.histories.isEmpty() && newHistories.isEmpty()) return

        this.histories.clear()
        this.histories.addAll(newHistories)
    }

    @SubscribeEvent
    fun onWorldRenderLast(event: RenderLevelStageEvent) {
        if (event.stage != RenderLevelStageEvent.Stage.AFTER_LEVEL) return
        if (!this.reloadNeeded && this.histories.isEmpty()) return

        val poseStack: PoseStack = event.poseStack
        poseStack.pushPose()

        val pose = poseStack.last()

        val vertexConsumer: VertexConsumer = Minecraft.getInstance()
            .renderBuffers()
            .bufferSource()
            .getBuffer(THREAD_RENDER_TYPE)

        val alpha = ClientConfig.LINE_ALPHA.get().toFloat()
        val startRed = ClientConfig.LINE_START_RED.get().toFloat()
        val startGreen = ClientConfig.LINE_START_GREEN.get().toFloat()
        val startBlue = ClientConfig.LINE_START_BLUE.get().toFloat()
        val endRed = ClientConfig.LINE_END_RED.get().toFloat()
        val endGreen = ClientConfig.LINE_END_GREEN.get().toFloat()
        val endBlue = ClientConfig.LINE_END_BLUE.get().toFloat()

        val cameraPos = event.camera.position
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z)

        for (history in this.histories) {
            for (i in 0 until history.size - 1) {
                val percentDone = i.toFloat() / history.size

                val red = Mth.lerp(percentDone, startRed, endRed)
                val green = Mth.lerp(percentDone, startGreen, endGreen)
                val blue = Mth.lerp(percentDone, startBlue, endBlue)

                val startPos = history[i].center
                val endPos = history[i + 1].center

                if (i == 0) {
                    renderCube(vertexConsumer, pose.pose(), startPos, alpha, red, green, blue)
                }

                if (startPos.closerThan(endPos, ClientConfig.TELEPORT_DISTANCE.get())) {
                    drawLine(vertexConsumer, pose.pose(), startPos, endPos, alpha, red, green, blue)
                } else {
                    renderCube(vertexConsumer, pose.pose(), startPos, alpha, red, green, blue)
                    renderCube(vertexConsumer, pose.pose(), endPos, alpha, red, green, blue)
                }
            }
        }

        poseStack.popPose()
    }

    private fun drawLine(
        vertexConsumer: VertexConsumer,
        pose: Matrix4f,
        startX: Float,
        startY: Float,
        startZ: Float,
        endX: Float,
        endY: Float,
        endZ: Float,
        alpha: Float,
        red: Float,
        green: Float,
        blue: Float
    ) {
        var lengthX = endX - startX
        var lengthY = endY - startY
        var lengthZ = endZ - startZ

        val length = sqrt(lengthX * lengthX + lengthY * lengthY + lengthZ * lengthZ)

        lengthX /= length
        lengthY /= length
        lengthZ /= length

        vertexConsumer.addVertex(
            pose,
            startX,
            startY,
            startZ
        ).setColor(red, green, blue, alpha).setNormal(lengthX, lengthY, lengthZ)

        vertexConsumer.addVertex(
            pose,
            endX,
            endY,
            endZ
        ).setColor(red, green, blue, alpha).setNormal(lengthX, lengthY, lengthZ)
    }

    private fun drawLine(
        vertexConsumer: VertexConsumer,
        pose: Matrix4f,
        startPos: Vec3,
        endPos: Vec3,
        alpha: Float,
        red: Float,
        green: Float,
        blue: Float
    ) {
        drawLine(
            vertexConsumer,
            pose,
            startPos.x.toFloat(),
            startPos.y.toFloat(),
            startPos.z.toFloat(),
            endPos.x.toFloat(),
            endPos.y.toFloat(),
            endPos.z.toFloat(),
            alpha,
            red,
            green,
            blue
        )
    }

    private fun renderCube(
        buffer: VertexConsumer,
        pose: Matrix4f,
        center: Vec3,
        alpha: Float,
        red: Float,
        green: Float,
        blue: Float
    ) {
        val cubeSize = 0.5f
        val x1 = (center.x - cubeSize / 2).toFloat()
        val y1 = (center.y - cubeSize / 2).toFloat()
        val z1 = (center.z - cubeSize / 2).toFloat()
        val x2 = (center.x + cubeSize / 2).toFloat()
        val y2 = (center.y + cubeSize / 2).toFloat()
        val z2 = (center.z + cubeSize / 2).toFloat()

        drawLine(buffer, pose, x1, y1, z1, x2, y1, z1, alpha, red, green, blue)
        drawLine(buffer, pose, x1, y1, z1, x1, y2, z1, alpha, red, green, blue)
        drawLine(buffer, pose, x1, y1, z1, x1, y1, z2, alpha, red, green, blue)
        drawLine(buffer, pose, x2, y1, z1, x2, y2, z1, alpha, red, green, blue)
        drawLine(buffer, pose, x2, y1, z1, x2, y1, z2, alpha, red, green, blue)
        drawLine(buffer, pose, x1, y2, z1, x2, y2, z1, alpha, red, green, blue)
        drawLine(buffer, pose, x1, y2, z1, x1, y2, z2, alpha, red, green, blue)
        drawLine(buffer, pose, x1, y1, z2, x2, y1, z2, alpha, red, green, blue)
        drawLine(buffer, pose, x1, y1, z2, x1, y2, z2, alpha, red, green, blue)
        drawLine(buffer, pose, x1, y2, z2, x2, y2, z2, alpha, red, green, blue)
        drawLine(buffer, pose, x2, y1, z2, x2, y2, z2, alpha, red, green, blue)
        drawLine(buffer, pose, x2, y2, z1, x2, y2, z2, alpha, red, green, blue)
    }

}