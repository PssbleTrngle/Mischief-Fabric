package com.possible_triangle.mischief

import com.possible_triangle.mischief.block.SpellableBlock
import com.possible_triangle.mischief.spell.Spell
import com.possible_triangle.mischief.spell.Spell.Material
import com.possible_triangle.mischief.item.Powder
import com.possible_triangle.mischief.block.Totem
import com.possible_triangle.mischief.block.tile.TotemTile
import com.possible_triangle.mischief.item.SpellableBlockItem
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.util.function.Function
import java.util.function.Supplier

object Content {

    fun init() {
        // Required to load class
    }

    fun id(path: String): Identifier {
        return Identifier(Mischief.MODID, path);
    }

    val GROUP = FabricItemGroupBuilder.build(id("items")) { ItemStack(EXPERIMENTAL_POWDER) }!!

    val EXPERIMENTAL_POWDER = Registry.register(Registry.ITEM, id("experimental_powder"), Powder(Material(3)))!!
    val NATURAL_POWDER = Registry.register(Registry.ITEM, id("natural_powder"), Powder(Material(2)))!!

    val BLACKSTONE_TOTEM = registerBlock(id("blackstone_totem"), Totem(Material(4, 2), AbstractBlock.Settings.copy(Blocks.BLACKSTONE)), Function { b -> SpellableBlockItem(b) })
    val MARBLE_TOTEM = registerBlock(id("marble_totem"), Totem(Material(3, 3), AbstractBlock.Settings.copy(Blocks.QUARTZ_BLOCK)), Function { b -> SpellableBlockItem(b) })

    val TOTEM_TILE_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, id("totem"),
            BlockEntityType.Builder.create(Supplier { TotemTile() }, BLACKSTONE_TOTEM
    ).build(null))!!

    private fun <B : Block, I : BlockItem> registerBlock(id: Identifier, block: B, item: Function<B, I>): B {
        val block = Registry.register(Registry.BLOCK, id, block)
        Registry.register(Registry.ITEM, id, item.apply(block))
        return block
    }

}