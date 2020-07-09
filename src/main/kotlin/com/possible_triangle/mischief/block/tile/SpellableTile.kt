package com.possible_triangle.mischief.block.tile;

import com.possible_triangle.mischief.block.SpellableBlock
import com.possible_triangle.mischief.block.SpellableBlock.State
import com.possible_triangle.mischief.spell.ISpellable
import com.possible_triangle.mischief.spell.ISpellableReference
import com.possible_triangle.mischief.spell.Spell
import com.possible_triangle.mischief.spell.SpellStack
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Tickable
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d

abstract class SpellableTile(type: BlockEntityType<*>) : BlockEntity(type), ISpellableReference, Tickable {

    var spell: SpellStack? = null
        private set

    val range: Box by lazy {
        Box(pos).expand(getMaterial().range.toDouble())
    }

    fun setSpell(spell: SpellStack?) {
        if(spell != null && !canHold(spell)) throw IllegalArgumentException("Tile cannot hold spell")
        this.spell = spell
        markDirty()

        val world = this.world ?: return
        world.setBlockState(pos, world.getBlockState(pos).with(SpellableBlock.STATE, State.from(spell)))
    }

    override fun tick() {
        if(spell?.tick() == true) markDirty()
        update()
    }

    abstract fun update()

    override fun getReferent(): ISpellable {
        val block = this.cachedState.block
        if(block is ISpellable) return block
        throw IllegalArgumentException("The block of a spellable tile must also implement `ISpellable`")
    }

    val spellSource: Vec3d by lazy {
        Vec3d(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
    }

    override fun toTag(tag: CompoundTag): CompoundTag {
        val t = super.toTag(tag)
        val spell = this.spell
        if(spell != null) t.put("spell", spell.serialize())
        return t
    }

    override fun fromTag(state: BlockState, tag: CompoundTag) {
        super.fromTag(state, tag)
        if(tag.contains("spell")) this.spell = SpellStack.deserialize(tag.getCompound("spell"))
    }

}