package com.aaronhowser1.ariadnesthread.utils

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.DoubleTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.world.phys.Vec3

data class Location(
    val vec3: Vec3
) : Vec3(vec3.x, vec3.y, vec3.z) {

    constructor(compoundTag: CompoundTag) : this(
        Vec3(
            (compoundTag.get("blockPos") as ListTag).getDouble(0),
            (compoundTag.get("blockPos") as ListTag).getDouble(1),
            (compoundTag.get("blockPos") as ListTag).getDouble(2)
        )
    )

    override fun toString(): String {
        return "Location(${vec3.x}, ${vec3.y}, ${vec3.z})"
    }

    fun toTag(): Tag {

        val compoundTag = CompoundTag()

        val blockPosListTag = ListTag().apply {
            add(DoubleTag.valueOf(vec3.x))
            add(DoubleTag.valueOf(vec3.y))
            add(DoubleTag.valueOf(vec3.z))
        }

        compoundTag.put("blockPos", blockPosListTag)

        return compoundTag

    }

    companion object {
        fun Vec3.toLocation(): Location = Location(this)
    }
}