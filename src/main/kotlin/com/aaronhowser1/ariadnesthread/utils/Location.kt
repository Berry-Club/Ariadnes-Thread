package com.aaronhowser1.ariadnesthread.utils

import net.minecraft.core.Vec3i
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.IntTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.world.phys.Vec3

data class Location(
    val vec3i: Vec3i
) : Vec3i(vec3i.x, vec3i.y, vec3i.z) {

    constructor(x: Int, y: Int, z: Int) : this(Vec3i(x, y, z))

    constructor(compoundTag: CompoundTag) : this(
        Vec3i(
            (compoundTag.get("blockPos") as ListTag).getInt(0),
            (compoundTag.get("blockPos") as ListTag).getInt(1),
            (compoundTag.get("blockPos") as ListTag).getInt(2)
        )
    )

    override fun toString(): String {
        return "Location(${vec3i.x}, ${vec3i.y}, ${vec3i.z})"
    }

    fun toTag(): Tag {

        val compoundTag = CompoundTag()

        val blockPosListTag = ListTag().apply {
            add(IntTag.valueOf(vec3i.x))
            add(IntTag.valueOf(vec3i.y))
            add(IntTag.valueOf(vec3i.z))
        }

        compoundTag.put("blockPos", blockPosListTag)

        return compoundTag

    }

    companion object {
        fun Vec3.toLocation(): Location = Location(this.x.toInt(), this.y.toInt(), this.z.toInt())
    }
}