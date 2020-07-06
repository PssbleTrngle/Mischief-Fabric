package com.possible_triangle.mischief.spell

import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.Vec3d
import java.util.*

abstract class Spell {

    data class Material(val rank: Int)

    data class Context(val target: LivingEntity, val spell: SpellStack, val at: Vec3d?, private val by: UUID?) {
        fun getSource(): UUID? {
            return by ?: spell.owner
        }
    }

    internal abstract fun apply(ctx: Context)

}