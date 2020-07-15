package com.possible_triangle.mischief.recipe.serializer

import com.google.gson.JsonObject
import com.possible_triangle.mischief.recipe.CarvingRecipe
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

class CarvingRecipeSerializer : RecipeSerializer<CarvingRecipe> {

    override fun read(id: Identifier, json: JsonObject): CarvingRecipe {
        val input = Ingredient.fromJson(json.get("block"))
        val output = Registry.BLOCK.get(Identifier(json.get("result").asString))
        return CarvingRecipe(id, input, output)
    }

    override fun write(buf: PacketByteBuf, recipe: CarvingRecipe) {
        recipe.input.write(buf)
        buf.writeString(Registry.BLOCK.getId(recipe.output).toString())
    }

    override fun read(id: Identifier, buf: PacketByteBuf): CarvingRecipe {
        val input = Ingredient.fromPacket(buf)
        val output = Registry.BLOCK.get(Identifier(buf.readString()))
        return CarvingRecipe(id, input, output)
    }

}