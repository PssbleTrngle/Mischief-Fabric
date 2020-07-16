package com.possible_triangle.mischief

import com.possible_triangle.mischief.block.*
import com.possible_triangle.mischief.block.tile.CarvingTableTile
import com.possible_triangle.mischief.block.tile.DreamcatcherTile
import com.possible_triangle.mischief.block.tile.InfuserTile
import com.possible_triangle.mischief.block.tile.TotemTile
import com.possible_triangle.mischief.item.Powder
import com.possible_triangle.mischief.item.SpellableBlockItem
import com.possible_triangle.mischief.recipe.CarvingRecipe
import com.possible_triangle.mischief.recipe.SpellRecipe
import com.possible_triangle.mischief.recipe.serializer.CarvingRecipeSerializer
import com.possible_triangle.mischief.recipe.serializer.SpellRecipeSerializer
import com.possible_triangle.mischief.spell.Spell.Material
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeType
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.util.function.Supplier

object Content {

    fun init() {
        // Required to load class
    }

    fun id(path: String): Identifier {
        return Identifier(Mischief.MODID, path);
    }

    val GROUP = FabricItemGroupBuilder.build(id("items")) { ItemStack(EXPERIMENTAL_POWDER) }!!

    val CARVING_TABLE = registerBlock(id("carving_table"), CarvingTable(), { b -> BlockItem(b, Item.Settings().group(GROUP)) })

    val EXPERIMENTAL_POWDER = Registry.register(Registry.ITEM, id("experimental_powder"), Powder(Material(3)))!!
    val NATURAL_POWDER = Registry.register(Registry.ITEM, id("natural_powder"), Powder(Material(2)))!!

    val BLACKSTONE_TOTEM = registerSpellableBlock(id("blackstone_totem"), Totem(Material(4, 2), Blocks.BLACKSTONE))
    val MARBLE_TOTEM = registerSpellableBlock(id("marble_totem"), Totem(Material(3, 3), Blocks.QUARTZ_BLOCK))
    val totems = listOf(BLACKSTONE_TOTEM, MARBLE_TOTEM).map { it to it.parent }.toMap()

    val COSMIC_DREAMCATCHER = registerSpellableBlock(id("cosmic_dreamcatcher"), Dreamcatcher(Material(2, 3)))
    val dreamcatchers = listOf(COSMIC_DREAMCATCHER)

    val INFUSER = registerBlock(id("infuser"), Infuser(), { b -> BlockItem(b, Item.Settings().group(GROUP)) })

    val TOTEM_TILE_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, id("totem"),
            BlockEntityType.Builder.create(Supplier { TotemTile() }, *totems.keys.toTypedArray()).build(null))!!

    val DREAMCATCHER_TILE_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, id("dreamcatcher"),
            BlockEntityType.Builder.create(Supplier { DreamcatcherTile() }, *dreamcatchers.toTypedArray()).build(null))!!

    val CARVING_TABLE_TILE_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, id("carving_table"),
            BlockEntityType.Builder.create(Supplier { CarvingTableTile() }, CARVING_TABLE).build(null))!!

    val INFUSER_TILE_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, id("infuser"),
            BlockEntityType.Builder.create(Supplier { InfuserTile() }, INFUSER).build(null))!!

    val CARVING = Registry.register(Registry.RECIPE_TYPE, id("carving"), object : RecipeType<CarvingRecipe> {})
    val CARVING_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, id("carving"), CarvingRecipeSerializer())!!

    val SPELL_RECIPE_TYPE = Registry.register(Registry.RECIPE_TYPE, id("spell"), object : RecipeType<SpellRecipe> {})
    val SPELL_RECIPE_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, id("spell"), SpellRecipeSerializer())!!

    private fun <B : SpellableBlock> registerSpellableBlock(id: Identifier, block: B): B {
        return registerBlock(id, block, { b -> SpellableBlockItem(b) })
    }

    private fun <B : Block, I : BlockItem> registerBlock(id: Identifier, block: B, item: (B) -> I): B {
        val block = Registry.register(Registry.BLOCK, id, block)
        Registry.register(Registry.ITEM, id, item(block))
        return block
    }

}