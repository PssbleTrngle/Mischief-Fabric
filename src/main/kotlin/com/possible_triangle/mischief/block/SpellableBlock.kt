package com.possible_triangle.mischief.block;

import com.possible_triangle.mischief.block.tile.SpellableTile
import com.possible_triangle.mischief.item.SpellableItem
import com.possible_triangle.mischief.spell.ISpellable
import com.possible_triangle.mischief.spell.Spell
import com.possible_triangle.mischief.spell.SpellStack
import com.possible_triangle.mischief.spell.Spells
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.particle.DustParticleEffect
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.EnumProperty
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.ServerWorldAccess
import net.minecraft.world.World
import java.lang.IllegalArgumentException
import java.util.stream.Stream

abstract class SpellableBlock(private val spellMaterial: Spell.Material, settings: Settings) :
        TileHolder(settings), ISpellable {

    init {
        defaultState = super.getDefaultState().with(STATE, State.NONE)
    }

    enum class State : StringIdentifiable {
        CURSED, BLESSED, NONE;

        override fun asString(): String {
            return name.toLowerCase()
        }

        companion object {
            fun from(spell: SpellStack?): State {
                if (spell == null) return NONE
                return if(spell.spell.bad) CURSED else BLESSED
            }
        }
    }

    companion object {
        val STATE = EnumProperty.of("state", State::class.java)!!

        fun getTile(world: BlockView, pos: BlockPos): SpellableTile? {
            val tile = world.getBlockEntity(pos)
            return if (tile is SpellableTile) tile else null
        }
    }

    override fun getMaterial(): Spell.Material {
        return spellMaterial
    }

    override fun onPlaced(world: World, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
        val spell = SpellableItem.getSpell(stack)
        val tile = getTile(world, pos) ?: return
        tile.setSpell(spell)
    }

    @Environment(EnvType.CLIENT)
    override fun getPickStack(world: BlockView, pos: BlockPos, state: BlockState): ItemStack {
        val stack = super.getPickStack(world, pos, state)
        val spell = getTile(world, pos)?.spell
        try {
            SpellableItem.setSpell(spell, stack)
        } catch (ex: IllegalArgumentException) {}
        return stack
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(STATE)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return defaultState
    }

    override fun onUse(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockHitResult): ActionResult {
        val tile = getTile(world, pos) ?: return ActionResult.PASS
        val particle = tile.particleType()
        val range = tile.range

        if(world is ServerWorld) {
            val p = 4
            val minX = range.minX.toInt() * p
            val minY = range.minY.toInt() * p
            val minZ = range.minZ.toInt() * p
            val maxX = range.maxX.toInt() * p
            val maxY = range.maxY.toInt() * p
            val maxZ = range.maxZ.toInt() * p
            for (x in minX..maxX)
                for (y in minY..maxY)
                    for (z in minZ..maxZ) {
                        val xb = x in (minX + 1) until maxX
                        val yb = y in (minY + 1) until maxY
                        val zb = z in (minZ + 1) until maxZ
                        if (Stream.of(xb, yb, zb).filter { it }.count() <= 1) world.spawnParticles(particle, x / p.toDouble(), y / p.toDouble(), z / p.toDouble(), 1, 0.0, 0.0, 0.0, 0.0)
                    }
        }

        return ActionResult.SUCCESS
    }
}