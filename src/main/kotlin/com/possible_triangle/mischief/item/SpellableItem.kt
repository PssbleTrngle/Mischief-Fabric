package com.possible_triangle.mischief.item

import com.possible_triangle.mischief.Content
import com.possible_triangle.mischief.spell.ISpellable
import com.possible_triangle.mischief.spell.Spell
import com.possible_triangle.mischief.spell.SpellStack
import com.possible_triangle.mischief.spell.Spells
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.world.World

abstract class SpellableItem(private val material: Spell.Material, settings: Settings = Settings()) :
    Item(settings.group(Content.GROUP).maxCount(1)), ISpellable {

    companion object {
        fun setSpell(spell: SpellStack?, item: ItemStack) {
            if (item.isEmpty) throw IllegalArgumentException("No Item given")

            val i = item.item
            if (i !is ISpellable) throw IllegalArgumentException("Given item can not have a spell applied")

            if (spell != null) {
                if (!i.canHold(spell)) throw IllegalArgumentException("Item can not hold spell of this type")
                item.orCreateTag.put("spell", spell.serialize())
            } else item.orCreateTag.remove("spell")
        }

        fun getSpell(item: ItemStack): SpellStack? {
            if (item.orCreateTag.contains("spell"))
                return SpellStack.deserialize(item.orCreateTag.getCompound("spell"))
            return null
        }

        fun appendTooltip(stack: ItemStack, tooltip: MutableList<Text>, context: TooltipContext) {
            val item = stack.item ?: return
            if(item !is ISpellable) return
            val spell = getSpell(stack)
            val material = item.getMaterial()

            if(spell != null) {
                val id = Spells.REGISTRY.getId(spell.spell)!!
                tooltip.add(TranslatableText("${id.namespace}.spell.${id.path}"))
                if(spell.getCooldown() > 0) tooltip.add(TranslatableText("mischief.tooltip.cooldown", spell.getCooldown()))
                tooltip.add(TranslatableText("mischief.tooltip.power", spell.power))
                if(material.range > 0) tooltip.add(TranslatableText("mischief.tooltip.range", material.range))
            } else {
                tooltip.add(TranslatableText("mischief.tooltip.rank_up_to", material.rank))
            }
        }
    }

    final override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        super.inventoryTick(stack, world, entity, slot, selected)
        val spell = getSpell(stack)
        if(spell != null) {
            val ticked = spell.tick()
            val casted = entity is LivingEntity && tickSpell(spell, stack, world, entity, slot, selected)
            if(ticked || casted) setSpell(spell, stack)
        }
    }

    /**
     * Do something with the spell after [SpellStack.tick] has been called
     * @return Whether the spell was cast
     */
    open fun tickSpell(spell: SpellStack, stack: ItemStack, world: World, entity: LivingEntity, slot: Int, selected: Boolean): Boolean {
        return false
    }

    override fun getMaterial(): Spell.Material {
        return material;
    }

    override fun hasGlint(stack: ItemStack): Boolean {
        return getSpell(stack) != null
    }

    @Environment(EnvType.CLIENT)
    override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
        appendTooltip(stack, tooltip, context)
    }

}