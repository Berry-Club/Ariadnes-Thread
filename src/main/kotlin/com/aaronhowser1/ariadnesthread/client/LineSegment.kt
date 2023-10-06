package com.aaronhowser1.ariadnesthread.client

import com.aaronhowser1.ariadnesthread.utils.Location
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.LevelRenderer
import net.minecraft.client.renderer.RenderType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.phys.Vec3
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import kotlin.math.max
import kotlin.math.min

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

        val buffer: VertexConsumer = minecraftInstance.renderBuffers().bufferSource().getBuffer(RenderType.lines())

        val minX = min(start.x, end.x)
        val minY = min(start.y, end.y)
        val minZ = min(start.z, end.z)
        val maxX = max(start.x, end.x)
        val maxY = max(start.y, end.y)
        val maxZ = max(start.z, end.z)

        LevelRenderer.renderVoxelShape(
            poseStack,
            buffer,
            Block.box(
                minX.toDouble(),
                minY.toDouble(),
                minZ.toDouble(),
                maxX.toDouble(),
                maxY.toDouble(),
                maxZ.toDouble()
            ),
            start.x.toDouble() - projectedView.x,
            start.y.toDouble() - projectedView.y,
            start.z.toDouble() - projectedView.z,
            1f,
            1f,
            1f,
            1f
        )
    }

    init {
        lineSegments.add(this)
    }

    companion object {
        val lineSegments = mutableListOf<LineSegment>()
    }

}