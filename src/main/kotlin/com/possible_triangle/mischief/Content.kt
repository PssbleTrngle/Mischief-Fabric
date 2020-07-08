package com.possible_triangle.mischief

import com.possible_triangle.mischief.block.SpellableBlock
import com.possible_triangle.mischief.spell.Spell
import com.possible_triangle.mischief.spell.Spell.Material
import com.possible_triangle.mischief.item.Powder
import com.possible_triangle.mischief.block.Totem
import com.possible_triangle.mischief.item.SpellableBlockItem
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.registry.Registry
import java.util.function.Function

object Content {

    val MODID = "mischief"

    val GROUP = object : ItemGroup(-1, MODID) {
        override fun createIcon(): ItemStack {
            return ItemStack(EXPERIMENTAL_POWDER, 1)
        }
    }

    val EXPERIMENTAL_POWDER = Registry.register(Registry.ITEM, "experimental_powder", Powder(Material(3)))!!
    val NATURAL_POWDER = Registry.register(Registry.ITEM, "natural_powder", Powder(Material(2)))!!

    val BLACKSTONE_TOTEM = registerBlock("blackstone_totem", Totem(Material(4, 2), AbstractBlock.Settings.copy(Blocks.BLACKSTONE)), Function { b -> SpellableBlockItem(b)})
    val MARBLE_TOTEM = registerBlock("marble_totem", Totem(Material(3, 3), AbstractBlock.Settings.copy(Blocks.QUARTZ_BLOCK)), Function { b -> SpellableBlockItem(b) })

    private fun <B : Block, I : BlockItem> registerBlock(id: String, block: B, item: Function<B,I>) {
        Registry.register(Registry.BLOCK, id, block)
        Registry.register(Registry.ITEM, id, item.apply(block))
    }

}