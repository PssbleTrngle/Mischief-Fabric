package com.possible_triangle.mischief.block.tile.render

import com.possible_triangle.mischief.block.tile.CarvingTableTile
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.BlockPos
import java.util.*

class CarvingTableRenderer(dispatcher: BlockEntityRenderDispatcher) : BlockEntityRenderer<CarvingTableTile>(dispatcher) {

    override fun render(tile: CarvingTableTile, delta: Float, matrices: MatrixStack, vertex: VertexConsumerProvider, light: Int, overlay: Int) {
        if(!tile.isMaster()) return

        val mc = MinecraftClient.getInstance()
        val state = tile.inputState()
        if (state != null) {
            matrices.push()
            matrices.translate(0.6, 1.0, 0.6)
            matrices.scale(0.8F, 0.8F, 0.8F)
            mc.blockRenderManager.renderBlock(state, BlockPos(0, 0, 0), tile.world, matrices, vertex.getBuffer(RenderLayer.getSolid()), false, Random())
            matrices.pop()
        }
    }

}