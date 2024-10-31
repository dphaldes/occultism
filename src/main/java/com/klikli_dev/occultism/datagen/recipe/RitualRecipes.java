package com.klikli_dev.occultism.datagen.recipe;

import com.klikli_dev.occultism.Occultism;
import com.klikli_dev.occultism.crafting.recipe.conditionextension.condition.IsInBiomeWithTagCondition;
import com.klikli_dev.occultism.crafting.recipe.conditionextension.condition.IsInDimensionTypeCondition;
import com.klikli_dev.occultism.datagen.recipe.builders.RitualRecipeBuilder;
import com.klikli_dev.occultism.registry.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.OrCondition;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class RitualRecipes extends RecipeProvider {

    // Ritual Types
    private static final ResourceLocation RITUAL_SUMMON = ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "summon");
    private static final ResourceLocation RITUAL_SUMMON_WILD = ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "summon_wild");
    private static final ResourceLocation RITUAL_SUMMON_JOB = ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "summon_spirit_with_job");
    private static final ResourceLocation RITUAL_RESURRECT_FAMILIAR = ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "resurrect_familiar");
    private static final ResourceLocation RITUAL_FAMILIAR = ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "familiar");
    private static final ResourceLocation RITUAL_CRAFT_WITH_SPIRIT_NAME = ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "craft_with_spirit_name");
    private static final ResourceLocation RITUAL_CRAFT = ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "craft");
    private static final ResourceLocation RITUAL_CRAFT_MINER_SPIRIT = ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "craft_miner_spirit");
    private static final ResourceLocation RITUAL_REPAIR = ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "repair");
    // Pentacle IDs
    private static final ResourceLocation PENTACLE_SUMMON_FOLIOT = ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "summon_foliot");
    private static final ResourceLocation PENTACLE_SUMMON_DJINNI = ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "summon_djinni");
    private static final ResourceLocation PENTACLE_SUMMON_UNBOUND_AFRIT = ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "summon_unbound_afrit");
    private static final ResourceLocation PENTACLE_SUMMON_AFRIT = ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "summon_afrit");
    private static final ResourceLocation PENTACLE_SUMMON_UNBOUND_MARID = ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "summon_unbound_marid");
    private static final ResourceLocation PENTACLE_SUMMON_MARID = ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "summon_marid");
    private static final ResourceLocation PENTACLE_POSSESS_FOLIOT = ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "possess_foliot");
    private static final ResourceLocation PENTACLE_POSSESS_DJINNI = ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "possess_djinni");
    private static final ResourceLocation PENTACLE_POSSESS_UNBOUND_AFRIT = ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "possess_unbound_afrit");
    private static final ResourceLocation PENTACLE_POSSESS_AFRIT = ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "possess_afrit");
    private static final ResourceLocation PENTACLE_POSSESS_MARID = ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "possess_marid");
    private static final ResourceLocation PENTACLE_CRAFT_FOLIOT = ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "craft_foliot");
    private static final ResourceLocation PENTACLE_CRAFT_DJINNI = ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "craft_djinni");
    private static final ResourceLocation PENTACLE_CRAFT_AFRIT = ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "craft_afrit");
    private static final ResourceLocation PENTACLE_CRAFT_MARID = ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "craft_marid");
    private static final ResourceLocation PENTACLE_RESURRECT_SPIRIT = ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "resurrect_spirit");
    private static final ResourceLocation PENTACLE_CONTACT_WILD_SPIRIT = ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "contact_wild_spirit");
    private static final ResourceLocation PENTACLE_CONTACT_ELDRITCH_SPIRIT = ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "contact_eldritch_spirit");


    public RitualRecipes(PackOutput p_248933_, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(p_248933_, lookupProvider);
    }

    private static ItemStack makeLoreSpawnEgg(Item item, String key) {
        ItemStack output = new ItemStack(item);
        output.set(DataComponents.LORE, new ItemLore(List.of(Component.translatable(key + ".tooltip"))));
        output.set(DataComponents.ITEM_NAME, Component.translatable(key));
        return output;
    }

    private static ItemStack makeRitualDummy(ItemLike item) {
        return new ItemStack(item);
    }

    private static ItemStack makeRitualDummy(ResourceLocation location) {
        return new ItemStack(BuiltInRegistries.ITEM.get(location));
    }

    private static ItemStack makeJeiDummy(ResourceLocation location) {
        return new ItemStack(BuiltInRegistries.ITEM.get(location));
    }

    private static ItemStack makeJeiNoneDummy() {
        return makeJeiDummy(ResourceLocation.fromNamespaceAndPath("occultism", "jei_dummy/none"));
    }

    public static void ritualRecipes(RecipeOutput recipeOutput, HolderLookup.Provider registries) {
        craftingRituals(recipeOutput);
        familiarRituals(recipeOutput);
        possessRituals(recipeOutput);
        summonRituals(recipeOutput, registries);
        resurrectRituals(recipeOutput);
        repairRituals(recipeOutput);
    }

    private static void summonRituals(RecipeOutput recipeOutput, HolderLookup.Provider registries) {
        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_AFRIT.get(), "item.occultism.ritual_dummy.summon_afrit_crusher"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/summon_afrit_crusher")),
                        120,
                        RITUAL_SUMMON_JOB,
                        PENTACLE_SUMMON_AFRIT,
                        Ingredient.of(OccultismTags.Items.IESNIUM_DUST),
                        Ingredient.of(OccultismTags.Items.EMERALD_DUST),
                        Ingredient.of(OccultismTags.Items.LAPIS_DUST),
                        Ingredient.of(OccultismTags.Items.AMETHYST_DUST),
                        Ingredient.of(OccultismTags.Items.OBSIDIAN_DUST))
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()))
                .spiritMaxAge(-1)
                .spiritJobType(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "crush_tier3"))
                .entityToSummon(OccultismEntities.AFRIT_TYPE.get())
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/summon_afrit_crusher"));
        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_AFRIT.get(), "item.occultism.ritual_dummy.summon_afrit_rain_weather"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/summon_afrit_rain_weather")),
                        60,
                        RITUAL_SUMMON_JOB,
                        PENTACLE_SUMMON_AFRIT,
                        Ingredient.of(Tags.Items.SANDS),
                        Ingredient.of(Tags.Items.GEMS_DIAMOND),
                        Ingredient.of(Items.CACTUS),
                        Ingredient.of(Items.DEAD_BUSH))
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()))
                .spiritMaxAge(120)
                .entityToSummon(OccultismEntities.AFRIT_TYPE.get())
                .spiritJobType(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "rain_weather"))
                .entityToSacrifice(OccultismTags.Entities.COWS)
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.cows")
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/summon_afrit_rain_weather"));
        //summon_afrit_thunder_weather
        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_AFRIT.get(), "item.occultism.ritual_dummy.summon_afrit_thunder_weather"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/summon_afrit_thunder_weather")),
                        60,
                        RITUAL_SUMMON_JOB,
                        PENTACLE_SUMMON_AFRIT,
                        Ingredient.of(Tags.Items.BONES),
                        Ingredient.of(Tags.Items.GUNPOWDERS),
                        Ingredient.of(Tags.Items.GUNPOWDERS),
                        Ingredient.of(Items.GHAST_TEAR))
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()))
                .spiritMaxAge(240)
                .entityToSummon(OccultismEntities.AFRIT_TYPE.get())
                .spiritJobType(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "thunder_weather"))
                .entityToSacrifice(OccultismTags.Entities.COWS)
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.cows")
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/summon_afrit_thunder_weather"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_DEMONIC_HUSBAND.get(), "item.occultism.ritual_dummy.summon_demonic_husband"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/summon_demonic_husband")),
                        60,
                        OccultismRituals.SUMMON_TAMED.getId(),
                        PENTACLE_SUMMON_DJINNI,
                        Ingredient.of(Tags.Items.INGOTS_GOLD),
                        Ingredient.of(Tags.Items.GEMS_EMERALD),
                        Ingredient.of(Tags.Items.GUNPOWDERS),
                        Ingredient.of(Items.PORKCHOP),
                        Ingredient.of(ItemTags.SWORDS),
                        Ingredient.of(Items.GLASS_BOTTLE)
                )
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .entityToSummon(OccultismEntities.DEMONIC_HUSBAND.get())
                .entityToSacrifice(OccultismTags.Entities.CHICKEN)
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.chicken")
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/summon_demonic_husband"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_DEMONIC_WIFE.get(), "item.occultism.ritual_dummy.summon_demonic_wife"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/summon_demonic_wife")),
                        60,
                        OccultismRituals.SUMMON_TAMED.getId(),
                        PENTACLE_SUMMON_DJINNI,
                        Ingredient.of(Tags.Items.INGOTS_GOLD),
                        Ingredient.of(Tags.Items.GEMS_DIAMOND),
                        Ingredient.of(Tags.Items.GUNPOWDERS),
                        Ingredient.of(Items.PORKCHOP),
                        Ingredient.of(ItemTags.SWORDS),
                        Ingredient.of(Items.GLASS_BOTTLE)
                )
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .entityToSummon(OccultismEntities.DEMONIC_WIFE.get())
                .entityToSacrifice(OccultismTags.Entities.CHICKEN)
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.chicken")
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/summon_demonic_wife"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_DJINNI.get(), "item.occultism.ritual_dummy.summon_djinni_clear_weather"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/summon_djinni_clear_weather")),
                        60,
                        RITUAL_SUMMON_JOB,
                        PENTACLE_SUMMON_DJINNI,
                        Ingredient.of(Tags.Items.CROPS_BEETROOT),
                        Ingredient.of(Tags.Items.CROPS_CARROT),
                        Ingredient.of(Tags.Items.CROPS_POTATO),
                        Ingredient.of(Tags.Items.CROPS_WHEAT))
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .spiritMaxAge(60)
                .spiritJobType(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "clear_weather"))
                .entityToSummon(OccultismEntities.DJINNI.get())
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/summon_djinni_clear_weather"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_DJINNI.get(), "item.occultism.ritual_dummy.summon_djinni_crusher"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/summon_djinni_crusher")),
                        90,
                        RITUAL_SUMMON_JOB,
                        PENTACLE_SUMMON_DJINNI,
                        Ingredient.of(OccultismTags.Items.IRON_DUST),
                        Ingredient.of(OccultismTags.Items.GOLD_DUST),
                        Ingredient.of(OccultismTags.Items.COPPER_DUST),
                        Ingredient.of(OccultismTags.Items.SILVER_DUST))
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .spiritMaxAge(-1)
                .spiritJobType(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "crush_tier2"))
                .entityToSummon(OccultismEntities.DJINNI.get())
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/summon_djinni_crusher"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_MARID.get(), "item.occultism.ritual_dummy.summon_djinni_day_time"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/summon_djinni_day_time")),
                        60,
                        RITUAL_SUMMON_JOB,
                        PENTACLE_SUMMON_DJINNI,
                        Ingredient.of(Items.TORCH),
                        Ingredient.of(ItemTags.SAPLINGS),
                        Ingredient.of(Items.WHEAT),
                        Ingredient.of(Tags.Items.DYES_YELLOW))
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .spiritMaxAge(60)
                .spiritJobType(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "day_time"))
                .entityToSummon(OccultismEntities.DJINNI.get())
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/summon_djinni_day_time"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        new ItemStack(OccultismItems.BOOK_OF_CALLING_DJINNI_MANAGE_MACHINE.get()),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/summon_djinni_manage_machine")),
                        60,
                        RITUAL_SUMMON_JOB,
                        PENTACLE_SUMMON_DJINNI,
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_COAL),
                        Ingredient.of(Tags.Items.INGOTS_GOLD),
                        Ingredient.of(Tags.Items.INGOTS_IRON),
                        Ingredient.of(Blocks.FURNACE))
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .spiritMaxAge(-1)
                .spiritJobType(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "manage_machine"))
                .entityToSummon(OccultismEntities.DJINNI.get())
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/summon_djinni_manage_machine"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_DJINNI.get(), "item.occultism.ritual_dummy.summon_djinni_night_time"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/summon_djinni_night_time")),
                        60,
                        RITUAL_SUMMON_JOB,
                        PENTACLE_SUMMON_DJINNI,
                        Ingredient.of(ItemTags.BEDS),
                        Ingredient.of(Items.ROTTEN_FLESH),
                        Ingredient.of(Tags.Items.BONES),
                        Ingredient.of(Tags.Items.DYES_BLACK))
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .spiritMaxAge(60)
                .spiritJobType(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "night_time"))
                .entityToSummon(OccultismEntities.DJINNI.get())
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/summon_djinni_night_time"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()),
                        new ItemStack(OccultismItems.BOOK_OF_CALLING_FOLIOT_CLEANER.get()),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/summon_foliot_cleaner")),
                        60,
                        RITUAL_SUMMON_JOB,
                        PENTACLE_SUMMON_FOLIOT,
                        Ingredient.of(OccultismItems.BRUSH.get()),
                        Ingredient.of(Tags.Items.CHESTS),
                        Ingredient.of(Blocks.DISPENSER),
                        Ingredient.of(Blocks.HOPPER))
                .unlockedBy("has_bound_foliot", has(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()))
                .spiritMaxAge(-1)
                .spiritJobType(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "cleaner"))
                .entityToSummon(OccultismEntities.FOLIOT.get())
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/summon_foliot_cleaner"));


        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_FOLIOT.get(), "item.occultism.ritual_dummy.summon_foliot_crusher"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/summon_foliot_crusher")),
                        60,
                        RITUAL_SUMMON_JOB,
                        PENTACLE_SUMMON_FOLIOT,
                        Ingredient.of(Tags.Items.RAW_MATERIALS_IRON),
                        Ingredient.of(Tags.Items.RAW_MATERIALS_GOLD),
                        Ingredient.of(Tags.Items.RAW_MATERIALS_COPPER),
                        Ingredient.of(OccultismTags.Items.RAW_MATERIALS_SILVER))
                .unlockedBy("has_bound_foliot", has(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()))
