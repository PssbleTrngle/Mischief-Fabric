package com.possible_triangle.mischief.item

import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.possible_triangle.mischief.Content
import com.possible_triangle.mischief.spell.Spell
import com.possible_triangle.mischief.spell.SpellStack
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import java.lang.IllegalArgumentException

abstract class SpelledItem(private val material: Spell.Material, settings: Settings? = null) : Item((settings ?: Settings()).group(Content.GROUP)) {

    companion object {
        fun setCurse(spell: SpellStack, item: ItemStack) {
            if(item.isEmpty) throw IllegalArgumentException("No Item given")
            if(item.item is SpelledItem) throw IllegalArgumentException("Given item can not have a spell applied")
            item.orCreateTag.put("spell", spell.serialize())
        }

        fun getCurse(item: ItemStack): SpellStack? {
            if(item.orCreateTag.contains("spell"))
                return SpellStack.deserialize(item.orCreateTag.getCompound("spell"))
            return null
        }
    }

}