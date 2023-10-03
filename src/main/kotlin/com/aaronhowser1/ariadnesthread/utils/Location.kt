package com.aaronhowser1.ariadnesthread.utils

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.IntTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag

data class Location(
    val blockPos: BlockPos
) : BlockPos(blockPos.x, blockPos.y, blockPos.z) {

    constructor(compoundTag: CompoundTag) : this(
        BlockPos(
            (compoundTag.get("blockPos") as ListTag).getInt(0),
            (compoundTag.get("blockPos") as ListTag).getInt(1),
            (compoundTag.get("blockPos") as ListTag).getInt(2)
        )
    )

    override fun toString(): String {
        return "Location(${blockPos.x}, ${blockPos.y}, ${blockPos.z})"
    }

    fun toTag(): Tag {

        val compoundTag = CompoundTag()

        val blockPosListTag = ListTag().apply {
            add(IntTag.valueOf(blockPos.x))
            add(IntTag.valueOf(blockPos.y))
            add(IntTag.valueOf(blockPos.z))
        }

        compoundTag.put("blockPos", blockPosListTag)

        return compoundTag

    }
}