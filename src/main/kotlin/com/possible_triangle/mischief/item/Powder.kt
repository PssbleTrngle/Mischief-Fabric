package com.possible_triangle.mischief.item

import com.possible_triangle.mischief.spell.Spell

class Powder(material: Spell.Material, settings: Settings? = null) : SpelledItem(material, settings) {

    override fun canCast(type: Spell.Execution): Boolean {
        return true
    }

}