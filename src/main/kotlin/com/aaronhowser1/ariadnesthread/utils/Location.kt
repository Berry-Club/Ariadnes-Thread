package com.aaronhowser1.ariadnesthread.utils

import com.google.gson.Gson
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.ResourceLocation

data class Location(
    val dimension: ResourceLocation,
    val blockPos: BlockPos
) {
    override fun toString(): String {
        return "Location(dimension=$dimension, blockPos=$blockPos)"
    }

    fun distanceSqr(other: Location): Double {
        return blockPos.distSqr(other.blockPos)
    }

    fun isCloserThan(other: Location, distance: Float): Boolean {
        return distanceSqr(other) < distance * distance
    }

    fun toJson(): String {
        val gson = Gson()
        return gson.toJson(
            mapOf(
                "dimension" to dimension.toString(),
                "blockPos" to mapOf(
                    "x" to blockPos.x,
                    "y" to blockPos.y,
                    "z" to blockPos.z
                )
            )
        )
    }

    fun toTag(): Tag {

        val compoundTag = CompoundTag()
        compoundTag.putString("dimension", dimension.toString())

        val blockPosTag = CompoundTag()
        blockPosTag.putInt("x", blockPos.x)
        blockPosTag.putInt("y", blockPos.y)
        blockPosTag.putInt("z", blockPos.z)

        compoundTag.put("blockPos", blockPosTag)

        return compoundTag

    }
}