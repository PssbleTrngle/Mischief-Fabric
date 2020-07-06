package com.possible_triangle.mischief.spell

import net.minecraft.entity.LivingEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.Vec3d
import java.util.*

final class SpellStack(val spell : Spell, val power: Int, val owner: UUID?) {

    companion object {
        fun deserialize(tag: CompoundTag): SpellStack? {
            val power = tag.getInt("power")
            val owner = if (tag.contains("owner")) tag.getUuid("owner") else null
            return null
        }
    }

    fun serialize(): CompoundTag {
        val tag = CompoundTag()
        tag.putInt("power", power)
        if(owner != null) tag.putUuid("owner", owner)
        return tag
    }

    fun cast(target: LivingEntity, by: ServerPlayerEntity? = null, at: Vec3d? = null) {
        val ctx = Spell.Context(target, this, at, by?.uuid)
        spell.apply(ctx)
    }

}