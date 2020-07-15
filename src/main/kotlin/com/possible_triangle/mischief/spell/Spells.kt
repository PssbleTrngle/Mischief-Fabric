package com.possible_triangle.mischief.spell

import com.google.common.collect.Lists
import com.google.common.collect.Maps
import com.possible_triangle.mischief.Content
import com.possible_triangle.mischief.Mischief
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.event.registry.RegistryAttribute
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey
import com.possible_triangle.mischief.spell.Spell.Context
import com.possible_triangle.mischief.spell.spells.*
import net.fabricmc.fabric.api.event.world.WorldTickCallback
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.potion.Potions
import net.minecraft.server.command.EffectCommand
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import java.awt.Color
import java.util.function.Function
import java.util.stream.Stream

object Spells {

    val NONE_COLOR = Color(0xC1C1C1)

    val REGISTRY = FabricRegistryBuilder.createSimple(Spell::class.java, Identifier(Mischief.MODID, "spell"))
        .attribute(RegistryAttribute.MODDED)
        .buildAndRegister()

    val DROP = Registry.register(REGISTRY, "drop", DropSpell())
    val DROWN = Registry.register(REGISTRY, "drown", DrownSpell())
    val BOUNCE = Registry.register(REGISTRY, "bounce", BounceSpell())

    val REFLECT = Registry.register(REGISTRY, "reflect", ReflectProtection())
    val SHIELD = Registry.register(REGISTRY, "shield", ShieldProtection())

    val EFFECTS = listOf(
            StatusEffects.BLINDNESS,
            StatusEffects.FIRE_RESISTANCE,
            StatusEffects.GLOWING,
            StatusEffects.HUNGER,
            StatusEffects.INVISIBILITY,
            StatusEffects.LEVITATION,
            StatusEffects.MINING_FATIGUE,
            StatusEffects.NAUSEA,
            StatusEffects.NIGHT_VISION,
            StatusEffects.RESISTANCE,
            StatusEffects.SLOWNESS,
            StatusEffects.WEAKNESS,
            StatusEffects.SLOW_FALLING
    ).map { Registry.register(REGISTRY, Registry.STATUS_EFFECT.getId(it)!!.path, EffectSpell(it)) }

    fun attemptCast(ctx: Context): Boolean {
        if(!ctx.spell.spell.affects().isInstance(ctx.target)) return false

        if(ctx.spell.piercing) {
            ctx.spell.spell.apply(ctx)
            return true
        }

        val stack = Protection.find(ctx)
        val protection = stack?.spell

        return if (protection is Protection) {
            protection.onSuccess(stack, ctx)
            false
        } else {
            ctx.spell.spell.apply(ctx)
            true
        }
    }

}