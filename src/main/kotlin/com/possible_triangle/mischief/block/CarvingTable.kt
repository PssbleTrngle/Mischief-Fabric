package com.possible_triangle.mischief.block

import com.possible_triangle.mischief.block.tile.CarvingTableTile
import com.possible_triangle.mischief.block.tile.inventory.CarvingInventory
import com.possible_triangle.mischief.block.tile.inventory.CarvingInventory.Companion.INPUT
import com.possible_triangle.mischief.block.tile.inventory.CarvingInventory.Companion.OUTPUT
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.pattern.BlockPatternBuilder
import net.minecraft.block.pattern.CachedBlockPosition
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.state.StateManager
import net.minecraft.state.property.EnumProperty
import net.minecraft.util.ActionResult
import net.minecraft.util.BlockRotation
import net.minecraft.util.Hand
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess

class CarvingTable : TileHolder(Settings.of(Material.STONE, MaterialColor.BLACK)) {

    companion object {
        val CORNER = EnumProperty.of("corner", Corner::class.java)

        fun getTile(world: WorldAccess, pos: BlockPos): CarvingTableTile? {
            val tile = world.getBlockEntity(pos)
            return if (tile is CarvingTableTile) tile else null
        }
    }

    private val PATTERN = BlockPatternBuilder.start().aisle("xx", "xx")
            .where('x', CachedBlockPosition.matchesBlockState { it.block == this && it.get(CORNER) == Corner.NONE })
            .build()

    enum class Corner(val rotation: BlockRotation = BlockRotation.NONE) : StringIdentifiable {
        NONE,
        NW(BlockRotation.NONE),
        SW(BlockRotation.CLOCKWISE_180),
        NE(BlockRotation.CLOCKWISE_90),
        SE(BlockRotation.COUNTERCLOCKWISE_90);

        override fun asString(): String {
            return name.toLowerCase()
        }
    }

    init {
        defaultState = super.getDefaultState().with(CORNER, Corner.NONE)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(CORNER)
    }

    override fun onPlaced(world: World, pos: BlockPos, state: BlockState, placer: LivingEntity?, itemStack: ItemStack) {
        val result = PATTERN.searchAround(world, pos) ?: return
        CarvingTableTile.createTable(world, result)
    }

    override fun onBroken(world: WorldAccess, pos: BlockPos, state: BlockState) {
        CarvingTableTile.destroyTable(world, pos)
    }

    override fun createBlockEntity(world: BlockView): BlockEntity {
        return CarvingTableTile()
    }

    override fun onUse(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockHitResult): ActionResult {
        val tile = getTile(world, pos)
        if (tile != null) {

            val held = player.getStackInHand(hand)

            if(player.isSneaking) {
                val slot = if(tile.getStack(OUTPUT).isEmpty) INPUT else OUTPUT
                val stack = tile.getStack(slot)
                if(!stack.isEmpty && player.giveItemStack(stack)) {
                    tile.removeStack(slot)
                    return ActionResult.SUCCESS
                }
            } else {
                if (tile.canInsert(INPUT, held, null)) {
                    tile.setStack(INPUT, held.split(1))
                    return ActionResult.SUCCESS
                } else if (tile.carve(held)) {
                    return ActionResult.SUCCESS
                }
            }
        }

        return ActionResult.PASS
    }

}