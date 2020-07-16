package com.possible_triangle.mischief.recipe

import com.possible_triangle.mischief.Content
import com.possible_triangle.mischief.block.tile.CarvingTableTile
import com.possible_triangle.mischief.block.tile.InfuserTile
import com.possible_triangle.mischief.block.tile.InfuserTile.Companion.POWER
import com.possible_triangle.mischief.block.tile.InfuserTile.Companion.SPELLABLE
import com.possible_triangle.mischief.block.tile.InfuserTile.Companion.INGREDIENTS
import com.possible_triangle.mischief.block.tile.inventory.CarvingInventory
import com.possible_triangle.mischief.item.SpellableItem
import com.possible_triangle.mischief.spell.ISpellable
import com.possible_triangle.mischief.spell.Spell
import com.possible_triangle.mischief.spell.SpellStack
import com.possible_triangle.mischief.spell.Spells
import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

class SpellRecipe(private val identifier: Identifier, val spell: Spell, val ingredients: List<Ingredient>) : Recipe<InfuserTile> {

    companion object {
        private val EXAMPLE = Content.EXPERIMENTAL_POWDER
        val POWER_ITEMS = listOf(Items.BEETROOT, Items.CHORUS_FRUIT, Items.DRAGON_BREATH).map { Ingredient.ofItems(it) }
    }

    fun spell(inv: InfuserTile): SpellStack? {
        val powerStack = inv.getStack(POWER)
        val power = POWER_ITEMS.indexOfLast { it.test(powerStack) }
        if(power <= 0) return null
        return SpellStack(spell, power)
    }

    override fun craft(inv: InfuserTile): ItemStack {
        val spellable = inv.getStack(SPELLABLE).copy()
        SpellableItem.setSpell(spell(inv)!!, spellable)
        return spellable
    }

    override fun getId(): Identifier {
        return identifier;
    }

    override fun getGroup(): String {
        return Spells.REGISTRY.getId(spell).toString()
    }

    override fun getType(): RecipeType<*> {
        return Content.SPELL_RECIPE_TYPE
    }

    override fun fits(width: Int, height: Int): Boolean {
        return true
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return Content.SPELL_RECIPE_SERIALIZER
    }

    override fun getOutput(): ItemStack {
        val stack = ItemStack(EXAMPLE)
        SpellableItem.setSpell(SpellStack(spell, 1), stack)
        return stack
    }

    override fun matches(inv: InfuserTile, world: World): Boolean {
        val spellable = inv.getStack(SPELLABLE).item
        val stacks = INGREDIENTS.map { inv.getStack(it) }.map { it.copy() }
        val spell = spell(inv)

        return POWER_ITEMS.any{ it.test(inv.getStack(POWER)) }
                && spellable is ISpellable
                && spell != null
                && spellable.canHold(spell)
                && ingredients.all { stacks.any { s -> it.test(s) } }
                && ingredients.map { stacks.count { s -> it.test(s) } }.reduce { a, b -> a + b} >= ingredients.size
    }

}