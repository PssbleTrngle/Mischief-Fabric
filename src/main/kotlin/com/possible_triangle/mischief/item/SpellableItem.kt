package com.possible_triangle.mischief.item

import com.possible_triangle.mischief.Content
import com.possible_triangle.mischief.spell.ISpellable
import com.possible_triangle.mischief.spell.Spell
import com.possible_triangle.mischief.spell.SpellStack
import com.possible_triangle.mischief.spell.Spells
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.world.World

abstract class SpellableItem(private val material: Spell.Material, settings: Settings = Settings()) :
    Item(settings.group(Content.GROUP)), ISpellable {

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
    }

    override fun getMaterial(): Spell.Material {
        return material;
    }

    override fun hasGlint(stack: ItemStack): Boolean {
        return getSpell(stack) != null
    }

    @Environment(EnvType.CLIENT)
    override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
        val spell = getSpell(stack)

        if(spell != null) {
            val id = Spells.REGISTRY.getId(spell.spell)!!
            tooltip.add(TranslatableText("${id.namespace}.spell.${id.path}"))
            tooltip.add(TranslatableText("mischief.tooltip.cooldown", spell.getCooldown()))
            tooltip.add(TranslatableText("mischief.tooltip.power", spell.power))
            if(material.range > 0) tooltip.add(TranslatableText("mischief.tooltip.range", material.range))
        } else {
            tooltip.add(TranslatableText("mischief.tooltip.rank_up_to", material.rank))
        }
    }

}