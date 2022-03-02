/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.klikli_dev.occultism.registry;

import com.github.klikli_dev.occultism.Occultism;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class OccultismTags {
    //region Fields

    // Block Tags
    public static final TagKey<Block> TREE_SOIL = makeBlockTag(new ResourceLocation(Occultism.MODID, "tree_soil"));
    public static final TagKey<Block> CAVE_WALL_BLOCKS = makeBlockTag(new ResourceLocation(Occultism.MODID, "cave_wall_blocks"));
    public static final TagKey<Block> WORLDGEN_BLACKLIST = makeBlockTag(new ResourceLocation(Occultism.MODID, "worldgen_blacklist"));
    public static final TagKey<Block> NETHERRACK = makeBlockTag(new ResourceLocation(Occultism.MODID, "netherrack"));
    public static final TagKey<Block> CANDLES = makeBlockTag(new ResourceLocation("minecraft", "candles"));

    //Item Tags
    public static final TagKey<Item> ELYTRA = makeItemTag(new ResourceLocation(Occultism.MODID, "elytra"));
    public static final TagKey<Item> FRUITS = makeItemTag(new ResourceLocation("forge", "fruits"));

    //Entity Tags
    public static final TagKey<EntityType<?>> AFRIT_ALLIES = makeEntityTypeTag(new ResourceLocation(Occultism.MODID, "afrit_allies"));
    public static final TagKey<EntityType<?>> WILD_HUNT = makeEntityTypeTag(new ResourceLocation(Occultism.MODID, "wild_hunt"));

    public static final TagKey<EntityType<?>> CHICKEN = makeEntityTypeTag(new ResourceLocation("forge", "chicken"));
    public static final TagKey<EntityType<?>> PARROTS = makeEntityTypeTag(new ResourceLocation("forge", "parrots"));
    public static final TagKey<EntityType<?>> PIGS = makeEntityTypeTag(new ResourceLocation("forge", "pigs"));
    public static final TagKey<EntityType<?>> COWS = makeEntityTypeTag(new ResourceLocation("forge", "cows"));
    public static final TagKey<EntityType<?>> VILLAGERS = makeEntityTypeTag(new ResourceLocation("forge", "villagers"));
    public static final TagKey<EntityType<?>> ZOMBIES = makeEntityTypeTag(new ResourceLocation("forge", "zombies"));
    public static final TagKey<EntityType<?>> BATS = makeEntityTypeTag(new ResourceLocation("forge", "bats"));

    //endregion Fields

    //region Static Methods
    public static TagKey<Item> makeItemTag(String id) {
        return makeItemTag(new ResourceLocation(id));
    }

    public static TagKey<Item> makeItemTag(ResourceLocation id) {
        return TagKey.create(Registry.ITEM_REGISTRY, id);
    }

    public static TagKey<Block> makeBlockTag(String id) {
        return makeBlockTag(new ResourceLocation(id));
    }

    public static TagKey<Block> makeBlockTag(ResourceLocation id) {
        return TagKey.create(Registry.BLOCK_REGISTRY, id);
    }

    public static TagKey<EntityType<?>> makeEntityTypeTag(String id) {
        return makeEntityTypeTag(new ResourceLocation(id));
    }

    public static TagKey<EntityType<?>> makeEntityTypeTag(ResourceLocation id) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, id);
    }
    //endregion Static Methods
}
