package com.possible_triangle.mischief.block.tile.inventory

import com.possible_triangle.mischief.block.tile.BaseTile
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Direction

abstract class SharedInventory<I : SidedInventory>(type: BlockEntityType<*>, private val inventorySupplier: (BlockEntity) -> I): SidedInventory, BaseTile(type) {

    val inventory: I by lazy {
        inventorySupplier(this)
    }

    abstract fun getMaster(): SharedInventory<I>?

    abstract fun isMaster(): Boolean;

    fun getShared(): SidedInventory? {
        if(isMaster()) return this.inventory
        val master = getMaster()
        return master?.inventory
    }

    override fun clear() {
        getShared()?.clear()
    }

    override fun canExtract(slot: Int, stack: ItemStack, dir: Direction?): Boolean {
        return getShared()?.canExtract(slot, stack, dir) ?: false
    }

    override fun canInsert(slot: Int, stack: ItemStack?, dir: Direction?): Boolean {
        return getShared()?.canInsert(slot, stack, dir) ?: false
    }

    override fun getAvailableSlots(side: Direction?): IntArray {
        return getShared()?.getAvailableSlots(side) ?: intArrayOf()
    }

    override fun onOpen(player: PlayerEntity) {
        getShared()?.onOpen(player)
    }

    override fun setStack(slot: Int, stack: ItemStack) {
        getShared()?.setStack(slot, stack)
    }

    override fun getMaxCountPerStack(): Int {
        return getShared()?.maxCountPerStack ?: 0
    }

    override fun containsAny(items: MutableSet<Item>): Boolean {
        return getShared()?.containsAny(items) ?: false
    }

    override fun isEmpty(): Boolean {
        return getShared()?.isEmpty ?: true
    }

    override fun removeStack(slot: Int, amount: Int): ItemStack {
        return getShared()?.removeStack(slot, amount) ?: ItemStack.EMPTY
    }

    override fun removeStack(slot: Int): ItemStack {
        return getShared()?.removeStack(slot) ?: ItemStack.EMPTY
    }

    override fun getStack(slot: Int): ItemStack {
        return getShared()?.getStack(slot) ?: ItemStack.EMPTY
    }

    override fun onClose(player: PlayerEntity) {
        getShared()?.onClose(player)
    }

    override fun count(item: Item): Int {
        return getShared()?.count(item) ?: 0
    }

    override fun canPlayerUse(player: PlayerEntity): Boolean {
        return getShared()?.canPlayerUse(player) ?: false
    }

    override fun size(): Int {
        return getShared()?.size() ?: 0
    }

    override fun isValid(slot: Int, stack: ItemStack): Boolean {
        return getShared()?.isValid(slot, stack) ?: false
    }
}