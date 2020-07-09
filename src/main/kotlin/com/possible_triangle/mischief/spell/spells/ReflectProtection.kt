package com.possible_triangle.mischief.spell.spells

import com.possible_triangle.mischief.spell.Protection
import com.possible_triangle.mischief.spell.SpellStack
import com.possible_triangle.mischief.spell.Spells
import net.minecraft.entity.LivingEntity
import net.minecraft.server.network.ServerPlayerEntity
import org.apache.logging.log4j.core.jmx.Server

class ReflectProtection : Protection()  {

    override fun protects(protection: SpellStack, ctx: Context): Boolean {
        return ctx.spell.rank < protection.rank
    }

    override fun onSuccess(protection: SpellStack, ctx: Context) {
        if(ctx.source != null && ctx.target.uuid != ctx.source) {
            val source = ctx.world.getEntity(ctx.source) ?: return
            val reflected = SpellStack(ctx.spell.spell, ctx.spell.power, ctx.spell.owner, piercing = true)
            reflected.cast(source as LivingEntity, ctx.target as ServerPlayerEntity, ctx.target.pos)
        }
    }

    override fun getRank(stack: SpellStack): Int {
        return stack.power
    }

    override fun maxPower(): Int {
        return 5
    }
}