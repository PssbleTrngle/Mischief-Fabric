package com.possible_triangle.mischief.block.tile.inventory

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

abstract interface SimpleInventory : SidedInventory {

    fun inventory(): DefaultedList<ItemStack>

    override fun removeStack(slot: Int, amount: Int): ItemStack {
        return Inventories.splitStack(inventory(), slot, amount)
    }

    override fun removeStack(slot: Int): ItemStack {
        return Inventories.removeStack(inventory(), slot)
    }

    override fun getStack(slot: Int): ItemStack {
        return inventory().getOrElse(slot) { ItemStack.EMPTY }
    }

    override fun size(): Int {
        return inventory().size
    }

    override fun isEmpty(): Boolean {
        return inventory().all { it.isEmpty }
    }

    fun getPos(): BlockPos

    override fun canPlayerUse(player: PlayerEntity): Boolean {
        val pos = getPos()
        return player.squaredDistanceTo(pos.x.toDouble() + 0.5, pos.y.toDouble() + 0.5, pos.z.toDouble() + 0.5) <= 64.0
    }

    override fun clear() {
        this.inventory().clear()
    }

    override fun setStack(slot: Int, stack: ItemStack) {
        if (slot >= 0 && slot < inventory().size) inventory()[slot] = stack
    }

    override fun canExtract(slot: Int, stack: ItemStack?, dir: Direction?): Boolean {
        return true
    }

    override fun canInsert(slot: Int, stack: ItemStack?, dir: Direction?): Boolean {
        return isValid(slot, stack)
    }

    override fun getAvailableSlots(side: Direction?): IntArray {
        return IntArray(inventory().size) { it }
    }

}