package com.possible_triangle.mischief.spell

import com.possible_triangle.mischief.Content
import com.possible_triangle.mischief.spell.spells.DropSpell
import com.possible_triangle.mischief.spell.spells.DrownSpell
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.event.registry.RegistryAttribute
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey

interface ISpellable {

    fun getMaterial(): Spell.Material

    fun canCast(type: Spell.Execution): Boolean

}