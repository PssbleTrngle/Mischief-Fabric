package com.possible_triangle.mischief.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.possible_triangle.mischief.block.SpellableBlock
import com.possible_triangle.mischief.item.Powder
import com.possible_triangle.mischief.item.SpellableItem
import com.possible_triangle.mischief.spell.Spell
import com.possible_triangle.mischief.spell.SpellStack
import com.possible_triangle.mischief.spell.Spells
import net.minecraft.command.arguments.BlockPosArgumentType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.CommandSource
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier
import java.lang.IllegalArgumentException
import java.util.concurrent.CompletableFuture

object DisguiseCommand {

    private val INVALID_ITEM = SimpleCommandExceptionType(TranslatableText("arguments.mischief.item.invalid"))

    fun register(): LiteralArgumentBuilder<ServerCommandSource> {
        return CommandManager.literal("disguise").executes(DisguiseCommand::disguise)
                .then(CommandManager.literal("clear").executes(DisguiseCommand::clear))
    }

    private fun clear(ctx: CommandContext<ServerCommandSource>): Int {

        val held = ctx.source.player.itemsHand.find { s -> !s.isEmpty } ?: ItemStack.EMPTY
        if(held.isEmpty) INVALID_ITEM.create()

        Powder.clearDisguise(held)

        return 1
    }

    private fun disguise(ctx: CommandContext<ServerCommandSource>): Int {

        val main = ctx.source.player.mainHandStack
        val off = ctx.source.player.offHandStack

        if(main.isEmpty || off.isEmpty) INVALID_ITEM.create()

        try {
           Powder.disguise(main, off)
        } catch (e: IllegalArgumentException) {
            throw INVALID_ITEM.create()
        }

        return 1
    }

}