package com.aaronhowser1.ariadnesthread.utils

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.IntTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.ResourceLocation

data class Location(
    val dimension: ResourceLocation,
    val blockPos: BlockPos
) {

    constructor(compoundTag: CompoundTag) : this(
        ResourceLocation(compoundTag.getString("dimension")),
        BlockPos(
            (compoundTag.get("blockPos") as ListTag).getInt(0),
            (compoundTag.get("blockPos") as ListTag).getInt(1),
            (compoundTag.get("blockPos") as ListTag).getInt(2)
        )
    )

    override fun toString(): String {
        return "Location(dimension=$dimension, blockPos=$blockPos)"
    }

    fun distanceSqr(other: Location): Double? {
        if (other.dimension != dimension) return null

        return blockPos.distSqr(other.blockPos)
    }

    fun isCloserThan(other: Location, distance: Float): Boolean {
        if (other.dimension != dimension) return false

        val distSqr = distanceSqr(other) ?: return false
        return distSqr < distance * distance
    }

    fun toTag(): Tag {

        val compoundTag = CompoundTag()
        compoundTag.putString("dimension", dimension.toString())

        val blockPosListTag = ListTag().apply {
            add(IntTag.valueOf(blockPos.x))
            add(IntTag.valueOf(blockPos.y))
            add(IntTag.valueOf(blockPos.z))
        }

        compoundTag.put("blockPos", blockPosListTag)

        return compoundTag

    }
}