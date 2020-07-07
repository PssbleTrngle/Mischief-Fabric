package com.possible_triangle.mischief.spell

import net.minecraft.entity.LivingEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.streams.toList

class SpellStack(val spell : Spell, val power: Int, val owner: UUID? = null, private var cooldown: Int = 0) {

    companion object {
        fun deserialize(tag: CompoundTag): SpellStack? {
            val spell = Spells.REGISTRY.get(Identifier(tag.getString("id"))) ?: return null

            val cooldown = tag.getInt("cooldown").coerceAtLeast(0)
            val owner = if (tag.contains("owner")) tag.getUuid("owner") else null
            val power = tag.getInt("power").coerceIn(1, spell.maxPower())
            return SpellStack(spell, power, owner, cooldown)
        }
    }

    fun serialize(): CompoundTag {
        val tag = CompoundTag()
        tag.putInt("power", power)
        tag.putInt("cooldown", cooldown)
        tag.putString("id", Spells.REGISTRY.getId(spell)!!.toString())
        if(owner != null) tag.putUuid("owner", owner)
        return tag
    }

    fun cast(type: Spell.Type, target: LivingEntity, by: ServerPlayerEntity? = null, at: Vec3d? = null): Boolean {
        if(!canCast(type)) return false

        val ctx = Spell.Context(target, this, at, by?.uuid)
        return Spells.attemptCast(ctx)
    }

    /**
     * @return The protected entities
     */
    fun cast(type: Spell.Type, targets: Stream<LivingEntity>, by: ServerPlayerEntity? = null, at: Vec3d? = null): Collection<LivingEntity> {
        return targets.filter { target -> !cast(type, target, by, at) }.toList()
    }

    fun canCast(type: Spell.Type): Boolean {
        return cooldown == 0 && executableVia(type)
    }

    fun getCooldownTotal(): Int? {
        return spell.getCooldown(this)
    }

    fun tick(): Boolean {
        return if(cooldown > 0) {
            cooldown--
            true
        } else false
    }

    fun getCooldown(): Int {
        return cooldown
    }

    fun executableVia(type: Spell.Type): Boolean {
        return spell.type == type;
    }

}