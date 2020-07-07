package com.possible_triangle.mischief.spell

import com.google.common.collect.Lists
import com.google.common.collect.Maps
import com.possible_triangle.mischief.Content
import com.possible_triangle.mischief.spell.spells.DropSpell
import com.possible_triangle.mischief.spell.spells.DrownSpell
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.event.registry.RegistryAttribute
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey
import com.possible_triangle.mischief.spell.Spell.Context
import net.fabricmc.fabric.api.event.world.WorldTickCallback
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import java.util.function.Function

@Mixin(LivingEntity::class)
object Spells {

    private val KEY = RegistryKey.ofRegistry<Spell>(Identifier(Content.MODID, "spell"))
    val REGISTRY = FabricRegistryBuilder.createSimple(Spell::class.java, Identifier(Content.MODID, "spell"))
        .attribute(RegistryAttribute.MODDED)
        .buildAndRegister()

    val DROP = Registry.register(REGISTRY, "drop", DropSpell())
    val DROWN = Registry.register(REGISTRY, "drown", DrownSpell())

    fun attemptCast(ctx: Context): Boolean {
        val protection = Protection.find(ctx);

        return if (protection != null) {
            protection.onSuccess(ctx)
            false
        } else {
            ctx.spell.spell.apply(ctx)
            true
        }
    }

}