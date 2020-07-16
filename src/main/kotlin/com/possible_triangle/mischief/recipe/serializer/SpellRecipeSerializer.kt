package com.possible_triangle.mischief.recipe.serializer

import com.google.gson.JsonObject
import com.possible_triangle.mischief.recipe.SpellRecipe
import com.possible_triangle.mischief.spell.SpellStack
import com.possible_triangle.mischief.spell.Spells
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.registry.Registry

class SpellRecipeSerializer : RecipeSerializer<SpellRecipe> {

    override fun read(id: Identifier, json: JsonObject): SpellRecipe {
        val spell = Spells.REGISTRY.get(Identifier(json.get("spell").asString))!!
        val ingredients = json.getAsJsonArray("ingredients")!!.map { Ingredient.fromJson(it) }
        return SpellRecipe(id, spell, ingredients)
    }

    override fun write(buf: PacketByteBuf, recipe: SpellRecipe) {
        buf.writeString(Spells.REGISTRY.getId(recipe.spell)!!.toString())
        buf.writeInt(recipe.ingredients.size)
        recipe.ingredients.forEach { it.write(buf) }
    }

    override fun read(id: Identifier, buf: PacketByteBuf): SpellRecipe {
        val spell = Spells.REGISTRY.get(Identifier(buf.readString()))!!
        val ingredients = Array(buf.readInt()) { Ingredient.fromPacket(buf) }.asList()
        return SpellRecipe(id, spell, ingredients)
    }

}