package com.possible_triangle.mischief.command

import com.mojang.brigadier.CommandDispatcher
import com.possible_triangle.mischief.Content
import com.possible_triangle.mischief.Mischief
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import javax.swing.text.AbstractDocument

object MischiefCommand {

    fun register(dispatched: CommandDispatcher<ServerCommandSource>) {
        dispatched.register(CommandManager.literal(Mischief.MODID)
            .then(SpellCommand.register())
            .then(DisguiseCommand.register())
        )
    }

}