package com.possible_triangle.mischief.block.tile;

import com.possible_triangle.mischief.Content;
import com.possible_triangle.mischief.spell.SpellStack
import net.minecraft.block.BedBlock
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BedBlockEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.data.client.model.BlockStateVariant
import net.minecraft.entity.LivingEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import java.util.stream.Stream

class DreamcatcherTile : SpellableTile(Content.DREAMCATCHER_TILE_TYPE) {

    fun getOwner(pos: BlockPos): List<ServerPlayerEntity> {
        val world = this.world
        if(world is ServerWorld) return world.players.filter { it.spawnPointPosition?.equals(pos) ?: false }
        return listOf()
    }

    fun getTargets(): List<ServerPlayerEntity> {
        return BlockPos.iterate(blockRange.minX, blockRange.minY, blockRange.minZ, blockRange.maxX, blockRange.maxY, blockRange.maxZ).map {
            return getOwner(it)
        }
    }

    override fun update(spell: SpellStack?): Boolean {
        if(spell != null) {
            
            val targets = getTargets()
            val affected = spell.cast(targets.stream(), null, spellSource).map(LivingEntity::getUuid)
            return affected.isNotEmpty()

        }

        return false
    }

}