package com.possible_triangle.mischief.block.tile;

import com.possible_triangle.mischief.spell.ISpellable
import com.possible_triangle.mischief.spell.Spell
import com.possible_triangle.mischief.spell.SpellStack
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.Tickable
import net.minecraft.util.math.Vec3d

abstract class SpellableTile(type: BlockEntityType<*>) : BlockEntity(type), ISpellable, Tickable {

    var spell: SpellStack? = null
        set(spell: SpellStack?) {
            field = spell
            markDirty()
        }

    override fun tick() {
        if(spell?.tick() == true) markDirty()
        update()
    }

    abstract fun update()

    fun getBlock(): ISpellable {
        val block = this.cachedState.block
        if(block is ISpellable) return block
        throw IllegalArgumentException("The block of a spellable tile must also implement `ISpellable`")
    }

    override fun getMaterial(): Spell.Material {
        return getBlock().getMaterial()
    }

    override fun canCast(type: Spell.Type): Boolean {
        return getBlock().canCast(type)
    }

    fun getSpellSource(): Vec3d {
        return Vec3d(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
    }

}