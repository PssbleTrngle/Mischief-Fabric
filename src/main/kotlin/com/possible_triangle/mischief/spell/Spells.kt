package com.possible_triangle.mischief.spell

import com.possible_triangle.mischief.Content
import com.possible_triangle.mischief.spell.spells.DropSpell
import com.possible_triangle.mischief.spell.spells.DrownSpell
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.event.registry.RegistryAttribute
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey

object Spells {

    private val KEY = RegistryKey.ofRegistry<Spell>(Identifier(Content.MODID, "spell"))
    val REGISTRY = FabricRegistryBuilder.createSimple(Spell::class.java, Identifier(Content.MODID, "spell"))
        .attribute(RegistryAttribute.MODDED)
        .buildAndRegister()

    val DROP = Registry.register(REGISTRY, "drop", DropSpell())
    val DROWN = Registry.register(REGISTRY, "drown", DrownSpell())

}