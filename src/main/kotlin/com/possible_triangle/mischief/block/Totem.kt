package com.possible_triangle.mischief.block;

import com.possible_triangle.mischief.Content;
import com.possible_triangle.mischief.spell.Spell;
import com.possible_triangle.mischief.spell.ISpellable;

class Totem(material: Spell.Material, settings: Settings) : SpellableBlock(material, settings) {

    override fun canCast(type: Spell.Type): Boolean {
        return true
    }
}