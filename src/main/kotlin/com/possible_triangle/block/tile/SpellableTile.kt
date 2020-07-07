package com.possible_triangle.block.tile;

import net.minecraft.block.Block;
import com.possible_triangle.Content;
import com.possible_triangle.spell.Spell;
import com.possible_triangle.spell.ISpellable;

class SpellableTile(type: TileEntityType<*>) : TileEntity(type), ISpellable, ITickable {

    var spell: SpellStack?
        set(spell: SpellStack?) {
            field = spell
            markDirty()
        }

    override final fun update() {
        val spell = getSpell()
        if(spell?.tick() == true) markDirty()

        tick()
    }

    abstract fun tick()

    override fun getMaterial(): Spell.Material {
        if(this.block is ISpellable) return this.block.getMaterial()
        throw IllegalArgumentException("The block of a spellable tile must also implement `ISpellable`")
    }

}