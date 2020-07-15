package com.possible_triangle.mischief.block.tile

import com.possible_triangle.mischief.Content
import com.possible_triangle.mischief.block.CarvingTable
import com.possible_triangle.mischief.block.CarvingTable.Corner
import com.possible_triangle.mischief.block.tile.inventory.CarvingInventory
import com.possible_triangle.mischief.block.tile.inventory.CarvingInventory.Companion.INPUT
import com.possible_triangle.mischief.block.tile.inventory.CarvingInventory.Companion.OUTPUT
import com.possible_triangle.mischief.block.tile.inventory.SharedInventory
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.pattern.BlockPattern
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.particle.BlockStateParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import kotlin.math.sign

class CarvingTableTile : SharedInventory<CarvingInventory>(Content.CARVING_TABLE_TILE_TYPE, { CarvingInventory(it) }) {

    companion object {
        private val corners by lazy {
            Corner.values().map {
                val offset = BlockPos(1, 0, 1).rotate(it.rotation).add(-1, 0, -1)
                it to BlockPos(offset.x.sign, 0, offset.z.sign)
            }.toMap()
        }

        fun createTable(world: World, result: BlockPattern.Result) {
            val rotations = mapOf(
                    Direction.NORTH to BlockRotation.NONE,
                    Direction.EAST to BlockRotation.CLOCKWISE_90,
                    Direction.SOUTH to BlockRotation.CLOCKWISE_180,
                    Direction.WEST to BlockRotation.COUNTERCLOCKWISE_90
            )
            val offset = BlockPos(1, 0, 1).rotate(rotations[result.up] ?: BlockRotation.NONE).add(-1, 0, -1)
            val master = result.frontTopLeft.add(offset.x.sign, 0, offset.z.sign)
            corners.forEach { (corner, o) ->
                val cornerPos = master.subtract(o)
                val state = Content.CARVING_TABLE.defaultState.with(CarvingTable.CORNER, corner)
                world.setBlockState(cornerPos, state)
                CarvingTable.getTile(world, cornerPos)?.master = master
            }
        }

        fun destroyTable(world: WorldAccess, pos: BlockPos) {
            val master = CarvingTable.getTile(world, pos)?.master ?: return
            corners.forEach { (_, offset) ->
                world.setBlockState(master.subtract(offset), Blocks.AIR.defaultState, 3)
            }
        }
    }

    private var master: BlockPos? = null
        set(value) {
            field = value
            markDirty()
        }

    override fun getMaster(): CarvingTableTile? {
        if (this.master == null) return null
        val tile = world?.getBlockEntity(this.master)
        return if (tile is CarvingTableTile) tile else null
    }

    override fun isMaster(): Boolean {
        return pos == master
    }

    override fun toTag(nbt: CompoundTag): CompoundTag {
        val tag = inventory.toTag(super.toTag(nbt))
        val master = this.master
        if (master != null) tag.putIntArray("master", intArrayOf(master.x, master.y, master.z))
        return tag
    }

    override fun fromTag(state: BlockState, tag: CompoundTag) {
        super.fromTag(state, tag)
        if (tag.contains("master")) {
            val i = tag.getIntArray("master")
            if (i.size == 3) this.master = BlockPos(i[0], i[1], i[2])
        }
        inventory.fromTag(tag)
    }

    fun inputState(): BlockState? {
        val shared = getShared() ?: return null
        val slot = if(shared.getStack(OUTPUT).isEmpty) INPUT else OUTPUT
        val stack = shared.getStack(slot)?.item
        if (stack !is BlockItem) return null
        return stack.block.defaultState
    }

    fun carve(with: ItemStack): Boolean {
        val master = getMaster()
        if (master != null && !isMaster()) return master.carve(with)

        val world = this.world ?: return false
        val state = inputState()
        val recipe = world.recipeManager.getFirstMatch(Content.CARVING, this, world).filter {
            with.isEffectiveOn(state)
        }

        recipe.ifPresent {
            this.removeStack(INPUT)
            this.setStack(OUTPUT, it.getOutput())
            if (world is ServerWorld) {
                val center = Vec3d(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble()).add(1.0, 1.4, 1.0)
                val particle = BlockStateParticleEffect(ParticleTypes.BLOCK, state)
                world.spawnParticles(particle, center.x, center.y, center.z, 5, 0.4, 0.4, 0.4, 0.1)
            }
        }

        return recipe.isPresent
    }

}