package com.possible_triangle.mischief.spell.spells

import com.possible_triangle.mischief.spell.Spell
import com.possible_triangle.mischief.spell.SpellStack
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import kotlin.reflect.KClass

class DropSpell : Spell(Type.TRIGGER) {

    override fun apply(ctx: Context) {
        val player = ctx.target as PlayerEntity
        val slot = player.inventory.selectedSlot
        player.dropItem(player.inventory.removeStack(slot, 1), true)
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

    override fun affects(): KClass<out LivingEntity> {
        return PlayerEntity::class
    }

}