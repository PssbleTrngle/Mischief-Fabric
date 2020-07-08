package com.possible_triangle.mischief.block.tile;

import com.google.common.collect.Lists
import com.possible_triangle.mischief.Content;
import com.possible_triangle.mischief.spell.Spell
import com.possible_triangle.mischief.spell.ISpellable;
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.util.math.Vector3d
import net.minecraft.client.util.math.Vector3f
import net.minecraft.entity.LivingEntity
import net.minecraft.structure.rule.AxisAlignedLinearPosRuleTest
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import java.util.*
import java.util.stream.Stream

class TotemTile(type: BlockEntityType<*>) : SpellableTile(type) {

    fun getRange(): Box {
        return Box(pos).expand(getMaterial().range.toDouble())
    }

    // TODO: Save to NBT
    private var last: List<UUID> = Lists.newArrayList()

    override fun update() {
        val spell = this.spell
        if(spell != null) {
        
            val type = spell.spell.type
            
            val targets = world?.getEntities(spell.spell.affects(), getRange()) {
                t -> type == Spell.Type.TICK || last.contains(t.uuid)
            }?.stream() ?: Stream.of()

            last = spell.cast(targets, null, getSpellSource()).map(LivingEntity::getUuid)
        }
    }

}