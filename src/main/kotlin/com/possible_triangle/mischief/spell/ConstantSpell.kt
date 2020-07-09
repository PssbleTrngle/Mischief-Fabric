package com.possible_triangle.mischief.spell

abstract class ConstantSpell : Spell(Type.TICK) {

    override fun getCooldown(stack: SpellStack): Int {
        return 0
    }

}