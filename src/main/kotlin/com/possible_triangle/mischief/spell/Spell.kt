package com.possible_triangle.mischief.spell

import net.minecraft.entity.LivingEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Vec3d
import java.awt.Color
import java.util.*
import kotlin.reflect.KClass

abstract class Spell(val type: Type, val bad: Boolean = true) {

    enum class Type {
        TRIGGER, TICK
    }

    data class Material(val rank: Int, val range: Int = 0)

    data class Context(val world: ServerWorld, val target: LivingEntity, val spell: SpellStack, val at: Vec3d?, private val by: UUID?) {
        val source: UUID? by lazy {
            by ?: spell.owner
        }
    }

    abstract fun getCooldown(stack: SpellStack): Int

    abstract fun getRank(stack: SpellStack): Int

    abstract fun maxPower(): Int

    internal abstract fun apply(ctx: Context)

    open fun affects(): KClass<out LivingEntity> {
        return LivingEntity::class
    }

    val color: Color by lazy {
        if(bad) Color(0xCB110C)
        else Color(0x08DE89)
    }

}