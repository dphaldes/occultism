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

package com.github.klikli_dev.occultism.common.ritual;

import com.github.klikli_dev.occultism.Occultism;
import com.github.klikli_dev.occultism.common.entity.spirit.SpiritEntity;
import com.github.klikli_dev.occultism.common.ritual.pentacle.Pentacle;
import com.github.klikli_dev.occultism.common.tile.GoldenSacrificialBowlBlockEntity;
import com.github.klikli_dev.occultism.common.tile.SacrificialBowlBlockEntity;
import com.github.klikli_dev.occultism.registry.OccultismAdvancements;
import com.github.klikli_dev.occultism.registry.OccultismSounds;
import net.minecraft.BlockEntity.BlockEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class Ritual extends ForgeRegistryEntry<Ritual> {

    //region Fields

    /**
     * The default range to look for sacrificial bowls with additional ingredients
     */
    public static final int SACRIFICIAL_BOWL_RANGE = 8;

    /**
     * The default range to listen for sacrifices.
     */
    public static final int SACRIFICE_DETECTION_RANGE = 8;

    /**
     * The default range to listen for sacrifices.
     */
    public static final int ITEM_USE_DETECTION_RANGE = 16;

    /**
     * The pentacle required to perform this ritual.
     */
    public final Pentacle pentacle;

    /**
     * The item required to start the ritual.
     */
    public final Ingredient startingItem;

    /**
     * The SpiritTrade recipe id representing the additional ingredients for this ritual.
     */
    public ResourceLocation additionalIngredientsRecipeId;
    /**
     * The predicate to check sacrifices against.
     */
    public Predicate<LivingEntity> sacrificePredicate;
    /**
     * The predicate to check sacrifices against.
     */
    public Predicate<PlayerInteractEvent.RightClickItem> itemUsePredicate;
    /**
     * The range to look for sacrificial bowls for additional ingredients.
     */
    public int sacrificialBowlRange;
    /**
     * The total time in seconds it takes to finish the ritual.
     */
    public int totalSeconds;
    /**
     * The ritual time to pass per ingredient.
     */
    public float timePerIngredient;
    /**
     * The additional ingredients required to finish the ritual.
     * These ingredients need to be placed within the ritual area.
     */
    protected List<Ingredient> additionalIngredients;
    protected boolean additionalIngredientsLoaded;
    //endregion Fields


    //region Initialization

    /**
     * Constructs a ritual.
     *
     * @param pentacle     the pentacle for the ritual.
     * @param startingItem the item required to start the ritual.
     */
    public Ritual(Pentacle pentacle, Ingredient startingItem) {
        this(pentacle, startingItem, 10);
    }


    /**
     * Constructs a ritual.
     *
     * @param pentacle     the pentacle for the ritual.
     * @param startingItem the item required to start the ritual.
     * @param totalSeconds the total time it takes to finish the ritual.
     */
    public Ritual(Pentacle pentacle, Ingredient startingItem, int totalSeconds) {
        this(pentacle, startingItem, (String) null, totalSeconds);
    }

    /**
     * Constructs a ritual.
     *
     * @param pentacle                        the pentacle for the ritual.
     * @param startingItem                    the item required to start the ritual.
     * @param additionalIngredientsRecipeName the name of the additional ingredients recipe id. Will be prefixed with MODID:rituals/
     * @param totalSeconds                    the total time it takes to finish the ritual.
     */
    public Ritual(Pentacle pentacle, Ingredient startingItem, String additionalIngredientsRecipeName,
                  int totalSeconds) {
        this(pentacle, startingItem, additionalIngredientsRecipeName, SACRIFICIAL_BOWL_RANGE, totalSeconds);
    }

    /**
     * Constructs a ritual.
     *
     * @param pentacle                        the pentacle for the ritual.
     * @param startingItem                    the item required to start the ritual.
     * @param additionalIngredientsRecipeName the name of the additional ingredients recipe id. Will be prefixed with MODID:rituals/
     * @param sacrificialBowlRange            the range to look for sacrificial bowls for additional ingredients.
     * @param totalSeconds                    the total time it takes to finish the ritual.
     */
    public Ritual(Pentacle pentacle, Ingredient startingItem, String additionalIngredientsRecipeName,
                  int sacrificialBowlRange, int totalSeconds) {
        this.pentacle = pentacle;
        this.startingItem = startingItem;
        if (additionalIngredientsRecipeName != null)
            this.additionalIngredientsRecipeId = new ResourceLocation(Occultism.MODID,
                    "ritual/" + additionalIngredientsRecipeName);
        this.additionalIngredients = new ArrayList<>();
        this.additionalIngredientsLoaded = false;
        this.sacrificialBowlRange = sacrificialBowlRange;
        this.totalSeconds = totalSeconds;
        this.timePerIngredient = this.totalSeconds;
    }
    //endregion Initialization

    //region Getter / Setter

    /**
     * @return the conditions message translation key for this ritual.
     */
    public String getConditionsMessage() {
        return String.format("ritual.%s.conditions", this.getRegistryName().toString().replace(":", "."));
    }

    /**
     * @return the started message translation key for this ritual.
     */
    public String getStartedMessage() {
        return String.format("ritual.%s.started", this.getRegistryName().toString().replace(":", "."));
    }

    /**
     * @return the interrupted message translation key for this ritual.
     */
    public String getInterruptedMessage() {
        return String.format("ritual.%s.interrupted", this.getRegistryName().toString().replace(":", "."));
    }

    /**
     * @return the finished message translation key for this ritual.
     */
    public String getFinishedMessage() {
        return String.format("ritual.%s.finished", this.getRegistryName().toString().replace(":", "."));
    }
    //endregion Getter / Setter

    //region Methods

    /**
     * The additional ingredients required to finish the ritual.
     * These ingredients need to be placed within the ritual area.
     */
    public List<Ingredient> getAdditionalIngredients(Level level) {
        //rituals persist between level unload/reloads, so additionalIngredientsLoaded will be true
        //therefore we also check for additional ingredients size
        if (!this.additionalIngredientsLoaded || this.additionalIngredients.size() == 0) {
            //this is lazily loading the ingredients.
            this.registerAdditionalIngredients(level.getRecipeManager());
        }
        return this.additionalIngredients;
    }

    /**
     * Gets the additional ingredients from the recipe registry.
     */
    public void registerAdditionalIngredients(RecipeManager recipeManager) {
        this.additionalIngredientsLoaded = recipeManager != null;
        if (this.additionalIngredientsRecipeId != null && recipeManager != null) {
            Optional<? extends IRecipe<?>> recipe = recipeManager.getRecipe(this.additionalIngredientsRecipeId);
            if (recipe.isPresent()) {
                this.additionalIngredients = recipe.get().getIngredients();
                //if we have multiple ingredients, make sure
                this.timePerIngredient = this.totalSeconds / (float) (this.additionalIngredients.size() + 1);
            }
            else {
                Occultism.LOGGER.warn("Additional Ingredients Recipe {} not found for Ritual {}",
                        this.additionalIngredientsRecipeId, this.getRegistryName());
            }
        }
    }

    /**
     * Checks whether the ritual is valid.
     * Validity is required to start and continue a ritual.
     *
     * @param level              the level.
     * @param goldenBowlPosition the position of the golden bowl.
     * @param BlockEntity         the tile entity controlling the ritual.
     * @param castingPlayer      the player starting the ritual.
     * @param activationItem     the item used to start the ritual.
     * @return true if a valid ritual is found.
     */
    public boolean isValid(Level level, BlockPos goldenBowlPosition, GoldenSacrificialBowlBlockEntity BlockEntity,
                           Player castingPlayer, ItemStack activationItem,
                           List<Ingredient> remainingAdditionalIngredients) {
        return this.startingItem.test(activationItem) &&
               this.areAdditionalIngredientsFulfilled(level, goldenBowlPosition, remainingAdditionalIngredients) &&
               this.pentacle.getBlockMatcher().validate(level, goldenBowlPosition) != null;
    }

    /**
     * Called when starting the ritual.
     *
     * @param level              the level.
     * @param goldenBowlPosition the position of the golden bowl.
     * @param BlockEntity         the tile entity controlling the ritual.
     * @param castingPlayer      the player starting the ritual.
     * @param activationItem     the item used to start the ritual.
     */
    public void start(Level level, BlockPos goldenBowlPosition, GoldenSacrificialBowlBlockEntity BlockEntity,
                      Player castingPlayer, ItemStack activationItem) {
        level.playSound(null, goldenBowlPosition, OccultismSounds.START_RITUAL.get(), SoundSource.BLOCKS, 1, 1);
        castingPlayer.sendStatusMessage(new TranslationTextComponent(this.getStartedMessage()), true);
    }

    /**
     * Called when finishing the ritual.
     *
     * @param level              the level.
     * @param goldenBowlPosition the position of the golden bowl.
     * @param BlockEntity         the tile entity controlling the ritual.
     * @param castingPlayer      the player starting the ritual.
     * @param activationItem     the item used to start the ritual.
     */
    public void finish(Level level, BlockPos goldenBowlPosition, GoldenSacrificialBowlBlockEntity BlockEntity,
                       Player castingPlayer, ItemStack activationItem) {
        level.playSound(null, goldenBowlPosition, OccultismSounds.POOF.get(), SoundSource.BLOCKS, 0.7f,
                0.7f);
        castingPlayer.sendStatusMessage(new TranslationTextComponent(this.getFinishedMessage()), true);
        OccultismAdvancements.RITUAL.trigger((ServerPlayer) castingPlayer, this);
    }

    /**
     * Called when interrupting the ritual.
     *
     * @param level              the level.
     * @param goldenBowlPosition the position of the golden bowl.
     * @param BlockEntity         the tile entity controlling the ritual.
     * @param castingPlayer      the player starting the ritual.
     * @param activationItem     the item used to start the ritual.
     */
    public void interrupt(Level level, BlockPos goldenBowlPosition, GoldenSacrificialBowlBlockEntity BlockEntity,
                          Player castingPlayer, ItemStack activationItem) {
        level.playSound(null, goldenBowlPosition, SoundEvents.ENTITY_CHICKEN_EGG, SoundSource.BLOCKS, 0.7f, 0.7f);
        castingPlayer.sendStatusMessage(new TranslationTextComponent(this.getInterruptedMessage()), true);
    }

    /**
     * Called when interrupting the ritual.
     *
     * @param level                          the level.
     * @param goldenBowlPosition             the position of the golden bowl.
     * @param BlockEntity                     the tile entity controlling the ritual.
     * @param castingPlayer                  the player starting the ritual.
     * @param activationItem                 the item used to start the ritual.
     * @param remainingAdditionalIngredients the additional ingredients not yet fulfilled.
     * @param time                           the current ritual time.
     */
    public void update(Level level, BlockPos goldenBowlPosition, GoldenSacrificialBowlBlockEntity BlockEntity,
                       Player castingPlayer, ItemStack activationItem,
                       List<Ingredient> remainingAdditionalIngredients, int time) {
    }

    /**
     * Called when interrupting the ritual.
     *
     * @param level              the level.
     * @param goldenBowlPosition the position of the golden bowl.
     * @param BlockEntity         the tile entity controlling the ritual.
     * @param castingPlayer      the player starting the ritual.
     * @param activationItem     the item used to start the ritual.
     * @param time               the current ritual time.
     */
    public void update(Level level, BlockPos goldenBowlPosition, GoldenSacrificialBowlBlockEntity BlockEntity,
                       Player castingPlayer, ItemStack activationItem, int time) {
        this.update(level, goldenBowlPosition, BlockEntity, castingPlayer, activationItem, new ArrayList<Ingredient>(),
                time);
    }

    /**
     * Identifies the ritual by it's activation item and pentacle level shape.
     *
     * @param level              the level.
     * @param goldenBowlPosition the position of the golden bowl.
     * @param activationItem     the item used to start the ritual.
     * @return true if the ritual matches, false otherwise.
     */
    public boolean identify(Level level, BlockPos goldenBowlPosition, ItemStack activationItem) {
        return this.startingItem.test(activationItem) &&
               this.areAdditionalIngredientsFulfilled(level, goldenBowlPosition,
                       this.getAdditionalIngredients(level)) &&
               this.pentacle.getBlockMatcher().validate(level, goldenBowlPosition) != null;
    }

    /**
     * Consumes additional ingredients from sacrificial bowls depending on the time passed.
     *
     * @param level                          the level.
     * @param goldenBowlPosition             the position of the golden bowl.
     * @param remainingAdditionalIngredients the remaining additional ingredients. Will be modified if something was consumed!
     * @param time                           the current ritual time.
     * @param consumedIngredients            the list of already consumed ingredients, newly consumd ingredients will be appended
     * @return true if ingredients were consumed successfully, or none needed to be consumed.
     */
    public boolean consumeAdditionalIngredients(Level level, BlockPos goldenBowlPosition,
                                                List<Ingredient> remainingAdditionalIngredients, int time,
                                                List<ItemStack> consumedIngredients) {
        if (remainingAdditionalIngredients.isEmpty())
            return true;

        int totalIngredientsToConsume = (int) Math.floor(time / this.timePerIngredient);
        int ingredientsConsumed = consumedIngredients.size();

        int ingredientsToConsume = totalIngredientsToConsume - ingredientsConsumed;
        if (ingredientsToConsume == 0)
            return true;

        List<SacrificialBowlBlockEntity> sacrificialBowls = this.getSacrificialBowls(level, goldenBowlPosition);
        int consumed = 0;
        for (Iterator<Ingredient> it = remainingAdditionalIngredients.iterator();
             it.hasNext() && consumed < ingredientsToConsume; consumed++) {
            Ingredient ingredient = it.next();
            if (this.consumeAdditionalIngredient(level, goldenBowlPosition, sacrificialBowls, ingredient,
                    consumedIngredients)) {
                //remove from the remaining required ingredients
                it.remove();
            }
            else {
                //if ingredient not found, return false to enable interrupting the ritual.
                return false;
            }
        }
        return true;
    }

    /**
     * Consumes one ingredient from the first matching sacrificial bowl.
     *
     * @param level               the level.
     * @param goldenBowlPosition  the position of the golden bowl.
     * @param sacrificialBowls    the list of sacrificial bowls to check.
     * @param ingredient          the ingredient to consume.
     * @param consumedIngredients the list of already consumed ingredients, newly consumd ingredients will be appended
     * @return true if the ingredient was found and consumed.
     */
    public boolean consumeAdditionalIngredient(Level level, BlockPos goldenBowlPosition,
                                               List<SacrificialBowlBlockEntity> sacrificialBowls,
                                               Ingredient ingredient, List<ItemStack> consumedIngredients) {
        for (SacrificialBowlBlockEntity sacrificialBowl : sacrificialBowls) {
            //first simulate removal to check the ingredient
            if (sacrificialBowl.itemStackHandler.map(handler -> {
                ItemStack stack = handler.extractItem(0, 1, true);
                if (ingredient.test(stack)) {
                    //now take for real
                    ItemStack extracted = handler.extractItem(0, 1, false);
                    consumedIngredients.add(extracted);
                    //Show effect in level
                    ((ServerLevel) level)
                            .sendParticles(ParticleTypes.LARGE_SMOKE, sacrificialBowl.getPos().getX() + 0.5,
                                    sacrificialBowl.getPos().getY() + 1.5, sacrificialBowl.getPos().getZ() + 0.5, 1,
                                    0.0, 0.0, 0.0,
                                    0.0);

                    level.playSound(null, sacrificialBowl.getPos(), OccultismSounds.POOF.get(), SoundSource.BLOCKS,
                            0.7f, 0.7f);
                    return true;
                }
                return false;
            }).orElse(false))
                return true;

        }
        return false;
    }

    /**
     * Removes all matching consumed already consumed ingredients from the remaining additional ingredients.
     * @param additionalIngredients the total additional ingredients required.
     * @param consumedIngredients the already consumed ingredients.
     * @return the remaining additional ingredients that still need to be consumed.
     */
    public static List<Ingredient> getRemainingAdditionalIngredients( List<Ingredient> additionalIngredients, List<ItemStack> consumedIngredients){
        //copy the consumed ingredients to not modify the input
        List<ItemStack> consumedIngredientsCopy = new ArrayList<>(consumedIngredients);
        List<Ingredient> remainingAdditionalIngredients = new ArrayList<>();
        for(Ingredient ingredient : additionalIngredients){
            Optional<ItemStack> matchedStack = consumedIngredientsCopy.stream().filter(ingredient::test).findFirst();
            if(matchedStack.isPresent()){
                //if it is in the consumed ingredients, we do not need to add it to the remaining required ones
                //but we remove it from our consumed ingredients copy so each provided ingredient an only be simulated consumed once
                consumedIngredientsCopy.remove(matchedStack.get());
            } else {
                //if it is not already consumed, we add it to the remaining additional ingredients.
                remainingAdditionalIngredients.add(ingredient);
            }
        }
        return remainingAdditionalIngredients;
    }

    /**
     * Compares the items on sacrificial bowls in range to the additional ingredients.
     *
     * @param level              the level.
     * @param goldenBowlPosition the position of the golden bowl.
     * @return true if additional ingredients are fulfilled.
     */
    public boolean areAdditionalIngredientsFulfilled(Level level, BlockPos goldenBowlPosition,
                                                     List<Ingredient> additionalIngredients) {
        return this.matchesAdditionalIngredients(additionalIngredients,
                this.getItemsOnSacrificialBowls(level, goldenBowlPosition));
    }

    /**
     * Checks if the given stack of items matches the additional ingredients for this ritual.
     *
     * @param items the item stacks to check
     * @return true if the additional ingredients are fulfilled.
     */
    public boolean matchesAdditionalIngredients(List<Ingredient> additionalIngredients, List<ItemStack> items) {

        //optional performance improvement to speed up matching at the cost of convenience
        if (Occultism.SERVER_CONFIG.rituals.enableRemainingIngredientCountMatching.get() &&
            additionalIngredients.size() != items.size())
            return false; //if we have different sizes, it cannot be right

        if (additionalIngredients.isEmpty())
            return true; //implies both are empty, so nothing to check.

        //create a copy to avoid modifying the original
        List<ItemStack> remainingItems = new ArrayList<>(items);

        for (Ingredient ingredient : additionalIngredients) {
            boolean isMatched = false;
            //go through the remaining items
            for (int i = 0; i < remainingItems.size(); i++) {
                ItemStack stack = remainingItems.get(i);
                //check if ingredient matches
                if (ingredient.test(stack)) {
                    isMatched = true;
                    remainingItems.remove(i); //prevent double dipping :)
                    break;
                }
            }
            if (!isMatched)
                return false;
        }

        //more items need to cause failure, otherwise we cannot properly identify the type of ritual.
        //return remainingItems.size() == 0;
        return true;
    }

    /**
     * Gets all items on sacrificial bowls in range of the golden bowl.
     *
     * @param level              the level.
     * @param goldenBowlPosition the ritual golden bowl.
     * @return a list of items on sacrificial bowls in range.
     */
    public List<ItemStack> getItemsOnSacrificialBowls(Level level, BlockPos goldenBowlPosition) {
        List<ItemStack> result = new ArrayList<>();

        List<SacrificialBowlBlockEntity> sacrificialBowls = this.getSacrificialBowls(level, goldenBowlPosition);
        for (SacrificialBowlBlockEntity sacrificialBowl : sacrificialBowls) {
            sacrificialBowl.itemStackHandler.ifPresent(handler -> {
                ItemStack stack = handler.getStackInSlot(0);
                if (!stack.isEmpty()) {
                    result.add(stack);
                }
            });
        }

        return result;
    }

    /**
     * Gets all sacrificial bowls in range of this ritual's golden bowl.
     *
     * @param level              the level.
     * @param goldenBowlPosition the block position of the golden bowl.
     * @return a list of sacrificial bowls.
     */
    public List<SacrificialBowlBlockEntity> getSacrificialBowls(Level level, BlockPos goldenBowlPosition) {
        List<SacrificialBowlBlockEntity> result = new ArrayList<>();
        Iterable<BlockPos> blocksToCheck = BlockPos.getAllInBoxMutable(
                goldenBowlPosition.add(-this.sacrificialBowlRange, 0, -this.sacrificialBowlRange),
                goldenBowlPosition.add(this.sacrificialBowlRange, 0, this.sacrificialBowlRange));
        for (BlockPos blockToCheck : blocksToCheck) {
            BlockEntity blockEntity = level.getBlockEntity(blockToCheck);
            if (BlockEntity instanceof SacrificialBowlBlockEntity &&
                !(BlockEntity instanceof GoldenSacrificialBowlBlockEntity)) {
                result.add((SacrificialBowlBlockEntity) BlockEntity);
            }
        }
        return result;
    }

    /**
     * Prepares the given spirit for spawning by
     *  - initializing it
     *  - setting the taming player
     *  - preparing position and rotation
     *  - setting the custom name.
     *
     * @param spirit             the spirit to prepare.
     * @param level              the level to spawn in.
     * @param goldenBowlPosition the golden bowl position.
     * @param castingPlayer      the ritual casting player.
     * @param spiritName         the spirit name.
     */
    public void prepareSpiritForSpawn(SpiritEntity spirit, Level level, BlockPos goldenBowlPosition,
                                      Player castingPlayer, String spiritName) {
        this.prepareSpiritForSpawn(spirit, level, goldenBowlPosition, castingPlayer, spiritName, true);
    }

    /**
     * Prepares the given spirit for spawning by
     *  - initializing it
     *  - optionally setting the taming player
     *  - preparing position and rotation
     *  - setting the custom name.
     *
     * @param spirit             the spirit to prepare.
     * @param level              the level to spawn in.
     * @param goldenBowlPosition the golden bowl position.
     * @param castingPlayer      the ritual casting player.
     * @param spiritName         the spirit name.
     * @param setTamed           true to tame the spirit
     */
    public void prepareSpiritForSpawn(SpiritEntity spirit, Level level, BlockPos goldenBowlPosition,
                                      Player castingPlayer, String spiritName, boolean setTamed) {
        if(setTamed){
            spirit.setTamedBy(castingPlayer);
        }
        spirit.setPositionAndRotation(goldenBowlPosition.getX(), goldenBowlPosition.getY(), goldenBowlPosition.getZ(),
                level.rand.nextInt(360), 0);
        spirit.setCustomName(new TextComponent(spiritName));
        spirit.onInitialSpawn((ServerLevel) level, level.getDifficultyForLocation(goldenBowlPosition),
                SpawnReason.MOB_SUMMONED, null,
                null);
    }

    /**
     * Checks if the given entity is a valid sacrifice.
     *
     * @param entity the entity to check against.
     * @return true if the entity is a valid sacrifice.
     */
    public boolean isValidSacrifice(LivingEntity entity) {
        if (this.sacrificePredicate == null)
            return false;

        return this.sacrificePredicate.test(entity);
    }

    /**
     * Checks if the given item use event is valid for this ritual.
     *
     * @param event the event to check.
     * @return true if the event represents a valid item use.
     */
    public boolean isValidItemUse(PlayerInteractEvent.RightClickItem event) {
        if (this.itemUsePredicate == null)
            return false;

        return this.itemUsePredicate.test(event);
    }

    /**
     * Gets whether this ritual needs a sacrifice to progress.
     *
     * @return true if a sacrifice is required.
     */
    public boolean requiresSacrifice() {
        return this.sacrificePredicate != null;
    }

    /**
     * Gets whether this ritual needs an item use to progress.
     *
     * @return true if an item use is required.
     */
    public boolean requiresItemUse() {
        return this.itemUsePredicate != null;
    }

    /**
     * Drops the given result stack near the golden bowl.
     *
     * @param level              the level.
     * @param goldenBowlPosition the position of the golden bowl.
     * @param BlockEntity         the tile entity controlling the ritual.
     * @param castingPlayer      the player starting the ritual.
     * @param stack              the result stack to drop.
     */
    public void dropResult(Level level, BlockPos goldenBowlPosition, GoldenSacrificialBowlBlockEntity BlockEntity,
                           Player castingPlayer,  ItemStack stack){
        double angle = level.rand.nextDouble() * Math.PI * 2;
        ItemEntity entity = new ItemEntity(level, goldenBowlPosition.getX() + 0.5, goldenBowlPosition.getY() + 0.75,
                goldenBowlPosition.getZ() + 0.5, stack);
        entity.setMotion(Math.sin(angle) * 0.125, 0.25, Math.cos(angle) * 0.125);
        entity.setPickupDelay(10);
        level.addEntity(entity);
    }
    //endregion Methods
}
