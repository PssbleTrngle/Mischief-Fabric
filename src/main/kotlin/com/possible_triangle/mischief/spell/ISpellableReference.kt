package com.possible_triangle.mischief.spell

interface ISpellableReference : ISpellable {

    fun getReferent(): ISpellable

    override fun getMaterial(): Spell.Material {
        return getReferent().getMaterial()
    }

    override fun canCast(type: Spell.Type): Boolean {
        return getReferent().canCast(type)
    }
}