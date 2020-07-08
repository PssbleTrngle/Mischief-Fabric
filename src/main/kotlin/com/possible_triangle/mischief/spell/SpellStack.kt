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
import com.possible_triangle.mischief.spell.Spell.Type

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

    fun cast(target: LivingEntity, by: ServerPlayerEntity? = null, at: Vec3d? = null, type: Type? = null): Boolean {
        if(!canCast(type)) return false

        val ctx = Spell.Context(target, this, at, by?.uuid)
        return if (Spells.attemptCast(ctx)) {
            val cooldown = getCooldownTotal()
            if(cooldown > 0) this.cooldown = cooldown
            true
        } else false
    }

    /**
     * @return The protected entities
     */
    fun cast(targets: Stream<LivingEntity>, by: ServerPlayerEntity? = null, at: Vec3d? = null, type: Type? = null): Collection<LivingEntity> {
        return targets.filter { target -> !cast(target, by, at, type) }.toList()
    }

    fun canCast(type: Type?): Boolean {
        return cooldown == 0 && (type == null || executableVia(type))
    }

    fun getCooldownTotal(): Int {
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

    fun executableVia(type: Type): Boolean {
        return spell.type == type;
    }

}