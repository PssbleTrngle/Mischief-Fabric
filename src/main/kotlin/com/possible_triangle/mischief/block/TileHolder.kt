package com.possible_triangle.mischief.block

import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity

abstract class TileHolder(settings: Settings) : BlockWithEntity(settings) {

    override fun getRenderType(state: BlockState?): BlockRenderType {
        return BlockRenderType.MODEL
    }

}