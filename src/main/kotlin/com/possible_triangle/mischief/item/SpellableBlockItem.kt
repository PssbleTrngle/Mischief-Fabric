package com.possible_triangle.mischief.item

import com.possible_triangle.mischief.Content
import com.possible_triangle.mischief.block.SpellableBlock
import com.possible_triangle.mischief.spell.ISpellable
import com.possible_triangle.mischief.spell.ISpellableReference
import com.possible_triangle.mischief.spell.Spell
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.Block
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.world.World

class SpellableBlockItem(block: SpellableBlock, settings: Settings = Settings()) :
    BlockItem(block, settings.group(Content.GROUP).maxCount(1)) , ISpellableReference {

    override fun getBlock(): SpellableBlock {
        return super.getBlock() as SpellableBlock
    }

    override fun getReferent(): ISpellable {
        return block
    }

    override fun hasGlint(stack: ItemStack): Boolean {
        return SpellableItem.getSpell(stack) != null
    }

    @Environment(EnvType.CLIENT)
    override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
        SpellableItem.appendTooltip(stack, tooltip, context)
    }

}