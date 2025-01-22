package dev.aaronhowser.mods.ariadnesthread.client

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import dev.aaronhowser.mods.ariadnesthread.AriadnesThread
import net.minecraft.client.renderer.RenderType
import java.util.*

// Never construct
class ModRenderTypes private constructor(
    name: String,
    format: VertexFormat,
    mode: VertexFormat.Mode,
    bufferSize: Int,
    affectsCrumbling: Boolean,
    sortOnUpload: Boolean,
    setupState: Runnable,
    clearState: Runnable
) : RenderType(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState) {

    companion object {
        @Suppress("INACCESSIBLE_TYPE")
        val THREAD: RenderType = create(
            "${AriadnesThread.ID}:thread",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.DEBUG_LINE_STRIP,
            1536,
            CompositeState.builder()
                .setShaderState(POSITION_COLOR_SHADER)
                .setLineState(LineStateShard(OptionalDouble.of(20.0)))  //FIXME: Why not thick?
                .setTransparencyState(NO_TRANSPARENCY)
                .setCullState(NO_CULL)
                .setDepthTestState(NO_DEPTH_TEST)
                .createCompositeState(false)
        )
    }
}
