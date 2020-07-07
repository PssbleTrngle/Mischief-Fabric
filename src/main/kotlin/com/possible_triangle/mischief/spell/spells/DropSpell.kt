package com.possible_triangle.mischief.spell.spells

import com.possible_triangle.mischief.spell.Spell
import com.possible_triangle.mischief.spell.SpellStack

class DropSpell : Spell(Type.TRIGGER) {

    override fun apply(ctx: Context) {
        val drop = ctx.target.activeItem
        if(drop != null) ctx.target.dropStack(drop)
    }

    override fun getCooldown(stack: SpellStack): Int? {
        return 20 * 5
    }

    override fun getRank(stack: SpellStack): Int {
        return stack.power * 4
    }

    override fun maxPower(): Int {
        return 2
    }
}