package com.possible_triangle.mischief.spell

import com.google.common.collect.Lists
import com.google.common.collect.Maps
import com.possible_triangle.mischief.spell.data.SpellDataHandler
import com.possible_triangle.mischief.spell.data.SpellsDataHandler
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.player.PlayerEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import java.util.function.BiFunction
import java.util.function.Function
import java.util.stream.Stream

abstract class Protection : ConstantSpell() {

    abstract fun protects(protection: SpellStack, ctx: Context): Boolean

    open fun onSuccess(protection: SpellStack, ctx: Context) {}

    override fun apply(ctx: Context) {
        MAP.computeIfAbsent(ctx.target) { Maps.newHashMap() }[ctx.spell] = 2
    }

    companion object {

        private val MAP =  Maps.newHashMap<LivingEntity,HashMap<SpellStack,Int>>()

        fun tick(entity: LivingEntity) {
            MAP[entity]?.replaceAll { _, i -> i - 1 }
            MAP[entity] = MAP[entity]?.filterTo(Maps.newHashMap()) { e -> e.value > 0 }
        }

        fun forEntity(entity: LivingEntity): List<SpellStack>? {
            return MAP[entity]?.map { e -> e.key }
        }

        fun find(ctx: Context): SpellStack? {
            return forEntity(ctx.target)?.firstOrNull { p -> p.spell is Protection && p.spell.protects(p, ctx) }
        }

    }

}