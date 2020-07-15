package com.possible_triangle.mischief.block.tile

import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.util.registry.Registry

abstract class BaseTile(type: BlockEntityType<*>) : BlockEntity(type) {

    override fun toUpdatePacket(): BlockEntityUpdateS2CPacket? {
        return BlockEntityUpdateS2CPacket(pos, Registry.BLOCK_ENTITY_TYPE.getRawId(type), toTag(CompoundTag()))
    }

}