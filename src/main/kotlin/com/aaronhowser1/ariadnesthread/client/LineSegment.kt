package com.aaronhowser1.ariadnesthread.client

import com.aaronhowser1.ariadnesthread.utils.Location
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
data class LineSegment(
    val start: Location,
    val end: Location,
    val level: String,
    val colorHex: String
) {
    companion object {
        val lineSegments: MutableList<LineSegment> = mutableListOf()
    }

    init {
        lineSegments.add(this)
    }
}