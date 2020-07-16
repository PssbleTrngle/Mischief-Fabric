package com.possible_triangle.mischief.block.tile

import com.possible_triangle.mischief.Content
import com.possible_triangle.mischief.block.tile.inventory.SimpleInventory
import com.possible_triangle.mischief.item.SpellableItem
import com.possible_triangle.mischief.recipe.SpellRecipe
import com.possible_triangle.mischief.spell.ISpellable
import net.minecraft.block.BlockState
import net.minecraft.block.entity.LockableContainerBlockEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Tickable
import net.minecraft.util.collection.DefaultedList

class InfuserTile : LockableContainerBlockEntity(Content.INFUSER_TILE_TYPE), SimpleInventory, Tickable {

    companion object {
        val SPELLABLE = 0
        val POWER = 1
        val INGREDIENTS = 2..5
    }

    private var time = 0
        set(value) {
            field = value
            markDirty()
        }

    private var inventory = DefaultedList.ofSize(INGREDIENTS.last, ItemStack.EMPTY)

    override fun inventory(): DefaultedList<ItemStack> {
        return inventory
    }

    override fun fromTag(state: BlockState, tag: CompoundTag) {
        super.fromTag(state, tag)
        inventory = DefaultedList.ofSize(size(), ItemStack.EMPTY)
        Inventories.fromTag(tag, inventory)
        this.time = tag.getShort("time").toInt()
    }

    override fun toTag(tag: CompoundTag): CompoundTag {
        super.toTag(tag)
        tag.putShort("time", this.time as Short)
        Inventories.toTag(tag, inventory)
        return tag
    }

    override fun createScreenHandler(syncId: Int, playerInventory: PlayerInventory): ScreenHandler {
        TODO("Not yet implemented")
    }

    override fun getContainerName(): Text {
        return TranslatableText("container.mischief.infuser")
    }

    override fun isValid(slot: Int, stack: ItemStack): Boolean {
        return stack.isEmpty || when(slot) {
            SPELLABLE -> stack.item is ISpellable
            POWER -> true
            else -> true
        }
    }

    private var recipe: SpellRecipe? = null

    fun setRecipe() {
        val world = this.world ?: return
        this.recipe = world.recipeManager.getFirstMatch(Content.SPELL_RECIPE_TYPE, this, world).orElseGet { null }
        if(this.recipe != null && time == 0) time = 180
    }

    override fun setStack(slot: Int, stack: ItemStack) {
        super.setStack(slot, stack)
        setRecipe()
    }

    override fun tick() {
        val recipe = this.recipe
        if(recipe != null) {
            if(time <= 0) {

                setStack(SPELLABLE, recipe.output)
                removeStack(POWER, 1)
                INGREDIENTS.forEach { removeStack(it, 1) }

            } else time--;
        }
    }

}