package com.possible_triangle.mischief.block;

import com.possible_triangle.mischief.block.tile.TotemTile
import com.possible_triangle.mischief.spell.Spell
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.FacingBlock
import net.minecraft.block.HorizontalFacingBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView

class Totem(material: Spell.Material, settings: Settings) : SpellableBlock(material, settings) {

    companion object {
        val FACING = HorizontalFacingBlock.FACING!!
    }

    init {
        defaultState = super.getDefaultState().with(FACING, Direction.NORTH)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(FACING)
    }

    override fun canCast(type: Spell.Type): Boolean {
        return true
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return super.getPlacementState(ctx).with(FACING, ctx.playerFacing)
    }

    override fun createBlockEntity(world: BlockView): BlockEntity? {
        return TotemTile()
    }

}