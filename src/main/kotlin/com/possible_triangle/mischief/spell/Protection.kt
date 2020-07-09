package com.possible_triangle.mischief.spell

import com.google.common.collect.Lists
import com.possible_triangle.mischief.spell.data.SpellDataHandler
import com.possible_triangle.mischief.spell.data.SpellsDataHandler
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.player.PlayerEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import java.util.stream.Stream

abstract class Protection : Spell(Type.TICK) {

    abstract fun protects(ctx: Context): Boolean

    open fun onSuccess(ctx: Context) {}

    override fun apply(ctx: Context) {
        ctx.target.dataTracker.set(KEY, ctx.target.dataTracker.get(KEY).plus(ctx.spell))
    }

    companion object {

        val KEY = DataTracker.registerData(PlayerEntity::class.java, SpellsDataHandler())

        fun forEntity(entity: LivingEntity): List<Protection> {
            return entity.dataTracker.get(KEY).map { s -> s.spell }.filterIsInstance<Protection>()
        }

        fun find(ctx: Context): Protection? {
            return forEntity(ctx.target).firstOrNull { p -> p.protects(ctx) }
        }

    }

}