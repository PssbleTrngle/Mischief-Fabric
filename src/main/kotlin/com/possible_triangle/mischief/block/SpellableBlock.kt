package com.possible_triangle.mischief.block;

import com.possible_triangle.mischief.block.tile.SpellableTile
import com.possible_triangle.mischief.item.SpellableItem
import com.possible_triangle.mischief.spell.ISpellable
import com.possible_triangle.mischief.spell.Spell
import com.possible_triangle.mischief.spell.SpellStack
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.state.StateManager
import net.minecraft.state.property.EnumProperty
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World

abstract class SpellableBlock(private val spellMaterial: Spell.Material, settings: Settings) :
    Block(settings), ISpellable, BlockEntityProvider {

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
                return CURSED
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
        SpellableItem.setSpell(spell, stack)
        return stack
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(STATE)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return defaultState
    }
}