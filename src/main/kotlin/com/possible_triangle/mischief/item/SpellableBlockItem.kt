package com.possible_triangle.mischief.item

import com.possible_triangle.mischief.Content
import com.possible_triangle.mischief.block.SpellableBlock
import com.possible_triangle.mischief.spell.ISpellable
import com.possible_triangle.mischief.spell.ISpellableReference
import com.possible_triangle.mischief.spell.Spell
import net.minecraft.block.Block
import net.minecraft.item.BlockItem

class SpellableBlockItem(block: SpellableBlock, settings: Settings = Settings()) :
    BlockItem(block, settings.group(Content.GROUP)) , ISpellableReference {

    override fun getBlock(): SpellableBlock {
        return super.getBlock() as SpellableBlock
    }

    override fun getReferent(): ISpellable {
        return block
    }

}