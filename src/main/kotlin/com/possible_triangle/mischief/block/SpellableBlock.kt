package com.possible_triangle.mischief.block;

import net.minecraft.block.Block;
import com.possible_triangle.mischief.Content;
import com.possible_triangle.mischief.spell.Spell;
import com.possible_triangle.mischief.spell.ISpellable;

abstract class SpellableBlock(private val spellMaterial: Spell.Material, settings: Settings) : Block(settings), ISpellable {

    override fun getMaterial(): Spell.Material {
        return spellMaterial
    }

}