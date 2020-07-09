package com.possible_triangle.mischief.mixin

import com.possible_triangle.mischief.item.Powder
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(ItemStack::class)
class ItemStackMixin {

    @Suppress("CAST_NEVER_SUCCEEDS")
    @Inject(at = [At("HEAD")], method = ["Lnet/minecraft/item/ItemStack;hasGlint()Z"], cancellable = true)
    fun hasGlint(callback: CallbackInfoReturnable<Boolean>) {
        val disguise = Powder.getDisguise(this as ItemStack)
        if (disguise != null) callback.returnValue = disguise.hasGlint()
    }

    @Environment(EnvType.CLIENT)
    @Suppress("CAST_NEVER_SUCCEEDS")
    @Inject(at = [At("HEAD")], method = ["Lnet/minecraft/item/ItemStack;getTooltip(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/client/item/TooltipContext;)Ljava/util/List;"], cancellable = true)
    fun getTooltip(player: PlayerEntity?, context: TooltipContext, callback: CallbackInfoReturnable<List<Text>>) {
        val disguise = Powder.getDisguise(this as ItemStack)
        if (disguise != null) callback.returnValue = disguise.getTooltip(player, context)
    }

    @Suppress("CAST_NEVER_SUCCEEDS")
    @Inject(at = [At("HEAD")], method = ["Lnet/minecraft/item/ItemStack;getName()Lnet/minecraft/text/Text;"], cancellable = true)
    fun getName(callback: CallbackInfoReturnable<Text>) {
        val disguise = Powder.getDisguise(this as ItemStack)
        if (disguise != null) callback.returnValue = disguise.name
    }

}