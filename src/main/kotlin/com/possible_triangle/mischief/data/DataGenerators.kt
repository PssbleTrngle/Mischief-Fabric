package com.possible_triangle.mischief.data

import com.google.common.collect.Maps
import com.possible_triangle.mischief.Content
import com.possible_triangle.mischief.Mischief
import com.possible_triangle.mischief.block.CarvingTable
import com.possible_triangle.mischief.block.SpellableBlock
import com.possible_triangle.mischief.block.Totem
import net.devtech.arrp.api.RRPCallback
import net.devtech.arrp.api.RRPPreGenEntrypoint
import net.devtech.arrp.api.RuntimeResourcePack
import net.devtech.arrp.json.blockstate.JBlockModel
import net.devtech.arrp.json.blockstate.JState.state
import net.devtech.arrp.json.blockstate.JState.variant
import net.devtech.arrp.json.blockstate.JVariant
import net.devtech.arrp.json.models.JModel
import net.devtech.arrp.json.models.JTextures
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.Item
import net.minecraft.state.property.Property
import net.minecraft.util.Identifier
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.registry.Registry
import java.util.stream.Stream

class DataGenerators : RRPPreGenEntrypoint {

    companion object {
        private val PACK = RuntimeResourcePack.create("${Mischief.MODID}:resources")

        fun id(item: Item): Identifier {
            return Registry.ITEM.getId(item)
        }

        fun model(item: Item): Identifier {
            val id = id(item)
            return Identifier(id.namespace, "item/${id.path}")
        }

        fun extend(id: Identifier, with: String): Identifier {
            return Identifier(id.namespace, id.path + with)
        }

        fun id(block: Block): Identifier {
            return Registry.BLOCK.getId(block)
        }

        fun id(string: String): Identifier {
            return Identifier(Mischief.MODID, string)
        }

        fun model(block: Block): Identifier {
            val id = id(block)
            return Identifier(id.namespace, "block/${id.path}")
        }

        fun register() {
            RRPCallback.EVENT.register(RRPCallback { l -> l.add(1, PACK) })
        }

        fun flatProperties(block: Block, model: (ModelState) -> JBlockModel): JVariant {
            val props = block.defaultState.properties.toTypedArray()
            return ModelState.forProperties(*props).fold(variant(), { v, m -> v.put(m.key, model(m)) })
        }
    }

    override fun pregen() {

        Stream.of(Content.EXPERIMENTAL_POWDER, Content.NATURAL_POWDER).forEach {
            PACK.addModel(JModel.model("item/generated").textures(JTextures().layer0(model(it).toString())), model(it))
        }

        Content.totems.forEach { (totem, parent) ->
            PACK.addModel(JModel.model(model(totem).toString()), model(totem.asItem()))

            SpellableBlock.State.values().forEach {
                val texture = model(parent ?: totem).toString()
                val noSpell = it == SpellableBlock.State.NONE

                PACK.addModel(
                        JModel.model(id("block/totem").toString()).textures(JTextures()
                                .`var`("texture", texture)
                                .`var`("glow", if (noSpell) texture else id("block/totem_glow").toString())),
                        if (noSpell) model(totem) else extend(model(totem), "_${it.asString()}")
                )
            }

            PACK.addBlockState(
                    state(flatProperties(totem) {
                        val y = it.get(Totem.FACING).asRotation().toInt()
                        val state = it.get(SpellableBlock.STATE)
                        val model = if (state == SpellableBlock.State.NONE) model(totem) else extend(model(totem), "_${state.asString()}")
                        JBlockModel(model.toString()).y(y)
                    }), id(totem)
            )
        }

        Content.dreamcatchers.forEach { dreamcatcher ->
            PACK.addModel(JModel.model(model(dreamcatcher).toString()), model(dreamcatcher.asItem()))

            val texture = model(dreamcatcher).toString()
            PACK.addModel(JModel.model(id("block/dreamcatcher").toString()).textures(JTextures().`var`("texture", texture)), model(dreamcatcher))

            PACK.addBlockState(
                    state(flatProperties(dreamcatcher) {
                        val model = model(dreamcatcher)
                        JBlockModel(model.toString())
                    }), id(dreamcatcher)
            )
        }

        PACK.addModel(JModel.model(id("block/carving_table_item").toString()), model(Content.CARVING_TABLE.asItem()))
        PACK.addBlockState(
                state(flatProperties(Content.CARVING_TABLE) {
                    val assembled = it.get(CarvingTable.CORNER) != CarvingTable.Corner.NONE
                    val model = model(if(assembled) Content.CARVING_TABLE else Blocks.BLACKSTONE)
                    val corner = it.get(CarvingTable.CORNER)
                    JBlockModel(model.toString()).uvlock().y(corner.rotation.ordinal * 90)
                }), id(Content.CARVING_TABLE)
        )

    }

}