package com.possible_triangle.mischief.spell

import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.Vec3d
import java.util.*

abstract class Spell(val type: Exectution) {

    enum Exectution {
        TRIGGER, TICK
    }

    data class Material(val rank: Int, val range: Int = 0)

    data class Context(val target: LivingEntity, val spell: SpellStack, val at: Vec3d?, private val by: UUID?) {
        fun getSource(): UUID? {
            return by ?: spell.owner
        }
    }

    companion object {
        fun attempCast(ctx: Context): Boolean {
            val protection = findProtected(ctx);

            if(protection != null) {
                protection.onSuccess(ctx)
                return false
            } else {
                ctx.spell.spell.apply(ctx)
                return true
            }
        }

        fun findProtected(ctx: Context): Protection {
            return false;
        }
    }

    abstract getCooldown(stack: SpellStack): Int?

    abstract getRank(stack: SpellStack): Int

    protected abstract fun apply(ctx: Context)

}