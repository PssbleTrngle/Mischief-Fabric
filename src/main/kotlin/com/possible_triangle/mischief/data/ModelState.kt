package com.possible_triangle.mischief.data

import com.google.common.collect.Maps
import net.minecraft.state.property.Property
import net.minecraft.util.StringIdentifiable


class ModelState {
    private val PROPS = Maps.newHashMap<String, Comparable<*>>()

    val key: String by lazy {
        PROPS.map {
            val v = it.value
            val value = if(v is StringIdentifiable) v.asString() else v.toString()
            "${it.key}=${value}"
        }.joinToString(",")
    }

    fun <T : Comparable<T>> get(property: Property<T>): T {
        val value = PROPS[property.name] ?: throw NullPointerException("Property not on block")
        return value as T
    }

    fun with(property: Property<*>, value: Comparable<*>): ModelState {
        val state = ModelState()
        state.PROPS.putAll(PROPS)
        state.PROPS[property.getName()] = value
        return state
    }

    companion object {
        private fun forProperties(models: List<ModelState>, vararg props: Property<*>): List<ModelState> {
            val first = props.firstOrNull() ?: return models
            val rest = props.drop(1).toTypedArray()
            return forProperties(models.map { model -> first.getValues().map { model.with(first, it) } }.flatten(), *rest)
        }

        fun forProperties(vararg props: Property<*>): List<ModelState> {
            return forProperties(listOf(ModelState()), *props)
        }
    }
}