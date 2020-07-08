package com.possible_triangle.mischief.spell

import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.Vec3d
import java.util.*

abstract class Spell(val type: Type) {

    enum class Type {
        TRIGGER, TICK
    }

    data class Material(val rank: Int, val range: Int = 0)

    data class Context(val target: LivingEntity, val spell: SpellStack, val at: Vec3d?, private val by: UUID?) {
        fun getSource(): UUID? {
            return by ?: spell.owner
        }
    }

    abstract fun getCooldown(stack: SpellStack): Int

    abstract fun getRank(stack: SpellStack): Int

    abstract fun maxPower(): Int

    internal abstract fun apply(ctx: Context)

    open fun affects(): Class<out LivingEntity> {
        return LivingEntity::class.java
    }

}