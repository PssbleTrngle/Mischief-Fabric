package com.possible_triangle.mischief.spell.spells

import com.possible_triangle.mischief.spell.Spell
import com.possible_triangle.mischief.spell.SpellStack
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance

class EffectSpell(val effect: StatusEffect) : Spell(Type.TICK, !effect.isBeneficial) {

    override fun apply(ctx: Context) {
        val duration = getCooldown(ctx.spell) / maxPower() * ctx.spell.power + 5
        val amplifier = ctx.spell.power % 2
        ctx.target.applyStatusEffect(StatusEffectInstance(effect, duration, amplifier))
    }

    override fun getCooldown(stack: SpellStack): Int {
        return 20 * maxPower()
    }

    override fun getRank(stack: SpellStack): Int {
        return stack.power * 2 + 3
    }

    override fun maxPower(): Int {
        return 6
    }
}