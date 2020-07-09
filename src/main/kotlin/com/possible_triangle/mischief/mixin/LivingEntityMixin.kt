package com.possible_triangle.mischief.mixin

import com.google.common.collect.Lists
import com.possible_triangle.mischief.spell.Protection
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(LivingEntity::class)
class LivingEntityMixin {

    @Suppress("CAST_NEVER_SUCCEEDS")
    @Inject(at = [At("RETURN")], method = ["tick()V"])
    fun tickEntity(callback: CallbackInfo) {
        Protection.tick(this as LivingEntity)
    }

}