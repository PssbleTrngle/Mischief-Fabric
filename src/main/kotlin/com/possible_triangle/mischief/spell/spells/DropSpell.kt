package com.possible_triangle.mischief.spell.spells

import com.possible_triangle.mischief.spell.Spell

class DropSpell : Spell() {

    override fun apply(ctx: Context) {
        val drop = ctx.target.activeItem
        if(drop != null) ctx.target.dropStack(drop)
    }

}