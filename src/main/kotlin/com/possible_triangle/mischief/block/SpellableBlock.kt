package com.possible_triangle.mischief.block;

import com.possible_triangle.mischief.block.tile.SpellableTile
import com.possible_triangle.mischief.item.SpellableItem
import com.possible_triangle.mischief.spell.ISpellable
import com.possible_triangle.mischief.spell.Spell
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World

abstract class SpellableBlock(private val spellMaterial: Spell.Material, settings: Settings) :
    Block(settings), ISpellable {

    override fun getMaterial(): Spell.Material {
        return spellMaterial
    }

    fun getTile(world: BlockView, pos: BlockPos): SpellableTile? {
        val tile = world.getBlockEntity(pos)
        return if (tile is SpellableTile) tile else null
    }

    override fun onPlaced(world: World, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
        val spell = SpellableItem.getSpell(stack)
        val tile = getTile(world, pos) ?: return
        tile.spell = spell
    }

    @Environment(EnvType.CLIENT)
    override fun getPickStack(world: BlockView, pos: BlockPos, state: BlockState): ItemStack {
        val stack = super.getPickStack(world, pos, state)
        val spell = getTile(world, pos)?.spell
        SpellableItem.setSpell(spell, stack)
        return stack
    }

}