package com.possible_triangle.mischief.spell

import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.Vec3d
import java.util.*

abstract class Protection : Spell(Execution.TICK) {

    abstract fun protects(ctx: Context): Boolean

    fun onSuccess(ctx: Context) {}

}