package com.aaronhowser1.ariadnesthread.client

import com.aaronhowser1.ariadnesthread.utils.Location
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Matrix3f
import com.mojang.math.Matrix4f
import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderType
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import kotlin.random.Random

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

        val vertexConsumer: VertexConsumer =
            minecraftInstance.renderBuffers().bufferSource().getBuffer(RenderType.lines())

        val matrix4f: Matrix4f = poseStack.last().pose()
        val matrix3f: Matrix3f = poseStack.last().normal()

        vertexConsumer
            .vertex(
                matrix4f,
                start.x.toFloat() - projectedView.x.toFloat(),
                start.y.toFloat() - projectedView.y.toFloat(),
                start.z.toFloat() - projectedView.z.toFloat()
            )
            .color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1f)
            .normal(matrix3f, 0f, 1f, 0f)
            .endVertex()
    }

    init {
        lineSegments.add(this)
    }

    companion object {
        val lineSegments = mutableListOf<LineSegment>()
    }

}