//                .condition(
//                        new OrCondition(
//                                List.of(
//                                        new IsInDimensionTypeCondition(registries.lookupOrThrow(Registries.DIMENSION_TYPE).getOrThrow(BuiltinDimensionTypes.NETHER)),
//                                        new IsInBiomeWithTagCondition(BiomeTags.HAS_NETHER_FORTRESS)
//                                )
//                        ))
                .spiritMaxAge(-1)
                .spiritJobType(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "crush_tier1"))
                .entityToSummon(OccultismEntities.FOLIOT.get())
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/summon_foliot_crusher"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()),
                        new ItemStack(OccultismItems.BOOK_OF_CALLING_FOLIOT_LUMBERJACK.get()),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/summon_foliot_lumberjack")),
                        60,
                        RITUAL_SUMMON_JOB,
                        PENTACLE_SUMMON_FOLIOT,
                        Ingredient.of(OccultismBlocks.OTHERWORLD_SAPLING.get()),
                        Ingredient.of(Items.OAK_SAPLING),
                        Ingredient.of(Items.BIRCH_SAPLING),
                        Ingredient.of(Items.SPRUCE_SAPLING),
                        Ingredient.of(ItemTags.AXES))
                .unlockedBy("has_bound_foliot", has(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()))
                .spiritMaxAge(-1)
                .spiritJobType(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "lumberjack"))
                .entityToSummon(OccultismEntities.FOLIOT.get())
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/summon_foliot_lumberjack"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_FOLIOT.get(), "item.occultism.ritual_dummy.summon_foliot_otherstone_trader"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/summon_foliot_otherstone_trader")),
                        30,
                        RITUAL_SUMMON_JOB,
                        PENTACLE_SUMMON_FOLIOT,
                        Ingredient.of(Blocks.STONE),
                        Ingredient.of(Blocks.GRANITE),
                        Ingredient.of(Blocks.DIORITE),
                        Ingredient.of(Blocks.ANDESITE))
                .unlockedBy("has_bound_foliot", has(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()))
                .spiritMaxAge(3600)
                .spiritJobType(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "trade_otherstone_t1"))
                .entityToSummon(OccultismEntities.FOLIOT.get())
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/summon_foliot_otherstone_trader"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_FOLIOT.get(), "item.occultism.ritual_dummy.summon_foliot_sapling_trader"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/summon_foliot_sapling_trader")),
                        30,
                        RITUAL_SUMMON_JOB,
                        PENTACLE_SUMMON_FOLIOT,
                        Ingredient.of(Items.OAK_SAPLING),
                        Ingredient.of(Items.BIRCH_SAPLING),
                        Ingredient.of(Items.SPRUCE_SAPLING),
                        Ingredient.of(Items.JUNGLE_SAPLING))
                .unlockedBy("has_bound_foliot", has(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()))
                .spiritMaxAge(3600)
                .spiritJobType(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "trade_otherworld_saplings_t1"))
                .entityToSummon(OccultismEntities.FOLIOT.get())
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/summon_foliot_sapling_trader"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()),
                        new ItemStack(OccultismItems.BOOK_OF_CALLING_FOLIOT_TRANSPORT_ITEMS.get()),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/summon_foliot_transport_items")),
                        60,
                        RITUAL_SUMMON_JOB,
                        PENTACLE_SUMMON_FOLIOT,
                        Ingredient.of(Items.MINECART),
                        Ingredient.of(Tags.Items.CHESTS),
                        Ingredient.of(Blocks.DISPENSER),
                        Ingredient.of(Blocks.HOPPER))
                .unlockedBy("has_bound_foliot", has(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()))
                .spiritMaxAge(-1)
                .spiritJobType(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "transport_items"))
                .entityToSummon(OccultismEntities.FOLIOT.get())
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/summon_foliot_transport_items"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_MARID.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_MARID.get(), "item.occultism.ritual_dummy.summon_marid_crusher"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/summon_marid_crusher")),
                        150,
                        RITUAL_SUMMON_JOB,
                        PENTACLE_SUMMON_MARID,
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_DIAMOND),
                        Ingredient.of(Items.GHAST_TEAR),
                        Ingredient.of(OccultismTags.Items.STORAGE_BLOCK_IESNIUM),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_EMERALD))
                .unlockedBy("has_bound_marid", has(OccultismItems.BOOK_OF_BINDING_BOUND_MARID.get()))
                .spiritMaxAge(-1)
                .spiritJobType(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "crush_tier4"))
                .entityToSummon(OccultismEntities.MARID.get())
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/summon_marid_crusher"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()),
                        makeLoreSpawnEgg(OccultismItems.AFRIT_ESSENCE.get(), "item.occultism.ritual_dummy.summon_unbound_afrit"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/summon_unbound_afrit")),
                        60,
                        RITUAL_SUMMON,
                        PENTACLE_SUMMON_UNBOUND_AFRIT,
                        Ingredient.of(Tags.Items.NETHERRACKS),
                        Ingredient.of(Tags.Items.GEMS_QUARTZ),
                        Ingredient.of(Items.FLINT_AND_STEEL),
                        Ingredient.of(Tags.Items.GUNPOWDERS))
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()))
                .entityToSummon(OccultismEntities.AFRIT_WILD.get())
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.cows")
                .entityToSacrifice(OccultismTags.Entities.COWS)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/summon_unbound_afrit"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_MARID.get()),
                        makeLoreSpawnEgg(OccultismItems.MARID_ESSENCE.get(), "item.occultism.ritual_dummy.summon_unbound_marid"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/summon_unbound_marid")),
                        90,
                        RITUAL_SUMMON,
                        PENTACLE_SUMMON_UNBOUND_MARID,
                        Ingredient.of(Items.CONDUIT),
                        Ingredient.of(Tags.Items.GEMS_PRISMARINE),
                        Ingredient.of(Items.PRISMARINE_SHARD),
                        Ingredient.of(Items.GHAST_TEAR))
                .unlockedBy("has_bound_marid", has(OccultismItems.BOOK_OF_BINDING_BOUND_MARID.get()))
                .entityToSummon(OccultismEntities.MARID_UNBOUND.get())
                .itemToUse(Ingredient.of(Items.TRIDENT))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/summon_unbound_marid"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(Items.SKELETON_SKULL),
                        makeLoreSpawnEgg(Items.WITHER_SKELETON_SKULL, "item.occultism.ritual_dummy.wild_hunt"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/wild_hunt")),
                        30,
                        RITUAL_SUMMON_WILD,
                        PENTACLE_CONTACT_WILD_SPIRIT,
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_COPPER),
                        Ingredient.of(OccultismTags.Items.STORAGE_BLOCK_SILVER),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_GOLD),
                        Ingredient.of(Tags.Items.GEMS_DIAMOND),
                        Ingredient.of(Tags.Items.NETHERRACKS),
                        Ingredient.of(Items.SOUL_SAND))
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()))
                .entityToSummon(OccultismEntities.WILD_HUNT_WITHER_SKELETON.get())
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.humans")
                .entityToSacrifice(OccultismTags.Entities.HUMANS)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/wild_hunt"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(Items.RAW_GOLD),
                        makeLoreSpawnEgg(Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, "item.occultism.ritual_dummy.wild_husk"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/wild_husk")),
                        30,
                        RITUAL_SUMMON_WILD,
                        PENTACLE_CONTACT_WILD_SPIRIT,
                        Ingredient.of(Items.SAND),
                        Ingredient.of(Items.SANDSTONE),
                        Ingredient.of(Items.CHISELED_SANDSTONE),
                        Ingredient.of(Items.CUT_SANDSTONE),
                        Ingredient.of(Items.SMOOTH_SANDSTONE),
                        Ingredient.of(Items.DEAD_BUSH),
                        Ingredient.of(Items.SAND),
                        Ingredient.of(Items.SANDSTONE),
                        Ingredient.of(Items.CHISELED_SANDSTONE),
                        Ingredient.of(Items.CUT_SANDSTONE),
                        Ingredient.of(Items.SMOOTH_SANDSTONE),
                        Ingredient.of(Items.DEAD_BUSH))
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()))
                .entityToSummon(OccultismEntities.WILD_HORDE_HUSK.get())
                .summonNumber(5)
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.camel")
                .entityToSacrifice(OccultismTags.Entities.CAMEL)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/wild_husk"));
        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(Items.HEART_OF_THE_SEA),
                        makeLoreSpawnEgg(Items.SNIFFER_EGG, "item.occultism.ritual_dummy.wild_drowned"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/wild_drowned")),
                        30,
                        RITUAL_SUMMON_WILD,
                        PENTACLE_CONTACT_WILD_SPIRIT,
                        Ingredient.of(Items.DEAD_BRAIN_CORAL_BLOCK),
                        Ingredient.of(Items.DEAD_BUBBLE_CORAL_BLOCK),
                        Ingredient.of(Items.DEAD_FIRE_CORAL_BLOCK),
                        Ingredient.of(Items.DEAD_HORN_CORAL_BLOCK),
                        Ingredient.of(Items.DEAD_TUBE_CORAL_BLOCK),
                        Ingredient.of(Items.DRIED_KELP_BLOCK))
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()))
                .entityToSummon(OccultismEntities.WILD_HORDE_DROWNED.get())
                .summonNumber(5)
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.dolphin")
                .entityToSacrifice(OccultismTags.Entities.DOLPHIN)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/wild_drowned"));
        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(Items.TNT),
                        makeLoreSpawnEgg(Items.MUSIC_DISC_CAT, "item.occultism.ritual_dummy.wild_creeper"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/wild_creeper")),
                        30,
                        RITUAL_SUMMON_WILD,
                        PENTACLE_CONTACT_WILD_SPIRIT,
                        Ingredient.of(Items.TNT),
                        Ingredient.of(Items.OAK_LEAVES),
                        Ingredient.of(Items.MOSS_BLOCK),
                        Ingredient.of(Items.TNT),
                        Ingredient.of(Items.BIRCH_LEAVES),
                        Ingredient.of(Items.MOSS_BLOCK),
                        Ingredient.of(Items.TNT),
                        Ingredient.of(Items.SPRUCE_LEAVES),
                        Ingredient.of(Items.MOSS_BLOCK),
                        Ingredient.of(Items.TNT),
                        Ingredient.of(Items.JUNGLE_LEAVES),
                        Ingredient.of(Items.MOSS_BLOCK))
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()))
                .entityToSummon(OccultismEntities.WILD_HORDE_CREEPER.get())
                .summonNumber(5)
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.pigs")
                .entityToSacrifice(OccultismTags.Entities.PIGS)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/wild_creeper"));
        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(Items.BRUSH),
                        makeLoreSpawnEgg(Items.MUSIC_DISC_RELIC, "item.occultism.ritual_dummy.wild_silverfish"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/wild_silverfish")),
                        30,
                        RITUAL_SUMMON_WILD,
                        PENTACLE_CONTACT_WILD_SPIRIT,
                        Ingredient.of(Items.SAND),
                        Ingredient.of(Items.GRAVEL),
                        Ingredient.of(Items.BRICKS),
                        Ingredient.of(Items.MUD_BRICKS),
                        Ingredient.of(Items.STONE_BRICKS),
                        Ingredient.of(Items.WHITE_TERRACOTTA),
                        Ingredient.of(Items.DIRT))
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()))
                .entityToSummon(OccultismEntities.WILD_HORDE_SILVERFISH.get())
                .summonNumber(7)
                .itemToUse(Ingredient.of(Items.EGG))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/wild_silverfish"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(Items.HONEYCOMB),
                        makeLoreSpawnEgg(Items.TRIAL_KEY, "item.occultism.ritual_dummy.wild_weak_breeze"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/wild_weak_breeze")),
                        30,
                        RITUAL_SUMMON_WILD,
                        PENTACLE_CONTACT_WILD_SPIRIT,
                        Ingredient.of(Items.OXIDIZED_COPPER),
                        Ingredient.of(Items.WEATHERED_CHISELED_COPPER),
                        Ingredient.of(Items.EXPOSED_COPPER_GRATE),
                        Ingredient.of(Items.CUT_COPPER),
                        Ingredient.of(Items.TUFF),
                        Ingredient.of(Items.TUFF),
                        Ingredient.of(Items.TUFF),
                        Ingredient.of(Items.TUFF))
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()))
                .entityToSummon(OccultismEntities.POSSESSED_WEAK_BREEZE.get())
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.snow_golem")
                .entityToSacrifice(OccultismTags.Entities.SNOW_GOLEM)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/wild_weak_breeze"));
        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(Items.TRIAL_KEY),
                        makeLoreSpawnEgg(Items.OMINOUS_TRIAL_KEY, "item.occultism.ritual_dummy.wild_breeze"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/wild_breeze")),
                        30,
                        RITUAL_SUMMON_WILD,
                        PENTACLE_CONTACT_WILD_SPIRIT,
                        Ingredient.of(Items.LIGHTNING_ROD),
                        Ingredient.of(Items.COPPER_DOOR),
                        Ingredient.of(Items.COPPER_TRAPDOOR),
                        Ingredient.of(Items.COPPER_BULB),
                        Ingredient.of(Items.POLISHED_TUFF),
                        Ingredient.of(Items.TUFF_BRICKS))
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()))
                .entityToSummon(OccultismEntities.POSSESSED_BREEZE.get())
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.wolfs")
                .entityToSacrifice(OccultismTags.Entities.WOLFS)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/wild_breeze"));
        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(Items.OMINOUS_TRIAL_KEY),
                        makeLoreSpawnEgg(Items.HEAVY_CORE, "item.occultism.ritual_dummy.wild_strong_breeze"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/wild_strong_breeze")),
                        30,
                        RITUAL_SUMMON_WILD,
                        PENTACLE_CONTACT_WILD_SPIRIT,
                        Ingredient.of(Items.CHISELED_TUFF),
                        Ingredient.of(Items.CHISELED_TUFF),
                        Ingredient.of(Items.CHISELED_TUFF),
                        Ingredient.of(Items.CHISELED_TUFF),
                        Ingredient.of(Items.CHISELED_TUFF_BRICKS),
                        Ingredient.of(Items.CHISELED_TUFF_BRICKS),
                        Ingredient.of(Items.CHISELED_TUFF_BRICKS),
                        Ingredient.of(Items.CHISELED_TUFF_BRICKS),
                        Ingredient.of(Items.BREEZE_ROD),
                        Ingredient.of(Items.BREEZE_ROD),
                        Ingredient.of(Items.OMINOUS_BOTTLE))
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()))
                .entityToSummon(OccultismEntities.POSSESSED_STRONG_BREEZE.get())
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.iron_golem")
                .entityToSacrifice(OccultismTags.Entities.IRON_GOLEM)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/wild_strong_breeze"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(Items.GOLDEN_APPLE),
                        makeLoreSpawnEgg(Items.TOTEM_OF_UNDYING, "item.occultism.ritual_dummy.wild_horde_illager"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/wild_horde_illager")),
                        30,
                        RITUAL_SUMMON_WILD,
                        PENTACLE_CONTACT_WILD_SPIRIT,
                        Ingredient.of(Items.DARK_OAK_LOG),
                        Ingredient.of(Items.DARK_OAK_LOG),
                        Ingredient.of(Items.DARK_OAK_LOG),
                        Ingredient.of(OccultismTags.Items.EMERALD_DUST),
                        Ingredient.of(OccultismTags.Items.EMERALD_DUST),
                        Ingredient.of(OccultismTags.Items.EMERALD_DUST))
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()))
                .entityToSummon(OccultismEntities.POSSESSED_EVOKER.get())
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.humans")
                .entityToSacrifice(OccultismTags.Entities.HUMANS)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/wild_horde_illager"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_OTHERWORLD_BIRD.get(), "item.occultism.ritual_dummy.possess_unbound_otherworld_bird"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/possess_unbound_otherworld_bird")),
                        30,
                        RITUAL_SUMMON,
                        PENTACLE_POSSESS_DJINNI,
                        Ingredient.of(Tags.Items.FEATHERS),
                        Ingredient.of(Tags.Items.FEATHERS),
                        Ingredient.of(Items.COBWEB),
                        Ingredient.of(ItemTags.LEAVES),
                        Ingredient.of(Items.EGG))
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .entityToSummon(OccultismEntities.OTHERWORLD_BIRD.get())
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.parrots")
                .entityToSacrifice(OccultismTags.Entities.PARROTS)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/possess_unbound_otherworld_bird"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()),
                        makeLoreSpawnEgg(Items.PARROT_SPAWN_EGG, "item.occultism.ritual_dummy.possess_unbound_parrot"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/possess_unbound_parrot")),
                        30,
                        RITUAL_SUMMON,
                        PENTACLE_POSSESS_FOLIOT,
                        Ingredient.of(Tags.Items.FEATHERS),
                        Ingredient.of(Tags.Items.DYES_GREEN),
                        Ingredient.of(Tags.Items.DYES_YELLOW),
                        Ingredient.of(Tags.Items.DYES_RED),
                        Ingredient.of(Tags.Items.DYES_BLUE))
                .unlockedBy("has_bound_foliot", has(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()))
                .entityToSummon(EntityType.PARROT)
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.chicken")
                .entityToSacrifice(OccultismTags.Entities.CHICKEN)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/possess_unbound_parrot"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()),
                        makeLoreSpawnEgg(Items.SHEEP_SPAWN_EGG, "item.occultism.ritual_dummy.possess_random_animal"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/possess_random_animal")),
                        30,
                        RITUAL_SUMMON,
                        PENTACLE_POSSESS_FOLIOT,
                        Ingredient.of(Tags.Items.CROPS),
                        Ingredient.of(Tags.Items.CROPS),
                        Ingredient.of(Tags.Items.CROPS),
                        Ingredient.of(Tags.Items.CROPS),
                        Ingredient.of(Tags.Items.CROPS),
                        Ingredient.of(Tags.Items.CROPS),
                        Ingredient.of(Tags.Items.CROPS),
                        Ingredient.of(Tags.Items.CROPS),
                        Ingredient.of(Items.EGG))
                .entityTagToSummon(OccultismTags.Entities.RANDOM_ANIMALS_TO_SUMMON_LIST)
                .unlockedBy("has_bound_foliot", has(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/possess_random_animal"));
    }

    private static void possessRituals(RecipeOutput recipeOutput) {
        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()),
                        makeLoreSpawnEgg(Items.HEART_OF_THE_SEA, "item.occultism.ritual_dummy.possess_elder_guardian"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/possess_elder_guardian")),
                        90,
                        RITUAL_SUMMON,
                        PENTACLE_POSSESS_AFRIT,
                        Ingredient.of(Items.OXIDIZED_COPPER),
                        Ingredient.of(Items.PRISMARINE),
                        Ingredient.of(Items.PRISMARINE_BRICKS),
                        Ingredient.of(Items.DARK_PRISMARINE),
                        Ingredient.of(Items.YELLOW_WOOL),
                        Ingredient.of(Items.SEA_LANTERN),
                        Ingredient.of(Items.WATER_BUCKET),
                        Ingredient.of(Items.WATER_BUCKET),
                        Ingredient.of(Items.WATER_BUCKET),
                        Ingredient.of(Tags.Items.GEMS_EMERALD))
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()))
                .entityToSummon(OccultismEntities.POSSESSED_ELDER_GUARDIAN_TYPE.get())
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.fish")
                .entityToSacrifice(OccultismTags.Entities.FISH)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/possess_elder_guardian"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI),
                        makeLoreSpawnEgg(Items.ENDER_PEARL, "item.occultism.ritual_dummy.possess_enderman"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/possess_enderman")),
                        60,
                        RITUAL_SUMMON,
                        PENTACLE_POSSESS_DJINNI,
                        Ingredient.of(Tags.Items.BONES),
                        Ingredient.of(Tags.Items.STRINGS),
                        Ingredient.of(Tags.Items.END_STONES),
                        Ingredient.of(Items.ROTTEN_FLESH))
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .entityToSummon(OccultismEntities.POSSESSED_ENDERMAN_TYPE.get())
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.pigs")
                .entityToSacrifice(OccultismTags.Entities.PIGS)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/possess_enderman"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()),
                        makeLoreSpawnEgg(Items.END_STONE, "item.occultism.ritual_dummy.possess_endermite"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/possess_endermite")),
                        30,
                        RITUAL_SUMMON,
                        PENTACLE_POSSESS_FOLIOT,
                        Ingredient.of(ItemTags.DIRT),
                        Ingredient.of(Tags.Items.STONES),
                        Ingredient.of(ItemTags.DIRT),
                        Ingredient.of(Tags.Items.STONES))
                .unlockedBy("has_bound_foliot", has(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()))
                .entityToSummon(OccultismEntities.POSSESSED_ENDERMITE_TYPE.get())
                .itemToUse(Ingredient.of(Items.EGG))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/possess_endermite"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        makeLoreSpawnEgg(Items.GHAST_TEAR, "item.occultism.ritual_dummy.possess_ghast"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/possess_ghast")),
                        60,
                        RITUAL_SUMMON,
                        PENTACLE_POSSESS_DJINNI,
                        Ingredient.of(Tags.Items.NETHERRACKS),
                        Ingredient.of(Tags.Items.NETHERRACKS),
                        Ingredient.of(Tags.Items.NETHERRACKS),
                        Ingredient.of(OccultismTags.Items.MAGMA),
                        Ingredient.of(OccultismTags.Items.MAGMA),
                        Ingredient.of(OccultismTags.Items.MAGMA),
                        Ingredient.of(Items.LAVA_BUCKET),
                        Ingredient.of(Items.LAVA_BUCKET),
                        Ingredient.of(Items.LAVA_BUCKET),
                        Ingredient.of(Tags.Items.GEMS_DIAMOND))
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .entityToSummon(OccultismEntities.POSSESSED_GHAST_TYPE.get())
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.cows")
                .entityToSacrifice(OccultismTags.Entities.COWS)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/possess_ghast"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()),
                        makeLoreSpawnEgg(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, "item.occultism.ritual_dummy.possess_hoglin"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/possess_hoglin")),
                        60,
                        RITUAL_SUMMON,
                        PENTACLE_POSSESS_AFRIT,
                        Ingredient.of(Items.NETHERITE_SCRAP),
                        Ingredient.of(Tags.Items.LEATHERS),
                        Ingredient.of(Tags.Items.NETHERRACKS),
                        Ingredient.of(Tags.Items.NETHERRACKS),
                        Ingredient.of(Items.PORKCHOP),
                        Ingredient.of(Items.PORKCHOP),
                        Ingredient.of(Items.PORKCHOP),
                        Ingredient.of(OccultismBlocks.SPIRIT_ATTUNED_CRYSTAL))
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()))
                .entityToSummon(OccultismEntities.POSSESSED_HOGLIN_TYPE.get())
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.pigs")
                .entityToSacrifice(OccultismTags.Entities.PIGS)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/possess_hoglin"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()),
                        makeLoreSpawnEgg(Items.PHANTOM_MEMBRANE, "item.occultism.ritual_dummy.possess_phantom"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/possess_phantom")),
                        30,
                        RITUAL_SUMMON,
                        PENTACLE_POSSESS_FOLIOT,
                        Ingredient.of(Tags.Items.LEATHERS),
                        Ingredient.of(Tags.Items.FEATHERS),
                        Ingredient.of(Tags.Items.LEATHERS),
                        Ingredient.of(Tags.Items.FEATHERS))
                .unlockedBy("has_bound_foliot", has(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()))
                .entityToSummon(OccultismEntities.POSSESSED_PHANTOM_TYPE.get())
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.flying_passive")
                .entityToSacrifice(OccultismTags.Entities.FLYING_PASSIVE)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/possess_phantom"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()),
                        makeLoreSpawnEgg(Items.SHULKER_SHELL, "item.occultism.ritual_dummy.possess_shulker"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/possess_shulker")),
                        60,
                        RITUAL_SUMMON,
                        PENTACLE_POSSESS_AFRIT,
                        Ingredient.of(Items.DRAGON_BREATH),
                        Ingredient.of(Tags.Items.OBSIDIANS),
                        Ingredient.of(Tags.Items.END_STONES),
                        Ingredient.of(Items.PURPLE_GLAZED_TERRACOTTA))
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()))
                .entityToSummon(OccultismEntities.POSSESSED_SHULKER_TYPE.get())
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.cubemob")
                .entityToSacrifice(OccultismTags.Entities.CUBEMOB)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/possess_shulker"));


        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()),
                        makeLoreSpawnEgg(Items.SKELETON_SKULL, "item.occultism.ritual_dummy.possess_skeleton"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/possess_skeleton")),
                        15,
                        RITUAL_SUMMON,
                        PENTACLE_POSSESS_FOLIOT,
                        Ingredient.of(Tags.Items.BONES),
                        Ingredient.of(Tags.Items.BONES),
                        Ingredient.of(Tags.Items.BONES),
                        Ingredient.of(Tags.Items.BONES))
                .unlockedBy("has_bound_foliot", has(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()))
                .entityToSummon(OccultismEntities.POSSESSED_SKELETON_TYPE.get())
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.chicken")
                .entityToSacrifice(OccultismTags.Entities.CHICKEN)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/possess_skeleton"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        makeLoreSpawnEgg(Items.ECHO_SHARD, "item.occultism.ritual_dummy.possess_warden"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/possess_warden")),
                        60,
                        RITUAL_SUMMON,
                        PENTACLE_POSSESS_DJINNI,
                        Ingredient.of(Items.SCULK_SHRIEKER),
                        Ingredient.of(Items.SCULK_SENSOR),
                        Ingredient.of(Items.SCULK),
                        Ingredient.of(Items.SCULK),
                        Ingredient.of(Items.SCULK_SENSOR),
                        Ingredient.of(Items.SCULK),
                        Ingredient.of(Items.SCULK),
                        Ingredient.of(Items.SCULK_SENSOR),
                        Ingredient.of(Items.SCULK),
                        Ingredient.of(Items.SCULK))
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.axolotls")
                .entityToSacrifice(OccultismTags.Entities.AXOLOTL)
                .entityToSummon(OccultismEntities.POSSESSED_WARDEN_TYPE.get())
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/possess_warden"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        makeLoreSpawnEgg(Items.CHORUS_FRUIT, "item.occultism.ritual_dummy.possess_weak_shulker"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/possess_weak_shulker")),
                        60,
                        RITUAL_SUMMON,
                        PENTACLE_POSSESS_DJINNI,
                        Ingredient.of(Tags.Items.ENDER_PEARLS),
                        Ingredient.of(Items.PURPLE_CONCRETE),
                        Ingredient.of(Tags.Items.END_STONES),
                        Ingredient.of(Items.MAGENTA_CONCRETE))
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .entityToSacrifice(OccultismTags.Entities.CUBEMOB)
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.cubemob")
                .entityToSummon(OccultismEntities.POSSESSED_WEAK_SHULKER_TYPE.get())
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/possess_weak_shulker"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()),
                        makeLoreSpawnEgg(Items.EXPERIENCE_BOTTLE, "item.occultism.ritual_dummy.possess_witch"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/possess_witch")),
                        30,
                        RITUAL_SUMMON,
                        PENTACLE_POSSESS_FOLIOT,
                        Ingredient.of(Items.GLASS_BOTTLE),
                        Ingredient.of(Tags.Items.DUSTS_REDSTONE),
                        Ingredient.of(Items.BROWN_MUSHROOM),
                        Ingredient.of(Items.RED_MUSHROOM))
                .unlockedBy("has_bound_foliot", has(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()))
                .entityToSummon(OccultismEntities.POSSESSED_WITCH_TYPE.get())
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.cats")
                .entityToSacrifice(OccultismTags.Entities.CATS)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/possess_witch"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()),
                        makeLoreSpawnEgg(OccultismItems.DEMONIC_MEAT.get(), "item.occultism.ritual_dummy.possess_zombie_piglin"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/possess_zombie_piglin")),
                        90,
                        RITUAL_SUMMON,
                        PENTACLE_POSSESS_UNBOUND_AFRIT,
                        Ingredient.of(Items.CHERRY_LEAVES),
                        Ingredient.of(Items.PINK_PETALS),
                        Ingredient.of(OccultismItems.TALLOW),
                        Ingredient.of(Items.QUARTZ))
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()))
                .entityToSummon(OccultismEntities.POSSESSED_ZOMBIE_PIGLIN_TYPE.get())
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.pigs")
                .entityToSacrifice(OccultismTags.Entities.PIGS)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/possess_zombie_piglin"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        makeLoreSpawnEgg(OccultismItems.CURSED_HONEY.get(), "item.occultism.ritual_dummy.possess_bee"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/possess_bee")),
                        60,
                        RITUAL_SUMMON,
                        PENTACLE_POSSESS_DJINNI,
                        Ingredient.of(Items.HONEYCOMB),
                        Ingredient.of(Items.HONEY_BLOCK),
                        Ingredient.of(Items.HONEY_BOTTLE),
                        Ingredient.of(Items.HONEYCOMB_BLOCK))
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .entityToSummon(OccultismEntities.POSSESSED_BEE_TYPE.get())
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.bats")
                .entityToSacrifice(OccultismTags.Entities.BATS)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/possess_bee"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_MARID.get()),
                        makeLoreSpawnEgg(OccultismItems.CRUELTY_ESSENCE.get(), "item.occultism.ritual_dummy.possess_goat"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/possess_goat")),
                        240,
                        RITUAL_SUMMON,
                        PENTACLE_POSSESS_MARID,
                        Ingredient.of(Items.POINTED_DRIPSTONE),
                        Ingredient.of(Items.POINTED_DRIPSTONE),
                        Ingredient.of(Items.RABBIT_FOOT),
                        Ingredient.of(Items.RABBIT_FOOT),
                        Ingredient.of(Items.RABBIT_FOOT),
                        Ingredient.of(Items.RABBIT_FOOT),
                        Ingredient.of(Items.ARMADILLO_SCUTE),
                        Ingredient.of(Items.ARMADILLO_SCUTE),
                        Ingredient.of(Items.ARMADILLO_SCUTE),
                        Ingredient.of(Items.ARMADILLO_SCUTE),
                        Ingredient.of(ItemTags.WOOL),
                        Ingredient.of(ItemTags.WOOL))
                .unlockedBy("has_bound_marid", has(OccultismItems.BOOK_OF_BINDING_BOUND_MARID.get()))
                .entityToSummon(OccultismEntities.GOAT_OF_MERCY_TYPE.get())
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.tadpoles")
                .entityToSacrifice(OccultismTags.Entities.TADPOLES)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/possess_goat"));
    }

    private static void familiarRituals(RecipeOutput recipeOutput) {

        RitualRecipeBuilder.ritualRecipeBuilder(
                        Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_BAT_FAMILIAR.get(), "item.occultism.ritual_dummy.familiar_bat"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/familiar_bat")),
                        60,
                        RITUAL_FAMILIAR,
                        PENTACLE_POSSESS_DJINNI,
                        Ingredient.of(Items.GOLDEN_CARROT),
                        Ingredient.of(Items.SPIDER_EYE),
                        Ingredient.of(Tags.Items.DUSTS_GLOWSTONE),
                        Ingredient.of(Items.LAVA_BUCKET),
                        Ingredient.of(Items.TORCH)
                )
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .entityToSummon(OccultismEntities.BAT_FAMILIAR_TYPE.get())
                .entityToSacrifice(OccultismTags.Entities.BATS)
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.bats")
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/familiar_bat"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_BEAVER_FAMILIAR.get(), "item.occultism.ritual_dummy.familiar_beaver"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/familiar_beaver")),
                        30,
                        RITUAL_FAMILIAR,
                        PENTACLE_POSSESS_FOLIOT,
                        Ingredient.of(ItemTags.LOGS),
                        Ingredient.of(ItemTags.LOGS),
                        Ingredient.of(ItemTags.LOGS),
                        Ingredient.of(ItemTags.LOGS))
                .unlockedBy("has_bound_foliot", has(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()))
                .entityToSummon(OccultismEntities.BEAVER_FAMILIAR_TYPE.get())
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/familiar_beaver"));


        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_BEHOLDER_FAMILIAR.get(), "item.occultism.ritual_dummy.familiar_beholder"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/familiar_beholder")),
                        30,
                        RITUAL_FAMILIAR,
                        PENTACLE_POSSESS_DJINNI,
                        Ingredient.of(Items.SPIDER_EYE),
                        Ingredient.of(Items.SPIDER_EYE),
                        Ingredient.of(Items.SPIDER_EYE),
                        Ingredient.of(Items.SPIDER_EYE),
                        Ingredient.of(Tags.Items.DUSTS_GLOWSTONE),
                        Ingredient.of(Tags.Items.DUSTS_GLOWSTONE),
                        Ingredient.of(Tags.Items.DUSTS_GLOWSTONE),
                        Ingredient.of(Tags.Items.DUSTS_GLOWSTONE)
                )
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .entityToSummon(OccultismEntities.BEHOLDER_FAMILIAR_TYPE.get())
                .entityToSacrifice(OccultismTags.Entities.SPIDERS)
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.spiders")
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/familiar_beholder"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_BLACKSMITH_FAMILIAR.get(), "item.occultism.ritual_dummy.familiar_blacksmith"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/familiar_blacksmith")),
                        30,
                        RITUAL_FAMILIAR,
                        PENTACLE_POSSESS_FOLIOT,
                        Ingredient.of(Items.IRON_SHOVEL),
                        Ingredient.of(Items.IRON_PICKAXE),
                        Ingredient.of(Items.IRON_AXE),
                        Ingredient.of(Items.ANVIL),
                        Ingredient.of(Tags.Items.STONES),
                        Ingredient.of(Tags.Items.STONES),
                        Ingredient.of(Tags.Items.STONES),
                        Ingredient.of(Tags.Items.STONES))
                .unlockedBy("has_bound_foliot", has(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()))
                .entityToSummon(OccultismEntities.BLACKSMITH_FAMILIAR_TYPE.get())
                .entityToSacrifice(OccultismTags.Entities.ZOMBIES)
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.zombies")
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/familiar_blacksmith"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_CTHULHU_FAMILIAR.get(), "item.occultism.ritual_dummy.familiar_cthulhu"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/familiar_cthulhu")),
                        60,
                        RITUAL_FAMILIAR,
                        PENTACLE_POSSESS_DJINNI,
                        Ingredient.of(ItemTags.FISHES),
                        Ingredient.of(ItemTags.FISHES),
                        Ingredient.of(ItemTags.FISHES),
                        Ingredient.of(ItemTags.FISHES),
                        Ingredient.of(ItemTags.FISHES),
                        Ingredient.of(ItemTags.FISHES),
                        Ingredient.of(ItemTags.FISHES),
                        Ingredient.of(ItemTags.FISHES))
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .entityToSacrifice(OccultismTags.Entities.SQUID)
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.squid")
                .entityToSummon(OccultismEntities.CTHULHU_FAMILIAR_TYPE.get())
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/familiar_cthulhu"));


        RitualRecipeBuilder.ritualRecipeBuilder(
                        Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_CHIMERA_FAMILIAR.get(), "item.occultism.ritual_dummy.familiar_chimera"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/familiar_chimera")),
                        60,
                        RITUAL_FAMILIAR,
                        PENTACLE_POSSESS_DJINNI,
                        Ingredient.of(Tags.Items.LEATHERS),
                        Ingredient.of(Tags.Items.STRINGS),
                        Ingredient.of(Tags.Items.FEATHERS),
                        Ingredient.of(ItemTags.WOOL),
                        Ingredient.of(Tags.Items.BONES),
                        Ingredient.of(Items.MUTTON),
                        Ingredient.of(Items.PORKCHOP),
                        Ingredient.of(Items.BEEF),
                        Ingredient.of(Items.CHICKEN)
                )
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .entityToSummon(OccultismEntities.CHIMERA_FAMILIAR_TYPE.get())
                .entityToSacrifice(OccultismTags.Entities.SHEEP)
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.sheep")
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/familiar_chimera"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_DEER_FAMILIAR.get(), "item.occultism.ritual_dummy.familiar_deer"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/familiar_deer")),
                        15,
                        RITUAL_FAMILIAR,
                        PENTACLE_POSSESS_FOLIOT,
                        Ingredient.of(Tags.Items.RODS_WOODEN),
                        Ingredient.of(Tags.Items.RODS_WOODEN),
                        Ingredient.of(Tags.Items.RODS_WOODEN),
                        Ingredient.of(Tags.Items.RODS_WOODEN),
                        Ingredient.of(Tags.Items.STRINGS),
                        Ingredient.of(Tags.Items.STRINGS))
                .unlockedBy("has_bound_foliot", has(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()))
                .entityToSummon(OccultismEntities.DEER_FAMILIAR_TYPE.get())
                .entityToSacrifice(OccultismTags.Entities.COWS)
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.cows")
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/familiar_deer"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_DEVIL_FAMILIAR.get(), "item.occultism.ritual_dummy.familiar_devil"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/familiar_devil")),
                        60,
                        RITUAL_FAMILIAR,
                        PENTACLE_POSSESS_DJINNI,
                        Ingredient.of(Items.LAVA_BUCKET),
                        Ingredient.of(Items.LAVA_BUCKET),
                        Ingredient.of(Items.LAVA_BUCKET),
                        Ingredient.of(Items.LAVA_BUCKET),
                        Ingredient.of(Tags.Items.BONES),
                        Ingredient.of(Tags.Items.BONES),
                        Ingredient.of(Tags.Items.BONES),
                        Ingredient.of(Tags.Items.BONES))
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .entityToSummon(OccultismEntities.DEVIL_FAMILIAR_TYPE.get())
                .entityToSacrifice(OccultismTags.Entities.HORSES)
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.horses")
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/familiar_devil"));


        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_DRAGON_FAMILIAR.get(), "item.occultism.ritual_dummy.familiar_dragon"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/familiar_dragon")),
                        60,
                        RITUAL_FAMILIAR,
                        PENTACLE_POSSESS_DJINNI,
                        Ingredient.of(Items.LAVA_BUCKET),
                        Ingredient.of(Items.FLINT_AND_STEEL),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_COAL),
                        Ingredient.of(Items.QUARTZ_BLOCK),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_GOLD),
                        Ingredient.of(Tags.Items.GUNPOWDERS),
                        Ingredient.of(Items.OBSIDIAN),
                        Ingredient.of(Items.OBSIDIAN))
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .entityToSummon(OccultismEntities.DRAGON_FAMILIAR_TYPE.get())
                .entityToSacrifice(OccultismTags.Entities.HORSES)
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.horses")
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/familiar_dragon"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_FAIRY_FAMILIAR.get(), "item.occultism.ritual_dummy.familiar_fairy"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/familiar_fairy")),
                        30,
                        RITUAL_FAMILIAR,
                        PENTACLE_POSSESS_DJINNI,
                        Ingredient.of(Items.GOLDEN_APPLE),
                        Ingredient.of(Items.GOLDEN_APPLE),
                        Ingredient.of(Items.GHAST_TEAR),
                        Ingredient.of(Tags.Items.GUNPOWDERS),
                        Ingredient.of(Tags.Items.GUNPOWDERS),
                        Ingredient.of(Tags.Items.GUNPOWDERS),
                        Ingredient.of(Items.DRAGON_BREATH))
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .entityToSummon(OccultismEntities.FAIRY_FAMILIAR_TYPE.get())
                .entityToSacrifice(OccultismTags.Entities.HORSES)
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.horses")
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/familiar_fairy"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_GREEDY_FAMILIAR.get(), "item.occultism.ritual_dummy.familiar_greedy"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/familiar_greedy")),
                        30,
                        RITUAL_FAMILIAR,
                        PENTACLE_POSSESS_FOLIOT,
                        Ingredient.of(Tags.Items.CHESTS),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_IRON),
                        Ingredient.of(Items.DISPENSER),
                        Ingredient.of(Items.HOPPER))
                .unlockedBy("has_bound_foliot", has(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()))
                .entityToSummon(OccultismEntities.GREEDY_FAMILIAR_TYPE.get())
                .entityToSacrifice(OccultismTags.Entities.ZOMBIES)
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.zombies")
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/familiar_greedy"));
        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_GUARDIAN_FAMILIAR.get(), "item.occultism.ritual_dummy.familiar_guardian"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/familiar_guardian")),
                        60,
                        RITUAL_FAMILIAR,
                        PENTACLE_POSSESS_AFRIT,
                        Ingredient.of(Tags.Items.GEMS_DIAMOND),
                        Ingredient.of(Tags.Items.GEMS_DIAMOND),
                        Ingredient.of(Tags.Items.GEMS_DIAMOND),
                        Ingredient.of(Tags.Items.GEMS_DIAMOND),
                        Ingredient.of(Items.GOLDEN_APPLE),
                        Ingredient.of(Items.GOLDEN_APPLE))
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()))
                .entityToSummon(OccultismEntities.GUARDIAN_FAMILIAR_TYPE.get())
                .entityToSacrifice(OccultismTags.Entities.HUMANS)
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.humans")
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/familiar_guardian"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_HEADLESS_FAMILIAR.get(), "item.occultism.ritual_dummy.familiar_headless"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/familiar_headless")),
                        60,
                        RITUAL_FAMILIAR,
                        PENTACLE_POSSESS_DJINNI,
                        Ingredient.of(Tags.Items.CROPS_WHEAT),
                        Ingredient.of(Tags.Items.CROPS_WHEAT),
                        Ingredient.of(Blocks.HAY_BLOCK),
                        Ingredient.of(Tags.Items.RODS_WOODEN),
                        Ingredient.of(Tags.Items.RODS_WOODEN),
                        Ingredient.of(Blocks.CARVED_PUMPKIN))
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .entityToSummon(OccultismEntities.HEADLESS_FAMILIAR_TYPE.get())
                .entityToSacrifice(OccultismTags.Entities.SNOW_GOLEM)
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.snow_golem")
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/familiar_headless"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_MUMMY_FAMILIAR.get(), "item.occultism.ritual_dummy.familiar_mummy"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/familiar_mummy")),
                        30,
                        RITUAL_FAMILIAR,
                        PENTACLE_POSSESS_DJINNI,
                        Ingredient.of(Tags.Items.SLIME_BALLS),
                        Ingredient.of(Tags.Items.SLIME_BALLS),
                        Ingredient.of(Items.PAPER),
                        Ingredient.of(Items.PAPER),
                        Ingredient.of(ItemTags.WOOL),
                        Ingredient.of(ItemTags.WOOL),
                        Ingredient.of(ItemTags.WOOL),
                        Ingredient.of(ItemTags.WOOL))
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .entityToSummon(OccultismEntities.MUMMY_FAMILIAR_TYPE.get())
                .entityToSacrifice(OccultismTags.Entities.LLAMAS)
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.llamas")
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/familiar_mummy"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_OTHERWORLD_BIRD.get(), "item.occultism.ritual_dummy.familiar_otherworld_bird"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/familiar_otherworld_bird")),
                        30,
                        OccultismRituals.SUMMON_TAMED.getId(),
                        PENTACLE_POSSESS_DJINNI,
                        Ingredient.of(Tags.Items.FEATHERS),
                        Ingredient.of(Tags.Items.FEATHERS),
                        Ingredient.of(Blocks.COBWEB),
                        Ingredient.of(ItemTags.LEAVES),
                        Ingredient.of(Tags.Items.STRINGS))
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .entityToSummon(OccultismEntities.OTHERWORLD_BIRD_TYPE.get())
                .entityToSacrifice(OccultismTags.Entities.PARROTS)
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.parrots")
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/familiar_otherworld_bird"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()),
                        makeLoreSpawnEgg(OccultismItems.SPAWN_EGG_PARROT_FAMILIAR.get(), "item.occultism.ritual_dummy.familiar_parrot"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/familiar_parrot")),
                        30,
                        OccultismRituals.SUMMON_WITH_CHANCE_OF_CHICKEN_TAMED.getId(),
                        PENTACLE_POSSESS_FOLIOT,
                        Ingredient.of(Tags.Items.FEATHERS),
                        Ingredient.of(Tags.Items.DYES_GREEN),
                        Ingredient.of(Tags.Items.DYES_YELLOW),
                        Ingredient.of(Tags.Items.DYES_RED),
                        Ingredient.of(Tags.Items.DYES_BLUE),
                        Ingredient.of(Tags.Items.STRINGS))
                .unlockedBy("has_bound_foliot", has(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()))
                .entityToSummon(EntityType.PARROT)
                .entityToSacrifice(OccultismTags.Entities.CHICKEN)
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.chicken")
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/familiar_parrot"));


    }

    private static void craftingRituals(RecipeOutput recipeOutput) {
        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        new ItemStack(OccultismItems.DIMENSIONAL_MATRIX.get()),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/craft_dimensional_matrix")),
                        240,
                        RITUAL_CRAFT_WITH_SPIRIT_NAME,
                        PENTACLE_CRAFT_DJINNI,
                        Ingredient.of(Items.QUARTZ_BLOCK),
                        Ingredient.of(Items.QUARTZ_BLOCK),
                        Ingredient.of(Items.QUARTZ_BLOCK),
                        Ingredient.of(Tags.Items.ENDER_PEARLS)
                )
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/craft_dimensional_matrix"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        new ItemStack(OccultismBlocks.DIMENSIONAL_MINESHAFT.get()),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/craft_dimensional_mineshaft")),
                        240,
                        RITUAL_CRAFT,
                        PENTACLE_CRAFT_DJINNI,
                        Ingredient.of(OccultismBlocks.OTHERSTONE.get()),
                        Ingredient.of(OccultismBlocks.OTHERSTONE.get()),
                        Ingredient.of(OccultismBlocks.OTHERSTONE.get()),
                        Ingredient.of(OccultismBlocks.OTHERSTONE.get()),
                        Ingredient.of(Tags.Items.INGOTS_GOLD),
                        Ingredient.of(OccultismTags.Items.STORAGE_BLOCK_IESNIUM),
                        Ingredient.of(OccultismBlocks.SPIRIT_ATTUNED_CRYSTAL.get()))
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/craft_dimensional_mineshaft"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        new ItemStack(OccultismItems.FAMILIAR_RING.get()),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/craft_familiar_ring")),
                        90,
                        RITUAL_CRAFT,
                        PENTACLE_CRAFT_DJINNI,
                        Ingredient.of(OccultismItems.SOUL_GEM_ITEM.get()),
                        Ingredient.of(Tags.Items.INGOTS_GOLD),
                        Ingredient.of(Tags.Items.INGOTS_GOLD),
                        Ingredient.of(OccultismTags.Items.SILVER_INGOT),
                        Ingredient.of(OccultismTags.Items.SILVER_INGOT))
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/craft_familiar_ring"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()),
                        new ItemStack(OccultismItems.INFUSED_LENSES.get()),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/craft_infused_lenses")),
                        60,
                        RITUAL_CRAFT,
                        PENTACLE_CRAFT_FOLIOT,
                        Ingredient.of(OccultismItems.LENSES.get()),
                        Ingredient.of(OccultismTags.Items.SILVER_INGOT),
                        Ingredient.of(OccultismTags.Items.SILVER_INGOT),
                        Ingredient.of(Tags.Items.INGOTS_GOLD)
                ).unlockedBy("has_bound_foliot", has(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/craft_infused_lenses"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        new ItemStack(OccultismItems.INFUSED_PICKAXE.get()),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/craft_infused_pickaxe")),
                        60,
                        RITUAL_CRAFT_WITH_SPIRIT_NAME,
                        PENTACLE_CRAFT_DJINNI,
                        Ingredient.of(Tags.Items.RODS_WOODEN),
                        Ingredient.of(Tags.Items.RODS_WOODEN),
                        Ingredient.of(OccultismItems.SPIRIT_ATTUNED_PICKAXE_HEAD.get()),
                        Ingredient.of(OccultismTags.Items.SILVER_INGOT),
                        Ingredient.of(OccultismTags.Items.SILVER_INGOT)
                )
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/craft_infused_pickaxe"));

        minerRecipes(recipeOutput);

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()),
                        new ItemStack(OccultismItems.SATCHEL.get()),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/craft_satchel")),
                        240,
                        RITUAL_CRAFT_WITH_SPIRIT_NAME,
                        PENTACLE_CRAFT_FOLIOT,
                        Ingredient.of(Tags.Items.CHESTS_WOODEN),
                        Ingredient.of(Tags.Items.LEATHERS),
                        Ingredient.of(Tags.Items.LEATHERS),
                        Ingredient.of(Tags.Items.STRINGS),
                        Ingredient.of(OccultismTags.Items.SILVER_INGOT))
                .unlockedBy("has_bound_foliot", has(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/craft_satchel"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        new ItemStack(OccultismItems.RITUAL_SATCHEL_T1.get()),
                        makeRitualDummy(OccultismItems.RITUAL_DUMMY_CRAFT_RITUAL_SATCHEL_T1.get()),
                        240,
                        RITUAL_CRAFT_WITH_SPIRIT_NAME,
                        PENTACLE_CRAFT_DJINNI,
                        Ingredient.of(Items.HOPPER),
                        Ingredient.of(Items.DISPENSER),
                        Ingredient.of(ItemTags.WOOL),
                        Ingredient.of(ItemTags.WOOL),
                        Ingredient.of(Tags.Items.LEATHERS),
                        Ingredient.of(Tags.Items.LEATHERS),
                        Ingredient.of(Tags.Items.STRINGS),
                        Ingredient.of(OccultismTags.Items.SILVER_INGOT))
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/craft_ritual_satchel_t1"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()),
                        new ItemStack(OccultismItems.RITUAL_SATCHEL_T2.get()),
                        makeRitualDummy(OccultismItems.RITUAL_DUMMY_CRAFT_RITUAL_SATCHEL_T2.get()),
                        240,
                        RITUAL_CRAFT_WITH_SPIRIT_NAME,
                        PENTACLE_CRAFT_AFRIT,
                        Ingredient.of(Items.HOPPER),
                        Ingredient.of(Items.DISPENSER),
                        Ingredient.of(ItemTags.WOOL),
                        Ingredient.of(Tags.Items.LEATHERS),
                        Ingredient.of(Tags.Items.STRINGS),
                        Ingredient.of(OccultismTags.Items.SILVER_INGOT),
                        Ingredient.of(OccultismItems.AFRIT_ESSENCE.get()),
                        Ingredient.of(Tags.Items.ENDER_PEARLS),
                        Ingredient.of(Tags.Items.ENDER_PEARLS),
                        Ingredient.of(Tags.Items.ENDER_PEARLS),
                        Ingredient.of(Tags.Items.ENDER_PEARLS)
                )
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/craft_ritual_satchel_t2"));


        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        new ItemStack(OccultismItems.SOUL_GEM_ITEM.get()),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/craft_soul_gem")),
                        60,
                        RITUAL_CRAFT,
                        PENTACLE_CRAFT_DJINNI,
                        Ingredient.of(Tags.Items.GEMS_DIAMOND),
                        Ingredient.of(Tags.Items.INGOTS_COPPER),
                        Ingredient.of(OccultismTags.Items.SILVER_INGOT),
                        Ingredient.of(Tags.Items.INGOTS_GOLD),
                        Ingredient.of(Blocks.SOUL_SAND),
                        Ingredient.of(Blocks.SOUL_SAND),
                        Ingredient.of(Blocks.SOUL_SAND),
                        Ingredient.of(Blocks.SOUL_SAND)
                ).unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/craft_soul_gem"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()),
                        new ItemStack(OccultismBlocks.STORAGE_STABILIZER_TIER1.get()),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/craft_stabilizer_tier1")),
                        120,
                        RITUAL_CRAFT,
                        PENTACLE_CRAFT_FOLIOT,
                        Ingredient.of(OccultismBlocks.OTHERSTONE_PEDESTAL.get()),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_COPPER),
                        Ingredient.of(OccultismTags.Items.BLAZE_DUST),
                        Ingredient.of(OccultismItems.SPIRIT_ATTUNED_GEM.get()))
                .unlockedBy("has_bound_foliot", has(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/craft_stabilizer_tier1"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        new ItemStack(OccultismBlocks.STORAGE_STABILIZER_TIER2.get()),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/craft_stabilizer_tier2")),
                        240,
                        RITUAL_CRAFT,
                        PENTACLE_CRAFT_DJINNI,
                        Ingredient.of(OccultismBlocks.STORAGE_STABILIZER_TIER1.get()),
                        Ingredient.of(OccultismTags.Items.STORAGE_BLOCK_SILVER),
                        Ingredient.of(Items.GHAST_TEAR),
                        Ingredient.of(OccultismItems.SPIRIT_ATTUNED_GEM.get()),
                        Ingredient.of(OccultismItems.SPIRIT_ATTUNED_GEM.get()))
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/craft_stabilizer_tier2"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()),
                        new ItemStack(OccultismBlocks.STORAGE_STABILIZER_TIER3.get()),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/craft_stabilizer_tier3")),
                        240,
                        RITUAL_CRAFT,
                        PENTACLE_CRAFT_AFRIT,
                        Ingredient.of(OccultismBlocks.STORAGE_STABILIZER_TIER2.get()),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_GOLD),
                        Ingredient.of(Items.NETHER_STAR),
                        Ingredient.of(OccultismBlocks.SPIRIT_ATTUNED_CRYSTAL.get()),
                        Ingredient.of(OccultismItems.AFRIT_ESSENCE.get()))
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/craft_stabilizer_tier3"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_MARID.get()),
                        new ItemStack(OccultismBlocks.STORAGE_STABILIZER_TIER4.get()),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/craft_stabilizer_tier4")),
                        240,
                        RITUAL_CRAFT,
                        PENTACLE_CRAFT_MARID,
                        Ingredient.of(OccultismBlocks.STORAGE_STABILIZER_TIER3.get()),
                        Ingredient.of(OccultismTags.Items.STORAGE_BLOCK_IESNIUM),
                        Ingredient.of(Items.DRAGON_HEAD),
                        Ingredient.of(OccultismBlocks.SPIRIT_ATTUNED_CRYSTAL.get()),
                        Ingredient.of(OccultismBlocks.SPIRIT_ATTUNED_CRYSTAL.get()),
                        Ingredient.of(OccultismItems.MARID_ESSENCE.get()))
                .unlockedBy("has_bound_marid", has(OccultismItems.BOOK_OF_BINDING_BOUND_MARID.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/craft_stabilizer_tier4"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()),
                        new ItemStack(OccultismBlocks.STABLE_WORMHOLE.get()),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/craft_stable_wormhole")),
                        120,
                        RITUAL_CRAFT,
                        PENTACLE_CRAFT_FOLIOT,
                        Ingredient.of(OccultismItems.WORMHOLE_FRAME.get()),
                        Ingredient.of(Tags.Items.ENDER_PEARLS),
                        Ingredient.of(Tags.Items.GEMS_QUARTZ),
                        Ingredient.of(Tags.Items.GEMS_QUARTZ))
                .unlockedBy("has_bound_foliot", has(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/craft_stable_wormhole"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()),
                        new ItemStack(OccultismBlocks.STORAGE_CONTROLLER_BASE.get()),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/craft_storage_controller_base")),
                        60,
                        RITUAL_CRAFT,
                        PENTACLE_CRAFT_FOLIOT,
                        Ingredient.of(OccultismBlocks.OTHERSTONE_PEDESTAL.get()),
                        Ingredient.of(Tags.Items.INGOTS_GOLD),
                        Ingredient.of(Tags.Items.INGOTS_GOLD),
                        Ingredient.of(Tags.Items.INGOTS_GOLD))
                .unlockedBy("has_bound_foliot", has(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/craft_storage_controller_base"));
        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        new ItemStack(OccultismItems.STORAGE_REMOTE.get()),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/craft_storage_remote")),
                        120,
                        RITUAL_CRAFT,
                        PENTACLE_CRAFT_DJINNI,
                        Ingredient.of(OccultismItems.STORAGE_REMOTE_INERT.get()),
                        Ingredient.of(Tags.Items.ENDER_PEARLS),
                        Ingredient.of(Tags.Items.ENDER_PEARLS),
                        Ingredient.of(Tags.Items.GEMS_QUARTZ))
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/craft_storage_remote"));
        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(Items.DIAMOND_BLOCK),
                        new ItemStack(Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/misc_wild_trim")),
                        240,
                        RITUAL_CRAFT,
                        PENTACLE_CONTACT_WILD_SPIRIT,
                        Ingredient.of(Items.MOSSY_COBBLESTONE),
                        Ingredient.of(Items.JUNGLE_SAPLING),
                        Ingredient.of(Items.BAMBOO),
                        Ingredient.of(Items.GLISTERING_MELON_SLICE),
                        Ingredient.of(Items.MOSSY_COBBLESTONE),
                        Ingredient.of(Items.JUNGLE_SAPLING),
                        Ingredient.of(Items.BAMBOO),
                        Ingredient.of(Items.GLISTERING_MELON_SLICE),
                        Ingredient.of(Items.MOSSY_COBBLESTONE),
                        Ingredient.of(Items.JUNGLE_SAPLING),
                        Ingredient.of(Items.BAMBOO),
                        Ingredient.of(Items.GLISTERING_MELON_SLICE))
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()))
                .entityToSacrifice(OccultismTags.Entities.OCELOT)
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.ocelot")
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/misc_wild_trim"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()),
                        new ItemStack(OccultismItems.RESEARCH_FRAGMENT_DUST.get()),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/craft_research_fragment_dust")),
                        60,
                        RITUAL_CRAFT,
                        PENTACLE_CRAFT_FOLIOT,
                        Ingredient.of(OccultismTags.Items.EMERALD_DUST),
                        Ingredient.of(Items.EXPERIENCE_BOTTLE),
                        Ingredient.of(Items.EXPERIENCE_BOTTLE))
                .unlockedBy("has_bound_foliot", has(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/craft_research_fragment_dust"));
        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()),
                        new ItemStack(OccultismItems.NATURE_PASTE.get()),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/craft_nature_paste")),
                        30,
                        RITUAL_CRAFT,
                        PENTACLE_CRAFT_FOLIOT,
                        Ingredient.of(ItemTags.LEAVES),
                        Ingredient.of(ItemTags.SAPLINGS),
                        Ingredient.of(Tags.Items.SEEDS),
                        Ingredient.of(ItemTags.LEAVES),
                        Ingredient.of(ItemTags.SAPLINGS),
                        Ingredient.of(Tags.Items.SEEDS),
                        Ingredient.of(ItemTags.LEAVES),
                        Ingredient.of(ItemTags.SAPLINGS),
                        Ingredient.of(Tags.Items.SEEDS))
                .unlockedBy("has_bound_foliot", has(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/craft_nature_paste"));
        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        new ItemStack(OccultismItems.GRAY_PASTE.get()),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/craft_gray_paste")),
                        90,
                        RITUAL_CRAFT,
                        PENTACLE_CRAFT_DJINNI,
                        Ingredient.of(Tags.Items.GUNPOWDERS),
                        Ingredient.of(Items.CLAY_BALL),
                        Ingredient.of(Items.WIND_CHARGE))
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/craft_gray_paste"));
        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()),
                        new ItemStack(OccultismItems.WITHERITE_DUST.get(), 3),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/craft_witherite_dust")),
                        150,
                        RITUAL_CRAFT,
                        PENTACLE_CRAFT_AFRIT,
                        Ingredient.of(OccultismTags.Items.NETHERITE_DUST),
                        Ingredient.of(OccultismTags.Items.BLACKSTONE_DUST),
                        Ingredient.of(OccultismTags.Items.BLACKSTONE_DUST),
                        Ingredient.of(Items.WITHER_ROSE),
                        Ingredient.of(Items.WITHER_ROSE),
                        Ingredient.of(Items.WITHER_ROSE))
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/craft_witherite_dust"));
        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_MARID.get()),
                        new ItemStack(OccultismItems.DRAGONYST_DUST.get()),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/craft_dragonyst_dust")),
                        300,
                        RITUAL_CRAFT,
                        PENTACLE_CRAFT_MARID,
                        Ingredient.of(OccultismTags.Items.AMETHYST_DUST),
                        Ingredient.of(OccultismTags.Items.AMETHYST_DUST),
                        Ingredient.of(OccultismTags.Items.END_STONE_DUST),
                        Ingredient.of(Items.END_CRYSTAL),
                        Ingredient.of(Items.END_CRYSTAL),
                        Ingredient.of(Items.END_CRYSTAL),
                        Ingredient.of(Items.END_CRYSTAL),
                        Ingredient.of(Items.DRAGON_BREATH),
                        Ingredient.of(Items.DRAGON_BREATH),
                        Ingredient.of(Items.DRAGON_BREATH))
                .unlockedBy("has_bound_marid", has(OccultismItems.BOOK_OF_BINDING_BOUND_MARID.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/craft_dragonyst_dust"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(Items.AMETHYST_BLOCK),
                        new ItemStack(Items.BUDDING_AMETHYST),
                        makeRitualDummy(OccultismItems.RITUAL_DUMMY_CRAFT_BUDDING_AMETHYST.get()),
                        180,
                        RITUAL_CRAFT,
                        PENTACLE_CONTACT_WILD_SPIRIT,
                        Ingredient.of(Items.SMALL_AMETHYST_BUD),
                        Ingredient.of(Items.MEDIUM_AMETHYST_BUD),
                        Ingredient.of(Items.LARGE_AMETHYST_BUD),
                        Ingredient.of(Items.AMETHYST_CLUSTER),
                        Ingredient.of(Items.AMETHYST_SHARD),
                        Ingredient.of(OccultismTags.Items.AMETHYST_DUST),
                        Ingredient.of(Items.SMALL_AMETHYST_BUD),
                        Ingredient.of(Items.MEDIUM_AMETHYST_BUD),
                        Ingredient.of(Items.LARGE_AMETHYST_BUD),
                        Ingredient.of(Items.AMETHYST_CLUSTER),
                        Ingredient.of(Items.AMETHYST_SHARD),
                        Ingredient.of(OccultismTags.Items.AMETHYST_DUST))
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()))
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.allay")
                .entityToSacrifice(OccultismTags.Entities.ALLAY)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/misc_budding_amethyst"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(Items.DEEPSLATE),
                        new ItemStack(Items.REINFORCED_DEEPSLATE),
                        makeRitualDummy(OccultismItems.RITUAL_DUMMY_CRAFT_REINFORCED_DEEPSLATE.get()),
                        360,
                        RITUAL_CRAFT,
                        PENTACLE_CONTACT_ELDRITCH_SPIRIT,
                        Ingredient.of(Tags.Items.NETHER_STARS),
                        Ingredient.of(Items.SHULKER_SHELL),
                        Ingredient.of(Items.ARMADILLO_SCUTE),
                        Ingredient.of(Items.TURTLE_SCUTE),
                        Ingredient.of(Items.ECHO_SHARD),
                        Ingredient.of(Items.ANVIL),
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_NETHERITE),
                        Ingredient.of(Tags.Items.OBSIDIANS),
                        Ingredient.of(Tags.Items.OBSIDIANS_CRYING),
                        Ingredient.of(Items.END_STONE_BRICKS),
                        Ingredient.of(Items.SCULK_CATALYST),
                        Ingredient.of(OccultismTags.Items.STORAGE_BLOCK_IESNIUM))
                .unlockedBy("has_bound_marid", has(OccultismItems.BOOK_OF_BINDING_BOUND_MARID.get()))
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.warden")
                .entityToSacrifice(OccultismTags.Entities.WARDEN)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/misc_reinforced_deepslate"));

    }

    private static void minerRecipes(RecipeOutput recipeOutput) {
        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()),
                        new ItemStack(OccultismItems.MINER_AFRIT_DEEPS.get()),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/craft_miner_afrit_deeps")),
                        120,
                        RITUAL_CRAFT_MINER_SPIRIT,
                        PENTACLE_CRAFT_AFRIT,
                        Ingredient.of(OccultismItems.MINER_DJINNI_ORES.get()),
                        Ingredient.of(OccultismItems.IESNIUM_PICKAXE.get()),
                        Ingredient.of(OccultismBlocks.SPIRIT_ATTUNED_CRYSTAL.get()),
                        Ingredient.of(OccultismItems.AFRIT_ESSENCE.get()),
                        Ingredient.of(Items.ECHO_SHARD),
                        Ingredient.of(Blocks.CRYING_OBSIDIAN))
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/craft_miner_afrit_deeps"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()),
                        new ItemStack(OccultismItems.MINER_DJINNI_ORES.get()),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/craft_miner_djinni_ores")),
                        60,
                        RITUAL_CRAFT_MINER_SPIRIT,
                        PENTACLE_CRAFT_DJINNI,
                        Ingredient.of(OccultismItems.MINER_FOLIOT_UNSPECIALIZED.get()),
                        Ingredient.of(OccultismItems.IESNIUM_PICKAXE.get()),
                        Ingredient.of(OccultismBlocks.SPIRIT_ATTUNED_CRYSTAL.get()),
                        Ingredient.of(Tags.Items.INGOTS_GOLD),
                        Ingredient.of(Tags.Items.GEMS_LAPIS),
                        Ingredient.of(OccultismBlocks.SPIRIT_ATTUNED_CRYSTAL.get()))
                .unlockedBy("has_bound_djinni", has(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/craft_miner_djinni_ores"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()),
                        new ItemStack(OccultismItems.MINER_FOLIOT_UNSPECIALIZED.get()),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/craft_miner_foliot_unspecialized")),
                        60,
                        RITUAL_CRAFT_MINER_SPIRIT,
                        PENTACLE_CRAFT_FOLIOT,
                        Ingredient.of(OccultismItems.MAGIC_LAMP_EMPTY.get()),
                        Ingredient.of(OccultismItems.IESNIUM_PICKAXE.get()),
                        Ingredient.of(Tags.Items.RAW_MATERIALS_IRON),
                        Ingredient.of(Tags.Items.GRAVELS))
                .unlockedBy("has_bound_foliot", has(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/craft_miner_foliot_unspecialized"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.BOOK_OF_BINDING_BOUND_MARID.get()),
                        new ItemStack(OccultismItems.MINER_MARID_MASTER.get()),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/craft_miner_marid_master")),
                        120,
                        RITUAL_CRAFT_MINER_SPIRIT,
                        PENTACLE_CRAFT_MARID,
                        Ingredient.of(OccultismItems.MINER_AFRIT_DEEPS.get()),
                        Ingredient.of(OccultismItems.IESNIUM_PICKAXE.get()),
                        Ingredient.of(OccultismBlocks.SPIRIT_ATTUNED_CRYSTAL.get()),
                        Ingredient.of(Items.NETHERITE_PICKAXE),
                        Ingredient.of(Items.DRAGON_BREATH),
                        Ingredient.of(Items.TOTEM_OF_UNDYING),
                        Ingredient.of(Items.NETHER_STAR),
                        Ingredient.of(OccultismItems.MARID_ESSENCE.get()))
                .unlockedBy("has_bound_marid", has(OccultismItems.BOOK_OF_BINDING_BOUND_MARID.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/craft_miner_marid_master"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.MINING_DIMENSION_CORE_PIECE.get()),
                        new ItemStack(OccultismItems.MINER_ANCIENT_ELDRITCH.get()),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/craft_miner_ancient_eldritch")),
                        360,
                        RITUAL_CRAFT_MINER_SPIRIT,
                        PENTACLE_CONTACT_ELDRITCH_SPIRIT,
                        Ingredient.of(OccultismItems.MINER_MARID_MASTER.get()),
                        Ingredient.of(OccultismItems.MINER_MARID_MASTER.get()),
                        Ingredient.of(OccultismItems.MINER_MARID_MASTER.get()),
                        Ingredient.of(OccultismItems.MINER_MARID_MASTER.get()),
                        Ingredient.of(OccultismItems.MINER_MARID_MASTER.get()),
                        Ingredient.of(OccultismItems.MINER_MARID_MASTER.get()),
                        Ingredient.of(OccultismItems.MINER_MARID_MASTER.get()),
                        Ingredient.of(OccultismItems.MINER_MARID_MASTER.get()))
                .unlockedBy("has_mining_dimension_core", has(OccultismItems.MINING_DIMENSION_CORE_PIECE.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/craft_miner_ancient_eldritch"));
    }
    private static void resurrectRituals(RecipeOutput recipeOutput) {
        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismItems.SOUL_SHARD_ITEM.get()),
                        makeLoreSpawnEgg(OccultismItems.RESURRECT_ICON.get(), "item.occultism.ritual_dummy.resurrect_familiar"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/resurrect_familiar")),
                        15,
                        RITUAL_RESURRECT_FAMILIAR,
                        PENTACLE_RESURRECT_SPIRIT,
                        Ingredient.of(OccultismItems.OTHERWORLD_ESSENCE.get()),
                        Ingredient.of(OccultismItems.OTHERWORLD_ESSENCE.get()),
                        Ingredient.of(OccultismItems.OTHERWORLD_ESSENCE.get()),
                        Ingredient.of(OccultismItems.OTHERWORLD_ESSENCE.get()))
                .unlockedBy("has_otherworld_essence", has(OccultismItems.OTHERWORLD_ESSENCE.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/resurrect_familiar"));

        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(Items.SUGAR),
                        makeLoreSpawnEgg(Items.ALLAY_SPAWN_EGG, "item.occultism.ritual_dummy.resurrect_allay"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual_dummy/resurrect_allay")),
                        30,
                        RITUAL_SUMMON,
                        PENTACLE_RESURRECT_SPIRIT,
                        Ingredient.of(Tags.Items.DUSTS_REDSTONE),
                        Ingredient.of(Tags.Items.DUSTS_GLOWSTONE),
                        Ingredient.of(OccultismTags.Items.SILVER_DUST),
                        Ingredient.of(OccultismTags.Items.GOLD_DUST))
                .unlockedBy("has_bound_foliot", has(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT.get()))
                .entityToSummon(EntityType.ALLAY)
                .entityToSacrificeDisplayName("ritual.occultism.sacrifice.vex")
                .entityToSacrifice(OccultismTags.Entities.VEX)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID, "ritual/resurrect_allay"));
    }
    private static void repairRituals(RecipeOutput recipeOutput) {
        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismTags.Items.TOOLS_CHALK),
                    makeLoreSpawnEgg(OccultismItems.REPAIR_ICON.get(), "item.occultism.ritual_dummy.repair_chalks"),
                    makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID,"ritual_dummy/repair_chalks")),
                    5,
                    RITUAL_REPAIR,
                    PENTACLE_CRAFT_DJINNI,
                    Ingredient.of(OccultismTags.Items.COPPER_DUST),
                    Ingredient.of(OccultismTags.Items.OTHERWORLD_WOOD_DUST),
                    Ingredient.of(OccultismTags.Items.OTHERSTONE_DUST))
                .unlockedBy("has_white_chalk", has(OccultismItems.CHALK_WHITE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID,"ritual/repair_chalks"));
        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(Tags.Items.TOOLS),
                        makeLoreSpawnEgg(OccultismItems.REPAIR_ICON.get(), "item.occultism.ritual_dummy.repair_tools"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID,"ritual_dummy/repair_tools")),
                        60,
                        RITUAL_REPAIR,
                        PENTACLE_CRAFT_AFRIT,
                        Ingredient.of(OccultismTags.Items.IESNIUM_INGOT),
                        Ingredient.of(OccultismTags.Items.IESNIUM_INGOT),
                        Ingredient.of(OccultismItems.SPIRIT_ATTUNED_GEM))
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID,"ritual/repair_tools"));
        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(Tags.Items.ARMORS),
                        makeLoreSpawnEgg(OccultismItems.REPAIR_ICON.get(), "item.occultism.ritual_dummy.repair_armors"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID,"ritual_dummy/repair_armors")),
                        60,
                        RITUAL_REPAIR,
                        PENTACLE_CRAFT_AFRIT,
                        Ingredient.of(OccultismTags.Items.IESNIUM_INGOT),
                        Ingredient.of(OccultismTags.Items.IESNIUM_INGOT),
                        Ingredient.of(OccultismTags.Items.SCUTESHELL),
                        Ingredient.of(OccultismItems.SPIRIT_ATTUNED_GEM))
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID,"ritual/repair_armors"));
        RitualRecipeBuilder.ritualRecipeBuilder(Ingredient.of(OccultismTags.Items.Miners.MINERS),
                        makeLoreSpawnEgg(OccultismItems.REPAIR_ICON.get(), "item.occultism.ritual_dummy.repair_miners"),
                        makeRitualDummy(ResourceLocation.fromNamespaceAndPath(Occultism.MODID,"ritual_dummy/repair_miners")),
                        60,
                        RITUAL_REPAIR,
                        PENTACLE_CRAFT_AFRIT,
                        Ingredient.of(OccultismTags.Items.STORAGE_BLOCK_IESNIUM),
                        Ingredient.of(OccultismBlocks.SPIRIT_ATTUNED_CRYSTAL),
                        Ingredient.of(OccultismItems.OTHERWORLD_ESSENCE))
                .unlockedBy("has_bound_afrit", has(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Occultism.MODID,"ritual/repair_miners"));
    }
}
