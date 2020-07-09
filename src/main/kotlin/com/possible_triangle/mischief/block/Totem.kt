package com.possible_triangle.mischief.block;

import com.possible_triangle.mischief.block.tile.TotemTile
import com.possible_triangle.mischief.spell.Spell
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView

class Totem(material: Spell.Material, val parent: Block?) : SpellableBlock(material, Settings.copy(parent)), Waterloggable {

    companion object {
        val FACING = HorizontalFacingBlock.FACING!!
        val A = createCuboidShape(2.0, 0.0, 4.0, 14.0, 16.0, 12.0)!!
        val B = createCuboidShape(4.0, 0.0, 2.0, 12.0, 16.0, 14.0)!!
    }

    init {
        defaultState = super.getDefaultState().with(FACING, Direction.NORTH)
    }

    @Suppress("DEPRECATION")
    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape {
        return when (state.get(FACING)) {
            Direction.NORTH, Direction.SOUTH -> A
            Direction.EAST, Direction.WEST -> B
            else -> super.getOutlineShape(state, world, pos, context);
        }
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