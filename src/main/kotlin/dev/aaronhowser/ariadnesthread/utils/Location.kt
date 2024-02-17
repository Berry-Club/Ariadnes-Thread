package dev.aaronhowser.ariadnesthread.utils

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.FloatTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.world.phys.Vec3

data class Location(
    val x: Float,
    val y: Float,
    val z: Float
) {

    constructor(compoundTag: CompoundTag) : this(
        (compoundTag.get("blockPos") as ListTag).getFloat(0),
        (compoundTag.get("blockPos") as ListTag).getFloat(1),
        (compoundTag.get("blockPos") as ListTag).getFloat(2)
    )

    override fun toString(): String {
        return "Location($x, $y, $z)"
    }

    fun toTag(): Tag {

        val compoundTag = CompoundTag()

        val blockPosListTag = ListTag().apply {
            add(FloatTag.valueOf(x))
            add(FloatTag.valueOf(y))
            add(FloatTag.valueOf(z))
        }

        compoundTag.put("blockPos", blockPosListTag)

        return compoundTag

    }

    fun closerThan(other: Location, distance: Double): Boolean {
        return closerThan(other, distance.toFloat())
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun closerThan(other: Location, distance: Float): Boolean {
        return distanceToSqr(other) < distance * distance
    }

    private fun distanceToSqr(other: Location): Float {
        val d0: Float = other.x - x
        val d1: Float = other.y - y
        val d2: Float = other.z - z
        return d0 * d0 + d1 * d1 + d2 * d2
    }

    companion object {
        fun Vec3.toLocation(): Location = Location(this.x.toFloat(), this.y.toFloat(), this.z.toFloat())
    }
}