package dev.aaronhowser.mods.ariadnesthread.client

import com.mojang.blaze3d.vertex.*
import dev.aaronhowser.mods.ariadnesthread.config.ClientConfig
import dev.aaronhowser.mods.ariadnesthread.item.component.HistoryItemComponent
import dev.aaronhowser.mods.ariadnesthread.item.component.LocationItemComponent
import net.minecraft.util.Mth
import net.neoforged.neoforge.client.event.RenderLevelStageEvent

object ModRenderer {

    private var vertexBuffer: VertexBuffer? = null
    private var reloadNeeded = false

    var histories: List<HistoryItemComponent> = emptyList()
        set(value) {
            field = value
            reloadNeeded = true
        }

    fun renderLines(event: RenderLevelStageEvent) {
        if (!reloadNeeded && histories.isEmpty()) return

        if (vertexBuffer == null || reloadNeeded) refresh()

    }

    private fun refresh() {
        vertexBuffer = VertexBuffer(VertexBuffer.Usage.STATIC)
        reloadNeeded = false

        val tesselator: Tesselator = Tesselator.getInstance()
        val buffer: BufferBuilder = tesselator.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR)

        val alpha = ClientConfig.alpha.get().toFloat()
        val startRed = ClientConfig.startRed.get().toFloat()
        val startGreen = ClientConfig.startGreen.get().toFloat()
        val startBlue = ClientConfig.startBlue.get().toFloat()
        val endRed = ClientConfig.endRed.get().toFloat()
        val endGreen = ClientConfig.endGreen.get().toFloat()
        val endBlue = ClientConfig.endBlue.get().toFloat()

        for (history in histories.map { it.history }) {
            for (i in 0 until history.size - 1) {
                val percentDone = i.toFloat() / history.size

                val red = Mth.lerp(percentDone, startRed, endRed)
                val green = Mth.lerp(percentDone, startGreen, endGreen)
                val blue = Mth.lerp(percentDone, startBlue, endBlue)

                val loc1 = history[i]
                val loc2 = history[i + 1]

                if (i == 0) {
                    renderCube(buffer, loc1, alpha, red, green, blue)
                }

                if (loc1.closerThan(loc2, ClientConfig.teleportDistance.get())) {
                    renderLine(buffer, loc1, loc2, alpha, red, green, blue)
                } else {
                    renderCube(buffer, loc1, alpha, red, green, blue)
                    renderCube(buffer, loc2, alpha, red, green, blue)
                }
            }
        }

        val build = buffer.build()
        if (build == null) {
            vertexBuffer = null
            return
        } else {
            vertexBuffer!!.bind()
            vertexBuffer!!.upload(build)
            VertexBuffer.unbind()
        }

    }

    private fun renderLine(
        buffer: BufferBuilder,
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
        buffer: BufferBuilder,
        loc1: LocationItemComponent,
        loc2: LocationItemComponent,
        alpha: Float,
        red: Float,
        green: Float,
        blue: Float
    ) {
        renderLine(buffer, loc1.x, loc1.y, loc1.z, loc2.x, loc2.y, loc2.z, alpha, red, green, blue)
    }

    private fun renderCube(
        buffer: BufferBuilder,
        loc: LocationItemComponent,
        alpha: Float,
        red: Float,
        green: Float,
        blue: Float
    ) {
        val cubeSize = 0.5f
        val x1 = loc.x - cubeSize / 2
        val y1 = loc.y - cubeSize / 2
        val z1 = loc.z - cubeSize / 2
        val x2 = loc.x + cubeSize / 2
        val y2 = loc.y + cubeSize / 2
        val z2 = loc.z + cubeSize / 2

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