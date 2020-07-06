package com.possible_triangle.mischief

import com.possible_triangle.mischief.spell.Spell
import com.possible_triangle.mischief.item.Powder
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.registry.Registry

object Content {

    val MODID = "mischief"

    val GROUP = object : ItemGroup(-1, MODID) {
        override fun createIcon(): ItemStack {
            return ItemStack(EXPERIMENTAL_POWDER, 1)
        }
    }

    val EXPERIMENTAL_POWDER = Registry.register(Registry.ITEM, "experimental_powder", Powder(Spell.Material(3)))
    val NATURAL_POWDER = Registry.register(Registry.ITEM, "natural_powder", Powder(Spell.Material(2)))

}