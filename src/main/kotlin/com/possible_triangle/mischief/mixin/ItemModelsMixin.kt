package com.possible_triangle.mischief.mixin

import com.possible_triangle.mischief.item.Powder
import com.possible_triangle.mischief.spell.Protection
import net.minecraft.client.render.item.ItemModels
import net.minecraft.client.render.model.BakedModel
import net.minecraft.entity.ItemSteerable
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(ItemModels::class)
class ItemModelsMixin {

    @Suppress("CAST_NEVER_SUCCEEDS")
    @Inject(at = [At("HEAD")], method = ["getModel(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/client/render/model/BakedModel;"], cancellable = true)
    fun useDisguiseModel(stack: ItemStack, callback: CallbackInfoReturnable<BakedModel>) {
        val models = this as ItemModels
        val disguise = Powder.getDisguise(stack)
        if (disguise != null) callback.returnValue = models.getModel(disguise)
    }

}