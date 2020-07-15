package com.possible_triangle.mischief.block.tile.inventory

import com.possible_triangle.mischief.block.tile.CarvingTableTile
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.math.Direction

class CarvingInventory(private val tile: BlockEntity) : SidedInventory {

    companion object {
        val INPUT = 0
        val OUTPUT = 1
    }

    override fun markDirty() {
        tile.markDirty()
    }

    override fun getAvailableSlots(side: Direction?): IntArray {
        return intArrayOf(INPUT, OUTPUT)
    }

    private var input = ItemStack.EMPTY
        set(value) {
            field = value
            markDirty()
        }

    private var output = ItemStack.EMPTY
        set(value) {
            field = value
            markDirty()
        }

    override fun setStack(slot: Int, stack: ItemStack?) {
        when (slot) {
            INPUT -> input = stack
            OUTPUT -> output = stack
        }
    }

    override fun canInsert(slot: Int, stack: ItemStack, dir: Direction?): Boolean {
        return when (slot) {
            INPUT -> stack.item is BlockItem && input.isEmpty && output.isEmpty
            else -> false
        }
    }

    override fun getMaxCountPerStack(): Int {
        return 1
    }

    override fun canPlayerUse(player: PlayerEntity): Boolean {
        return true
    }

    override fun size(): Int {
        return 2
    }

    override fun canExtract(slot: Int, stack: ItemStack, dir: Direction?): Boolean {
        return when (slot) {
            OUTPUT -> true
            else -> false
        }
    }

    override fun clear() {
        input = ItemStack.EMPTY
        output = ItemStack.EMPTY
    }

    override fun isEmpty(): Boolean {
        return input.isEmpty && output.isEmpty
    }

    override fun removeStack(slot: Int, amount: Int): ItemStack {
        return removeStack(slot)
    }

    override fun removeStack(slot: Int): ItemStack {
        val stored = getStack(slot).copy()
        setStack(slot, ItemStack.EMPTY)
        return stored
    }

    override fun getStack(slot: Int): ItemStack {
        return when (slot) {
            INPUT -> input
            OUTPUT -> output
            else -> ItemStack.EMPTY
        }
    }

    fun toTag(tag: CompoundTag): CompoundTag {
        tag.put("input", input.toTag(CompoundTag()))
        tag.put("output", output.toTag(CompoundTag()))
        return tag
    }

    fun fromTag(tag: CompoundTag) {
        this.input = ItemStack.fromTag(tag.getCompound("input"))
        this.output = ItemStack.fromTag(tag.getCompound("output"))
    }
    
}