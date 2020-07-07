package com.possible_triangle.block.tile;

import net.minecraft.block.Block;
import com.possible_triangle.Content;
import com.possible_triangle.spell.Spell;
import com.possible_triangle.spell.ISpellable;

class TotemTile(type: TileEntityType<*>) : SpellableTile(type) {

    fun getRange(): AABB {
        val radius = 3
        return AABB(pos).grow(radius)
    }

    // TODO: Save to NBT
    var last: List<UUID> = Lists.newArrayList()

    override fun tick() {
        if(spell != null) {
        
            val type = spell.spell.type
            
            val targets = world.getEntitiesInAABB(range).stream()
            val at = Vec3d(pos)

            switch(type) {
                case TICK: spell.cast(type, targets, null, at); break
                case TRIGGER: spell.cast(type, target.filter { t -> !last.contains(t.uniqueId) }, null, at) break;
            }

            last = targets

        }
    }
    
    override fun canCast(type: Spell.Execution): Boolean {
        return true
    }

}