package com.possible_triangle.mischief.block

import com.possible_triangle.mischief.block.tile.InfuserTile
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.world.BlockView

class Infuser : TileHolder(Settings.copy(Blocks.BREWING_STAND)) {

    override fun createBlockEntity(world: BlockView): BlockEntity {
        return InfuserTile()
    }

}