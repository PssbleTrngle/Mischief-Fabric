package com.possible_triangle.mischief.spell.spells

import com.possible_triangle.mischief.spell.Spell
import com.possible_triangle.mischief.spell.SpellStack

class DrownSpell : Spell(Type.TICK) {

    override fun apply(ctx: Context) {

    }

    override fun getCooldown(stack: SpellStack): Int {
        return 20 * (4 + maxPower() - stack.power)
    }

    override fun getRank(stack: SpellStack): Int {
        return stack.power + 1
    }

    override fun maxPower(): Int {
        return 3
    }
}