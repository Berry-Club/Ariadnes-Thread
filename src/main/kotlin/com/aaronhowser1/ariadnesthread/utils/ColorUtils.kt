package com.aaronhowser1.ariadnesthread.utils

object ColorUtils {

    fun interpolateColor(start: Int, end: Int, progress: Float): Int {
        val startA = start shr 24 and 0xff
        val startR = start shr 16 and 0xff
        val startG = start shr 8 and 0xff
        val startB = start and 0xff

        val endA = end shr 24 and 0xff
        val endR = end shr 16 and 0xff
        val endG = end shr 8 and 0xff
        val endB = end and 0xff

        val a = ((startA + (endA - startA) * progress).toInt() shl 24 and 0xff000000.toInt())
        val r = ((startR + (endR - startR) * progress).toInt() shl 16 and 0x00ff0000.toInt())
        val g = ((startG + (endG - startG) * progress).toInt() shl 8 and 0x0000ff00.toInt())
        val b = ((startB + (endB - startB) * progress).toInt() and 0x000000ff.toInt())

        return a or r or g or b
    }

    fun Int.getARGB(): List<Int> {
        return listOf(this.getAlpha(), this.getRed(), this.getGreen(), this.getBlue())
    }

    fun Int.getRed(): Int {
        return this shr 16 and 0xff
    }

    fun Int.getGreen(): Int {
        return this shr 8 and 0xff
    }

    fun Int.getBlue(): Int {
        return this and 0xff
    }

    fun Int.getAlpha(): Int {
        return this shr 24 and 0xff
    }

}