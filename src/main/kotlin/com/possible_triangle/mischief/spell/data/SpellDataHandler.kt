package com.possible_triangle.mischief.spell.data

import com.possible_triangle.mischief.spell.SpellStack
import net.minecraft.entity.data.TrackedDataHandler
import net.minecraft.network.PacketByteBuf

class SpellDataHandler : TrackedDataHandler<SpellStack?> {

    override fun write(data: PacketByteBuf, spell: SpellStack?) {
        data.writeBoolean(spell != null)
        data.writeCompoundTag(spell?.serialize())
    }

    override fun copy(spell: SpellStack?): SpellStack? {
        return if(spell == null) null else SpellStack.deserialize(
            spell.serialize()
        )
    }

    override fun read(data: PacketByteBuf): SpellStack? {
         return if(data.readBoolean()) SpellStack.deserialize(
             data.readCompoundTag()!!
         ) else null
    }

}