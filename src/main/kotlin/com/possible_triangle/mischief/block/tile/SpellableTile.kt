package com.possible_triangle.mischief.block.tile;

import com.possible_triangle.mischief.block.SpellableBlock
import com.possible_triangle.mischief.block.SpellableBlock.State
import com.possible_triangle.mischief.spell.*
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.nbt.CompoundTag
import net.minecraft.particle.DustParticleEffect
import net.minecraft.particle.ParticleEffect
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Tickable
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d

abstract class SpellableTile(type: BlockEntityType<*>) : BaseTile(type), ISpellableReference, Tickable, BlockEntityClientSerializable {

    var spell: SpellStack? = null
        private set

    val range: Box by lazy {
        Box(pos).expand(getMaterial().range.toDouble())
    }

    val blockRange: BlockBox by lazy {
        BlockBox(range.minX.toInt(), range.minY.toInt(), range.minZ.toInt(), range.maxX.toInt(), range.maxY.toInt(), range.maxZ.toInt())
    }

    fun setSpell(spell: SpellStack?) {
        if(spell != null && !canHold(spell)) throw IllegalArgumentException("Tile cannot hold spell")
        this.spell = spell
        markDirty()

        val world = this.world ?: return
        //world.setBlockState(pos, world.getBlockState(pos).with(SpellableBlock.STATE, State.from(spell)), 4)
    }

    override fun tick() {
        val ticked = spell?.tick() == true
        val casted = update(spell)
        if(ticked || casted) markDirty()
        if(casted) spawnParticles()
    }

    /**
     * @return Whether the spell has been cast
     */
    abstract fun update(spell: SpellStack?): Boolean

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

    override fun toClientTag(tag: CompoundTag): CompoundTag {
        return toTag(tag)
    }

    override fun fromClientTag(tag: CompoundTag) {
    }

    fun particleType(): ParticleEffect {
        val color = spell?.spell?.color ?: Spells.NONE_COLOR
        return DustParticleEffect(color.red / 255F, color.blue / 255F, color.green / 255F, 1F)
    }

    fun spawnParticles(count: Int = 10) {
        val world = this.world
        if(world is ServerWorld) {
            world.spawnParticles(particleType(), pos.x.toDouble() + 0.5, pos.y.toDouble() + 0.5, pos.z.toDouble() + 0.5, count, 0.5, 0.5, 0.5, 0.0)
        }
    }

}