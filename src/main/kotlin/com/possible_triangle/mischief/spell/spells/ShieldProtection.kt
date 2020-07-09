package com.possible_triangle.mischief.spell.spells

import com.possible_triangle.mischief.spell.Protection
import com.possible_triangle.mischief.spell.SpellStack
import com.possible_triangle.mischief.spell.Spells
import net.minecraft.entity.LivingEntity
import net.minecraft.server.network.ServerPlayerEntity
import org.apache.logging.log4j.core.jmx.Server

class ShieldProtection : Protection()  {

    override fun protects(protection: SpellStack, ctx: Context): Boolean {
        return ctx.spell.rank <= protection.rank
    }

    override fun getRank(stack: SpellStack): Int {
        return stack.power + 2
    }

    override fun maxPower(): Int {
        return 3
    }
}