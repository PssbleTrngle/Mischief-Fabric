package com.possible_triangle.mischief.spell.spells

import com.possible_triangle.mischief.spell.Spell
import com.possible_triangle.mischief.spell.SpellStack
import net.minecraft.item.ItemStack

class DropSpell : Spell(Type.TRIGGER) {

    override fun apply(ctx: Context) {
        val held = ctx.target.itemsHand.find { s -> !s.isEmpty }
        if(held != null) ctx.target.dropStack(held)
    }

    override fun getCooldown(stack: SpellStack): Int {
        return 20 * 5
    }

    override fun getRank(stack: SpellStack): Int {
        return stack.power * 4
    }

    override fun maxPower(): Int {
        return 2
    }
}