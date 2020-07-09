package com.possible_triangle.mischief.item

import com.possible_triangle.mischief.spell.Spell
import com.possible_triangle.mischief.spell.SpellStack
import net.minecraft.client.render.item.ItemModels
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class Powder(material: Spell.Material, settings: Settings = Settings()) : SpellableItem(material, settings) {

    companion object {
        private val ID = "disguise"

        fun disguise(powder: ItemStack, other: ItemStack) {
            val disguise = other.toTag(CompoundTag())
            disguise.remove(ID)
            powder.putSubTag(ID, disguise)
        }

        fun clearDisguise(powder: ItemStack) {
            powder.removeSubTag(ID)
        }

        fun getDisguise(powder: ItemStack): ItemStack? {
            val disguise = powder.getSubTag(ID) ?: return null
            disguise.remove(ID)
            return ItemStack.fromTag(disguise)
        }
    }

    override fun canCast(type: Spell.Type): Boolean {
        return true
    }

    override fun tickSpell(spell: SpellStack, stack: ItemStack, world: World, entity: LivingEntity, slot: Int, selected: Boolean): Boolean {
        return spell.cast(entity, at = entity.pos, type = Spell.Type.TICK)
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)
        val spell = getSpell(stack) ?: return TypedActionResult.pass(stack)

        val casted = spell.cast(user, at = user.pos, type = Spell.Type.TRIGGER)
        return if (casted) {
            user.itemCooldownManager.set(stack.item, spell.totalCooldown)
            TypedActionResult.success(stack)
        } else TypedActionResult.fail(stack)

    }

}