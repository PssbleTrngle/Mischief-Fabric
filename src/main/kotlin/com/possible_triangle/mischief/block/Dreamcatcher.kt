package com.possible_triangle.mischief.block;

import com.possible_triangle.mischief.block.tile.DreamcatcherTile
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

class Dreamcatcher(material: Spell.Material) : SpellableBlock(material, Settings.of(Material.WOOL).noCollision().nonOpaque()) {

    companion object {
        val SHAPE = createCuboidShape(1.0, 1.0, 1.0, 15.0, 15.0, 15.0)!!
    }

    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape {
        return SHAPE
    }

    override fun canCast(type: Spell.Type): Boolean {
        return type == Spell.Type.TICK
    }

    override fun createBlockEntity(world: BlockView): BlockEntity? {
        return DreamcatcherTile()
    }

    override fun isTranslucent(state: BlockState, world: BlockView, pos: BlockPos): Boolean {
        return true
    }

}