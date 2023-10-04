package com.aaronhowser1.ariadnesthread.client

import com.aaronhowser1.ariadnesthread.utils.Location
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Matrix4f
import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderType
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
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


    // https://github.com/FTBTeam/FTB-Ultimine/blob/main/common/src/main/java/dev/ftb/mods/ftbultimine/client/FTBUltimineClient.java#L100-L139
    fun render(poseStack: PoseStack) {

        val minecraftInstance: Minecraft = Minecraft.getInstance()
        val camera: Camera = minecraftInstance.entityRenderDispatcher.camera
        val projectedView: Vec3 = camera.position

        poseStack.pushPose()
        poseStack.translate(
            start.x - projectedView.x,
            start.y - projectedView.y,
            start.z - projectedView.z
        )

        val matrix: Matrix4f = poseStack.last().pose()

        val buffer: VertexConsumer = minecraftInstance.renderBuffers().bufferSource().getBuffer(RenderType.lines())
        buffer.apply {
            vertex(matrix, start.x.toFloat(), start.y.toFloat(), start.z.toFloat())
                .color(0.0f, 0.0f, 0.0f, 0.0f)
                .endVertex()

            vertex(matrix, end.x.toFloat(), end.y.toFloat(), end.z.toFloat())
                .color(0.0f, 0.0f, 0.0f, 0.0f)
                .endVertex()
        }

        minecraftInstance.renderBuffers().bufferSource().endBatch(RenderType.lines())
        poseStack.popPose()
    }

    init {
        lineSegments.add(this)
    }

    companion object {
        val lineSegments = mutableListOf<LineSegment>()
    }

}