package com.possible_triangle.mischief.spell

import net.minecraft.entity.LivingEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.Vec3d
import java.util.*

final class SpellStack(val spell : Spell, val power: Int, val owner: UUID?, private var cooldown: Int = 0) {

    companion object {
        fun deserialize(tag: CompoundTag): SpellStack? {
            val power = tag.getInt("power")
            val owner = if (tag.contains("owner")) tag.getUuid("owner") else null
            return null
        }
    }

    fun serialize(): CompoundTag {
        val tag = CompoundTag()
        tag.putInt("power", power)
        if(owner != null) tag.putUuid("owner", owner)
        return tag
    }

    fun cast(type: Spell.Execution, target: LivingEntity, by: ServerPlayerEntity? = null, at: Vec3d? = null): Boolean {
        if(!canCast(type)) return false

        val ctx = Spell.Context(target, this, at, by?.uuid)
        return Spell.attempCast(ctx)
    }

    /**
     * @return The protected entities
     */
    fun cast(type: Spell.Execution, targets: Stream<LivingEntity>, by: ServerPlayerEntity? = null, at: Vec3d? = null): Stream<LivingEntity> {
        return targets.filter { target -> !cast(target, by, at) }
    }

    fun canCast(type: Spell.Execution): Boolean {
        return cooldown == 0 && executableVia(type)
    }

    fun getCooldownTotal(): Int {
        return spell.getCooldown(this)
    }

    fun tick(): Boolean {
        if(cooldown > 0) {
            cooldown--;
            return true;
        }
        return false;
    }

    fun getCooldown() {
        return cooldown
    }

    fun executableVia(type: Spell.Execution): Boolean {
        return spell.type == type;
    }

}