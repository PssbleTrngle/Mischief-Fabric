package com.possible_triangle.mischief.recipe

import com.possible_triangle.mischief.Content
import com.possible_triangle.mischief.block.tile.CarvingTableTile
import com.possible_triangle.mischief.block.tile.inventory.CarvingInventory
import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

class CarvingRecipe(private val identifier: Identifier, val input: Ingredient, val output: Block) : Recipe<CarvingTableTile> {

    override fun craft(inv: CarvingTableTile): ItemStack {
        return getOutput()
    }

    override fun getId(): Identifier {
        return identifier;
    }

    override fun getGroup(): String {
        return Registry.BLOCK.getId(output).toString()
    }

    override fun getType(): RecipeType<*> {
        return Content.CARVING
    }

    override fun fits(width: Int, height: Int): Boolean {
        return true
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return Content.CARVING_SERIALIZER
    }

    override fun getOutput(): ItemStack {
        return ItemStack(output)
    }

    override fun matches(inv: CarvingTableTile, world: World): Boolean {
        return input.test(inv.getStack(CarvingInventory.INPUT))
    }

}