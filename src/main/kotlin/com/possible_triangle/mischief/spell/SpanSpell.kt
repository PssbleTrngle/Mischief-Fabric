package com.possible_triangle.mischief.spell

abstract class SpanSpell : Spell(Type.TRIGGER) {

    abstract fun start(context: Context)

    open fun during(context: Context) {}

    abstract fun end(context: Context)

}