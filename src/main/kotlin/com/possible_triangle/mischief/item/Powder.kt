package com.possible_triangle.mischief.item

import com.possible_triangle.mischief.spell.Spell
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class Powder(material: Spell.Material, settings: Settings = Settings()) : SpellableItem(material, settings) {

    override fun canCast(type: Spell.Type): Boolean {
        return true
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        val spell = getSpell(stack) ?: return
        spell.tick()
        if (entity is LivingEntity) spell.cast(entity, at = entity.pos, type = Spell.Type.TICK)
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)
        val spell = getSpell(stack) ?: return TypedActionResult.pass(stack)

        val casted = spell.cast(user, at = user.pos, type = Spell.Type.TRIGGER)
        return if (casted) {
            user.itemCooldownManager.set(stack.item, spell.getCooldownTotal())
            TypedActionResult.success(stack)
        } else TypedActionResult.fail(stack)

    }

}