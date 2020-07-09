package com.possible_triangle.mischief.spell.spells

import com.possible_triangle.mischief.spell.Spell
import com.possible_triangle.mischief.spell.SpellStack
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack

class BounceSpell : Spell(Type.TICK) {

    override fun apply(ctx: Context) {
        if(ctx.target is PlayerEntity) ctx.target.jump()
        else ctx.target.setJumping(true)
    }

    override fun getCooldown(stack: SpellStack): Int {
        return 20 * 4 * (maxPower() - stack.power)
    }

    override fun getRank(stack: SpellStack): Int {
        return stack.power * 2
    }

    override fun maxPower(): Int {
        return 2
    }
}