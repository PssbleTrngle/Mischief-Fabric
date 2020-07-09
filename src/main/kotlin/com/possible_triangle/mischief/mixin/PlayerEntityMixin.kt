package com.possible_triangle.mischief.mixin

import com.google.common.collect.Lists
import com.possible_triangle.mischief.spell.Protection
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject

@Mixin(PlayerEntity::class)
class PlayerEntityMixin {

    @Suppress("CAST_NEVER_SUCCEEDS")
    @Inject(at = [At("RETURN")], method = ["tick"])
    fun tickEntity() {
        (this as PlayerEntity).dataTracker.set(Protection.KEY, Lists.newArrayList())
    }

    @Suppress("CAST_NEVER_SUCCEEDS")
    @Inject(at = [At("RETURN")], method = ["initDataTracker"])
    fun registerData() {
        (this as PlayerEntity).dataTracker.startTracking(Protection.KEY, Lists.newArrayList())
    }

}