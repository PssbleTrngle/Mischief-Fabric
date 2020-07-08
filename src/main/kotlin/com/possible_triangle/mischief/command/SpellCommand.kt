package com.possible_triangle.mischief.command

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.possible_triangle.mischief.item.SpellableItem
import com.possible_triangle.mischief.spell.Spell
import com.possible_triangle.mischief.spell.SpellStack
import com.possible_triangle.mischief.spell.Spells
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.CommandSource
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier
import java.lang.IllegalArgumentException
import java.util.concurrent.CompletableFuture

object SpellCommand {

    private val INVALID_SPELL = DynamicCommandExceptionType { id -> TranslatableText("arguments.mischief.spell.invalid", id) }
    private val INVALID_ITEM = SimpleCommandExceptionType(TranslatableText("arguments.mischief.item.invalid"))

    class SpellArgumentType : ArgumentType<Spell> {
        companion object {
            fun getSpell(ctx: CommandContext<ServerCommandSource>, name: String): Spell {
                return ctx.getArgument(name, Spell::class.java)
            }
        }

        override fun parse(reader: StringReader?): Spell {
            val id = Identifier.fromCommandInput(reader)
            return Spells.REGISTRY.getOrEmpty(id).orElseThrow { INVALID_SPELL.create(id) }
        }

        override fun <S : Any?> listSuggestions(ctx: CommandContext<S>?, builder: SuggestionsBuilder?): CompletableFuture<Suggestions> {
            return CommandSource.suggestIdentifiers(Spells.REGISTRY.ids, builder)
        }
    }

    fun register(): LiteralArgumentBuilder<ServerCommandSource> {
        return CommandManager.literal("spell")
            .then(CommandManager.argument("spell", SpellArgumentType()).then(CommandManager.argument("power", IntegerArgumentType.integer(1, 100))
                .executes(SpellCommand::setSpell)))
    }

    private fun setSpell(ctx: CommandContext<ServerCommandSource>): Int {

        val power = IntegerArgumentType.getInteger(ctx, "power")
        val spell = SpellArgumentType.getSpell(ctx, "spell")
        val held = ctx.source.player.activeItem

        try {
            SpellableItem.setSpell(SpellStack(spell, power, if(ctx.source.entity is PlayerEntity) ctx.source.player.uuid else null), held)
        } catch (e: IllegalArgumentException) {
            throw INVALID_ITEM.create()
        }

        return 1
    }

}