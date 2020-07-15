package com.possible_triangle.mischief

import com.possible_triangle.mischief.block.tile.render.CarvingTableRenderer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry
import net.fabricmc.fabric.impl.client.renderer.registry.BlockEntityRendererRegistryImpl

@Suppress("unused")
class MischiefClient : ClientModInitializer {

    override fun onInitializeClient() {
        BlockEntityRendererRegistryImpl.INSTANCE.register(Content.CARVING_TABLE_TILE_TYPE) { d -> CarvingTableRenderer(d) }
    }
}