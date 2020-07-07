package com.possible_triangle.mischief.spell.data

import com.possible_triangle.mischief.spell.Spell
import com.possible_triangle.mischief.spell.SpellStack
import net.minecraft.entity.data.TrackedDataHandler
import net.minecraft.network.PacketByteBuf
import java.util.stream.IntStream
import kotlin.streams.toList

class SpellsDataHandler : TrackedDataHandler<List<SpellStack>> {

    override fun write(data: PacketByteBuf, spells: List<SpellStack>) {
        data.writeInt(spells.size)
        spells.forEach { s -> data.writeCompoundTag(s.serialize()) }
    }

    override fun copy(spells: List<SpellStack>): List<SpellStack> {
        return spells.map(SpellStack::serialize).mapNotNull { t -> SpellStack.deserialize(t) }
    }

    override fun read(data: PacketByteBuf): List<SpellStack> {
        val size = data.readInt()
        return IntStream.range(0, size).toList().mapNotNull { data.readCompoundTag() }.mapNotNull { t -> SpellStack.deserialize(t) }
    }

}