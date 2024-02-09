package com.aaronhowser1.ariadnesthread.utils

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.IntTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.world.phys.Vec3

data class Location(
    val x: Int,
    val y: Int,
    val z: Int
) {

    constructor(compoundTag: CompoundTag) : this(
        (compoundTag.get("blockPos") as ListTag).getInt(0),
        (compoundTag.get("blockPos") as ListTag).getInt(1),
        (compoundTag.get("blockPos") as ListTag).getInt(2)
    )

    override fun toString(): String {
        return "Location($x, $y, $z)"
    }

    fun toTag(): Tag {
        val compoundTag = CompoundTag()

        val blockPosListTag = ListTag().apply {
            add(IntTag.valueOf(x))
            add(IntTag.valueOf(y))
            add(IntTag.valueOf(z))
        }

        compoundTag.put("blockPos", blockPosListTag)

        return compoundTag
    }

    fun closerThan(other: Location, distance: Int): Boolean {
        return
    }

    companion object {
        fun Vec3.toLocation(): Location = Location(x.toInt(), y.toInt(), z.toInt())
    }
}