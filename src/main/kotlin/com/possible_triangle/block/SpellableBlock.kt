package com.possible_triangle.block;

import net.minecraft.block.Block;
import com.possible_triangle.Content;
import com.possible_triangle.spell.Spell;
import com.possible_triangle.spell.ISpellable;

class SpellableBlock(val material: Spell.Material, settings: Settings) : Block(settings.group(Content.GROUP)), ISpellable {

    override fun getMaterial(): Spell.Material {
        return material;
    }

}