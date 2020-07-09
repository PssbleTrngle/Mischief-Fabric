package com.possible_triangle.mischief

import com.possible_triangle.mischief.command.MischiefCommand
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.data.DataGenerator

@Suppress("unused")
class Mischief : ModInitializer {

    companion object {
        const val MODID = "mischief"
    }

    override fun onInitialize() {
        println("Ready for Mischief!")
        Content.init()
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { d, _ -> MischiefCommand.register(d) })
    }

}

