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

package com.github.klikli_dev.occultism.common.entity;

import com.github.klikli_dev.occultism.Occultism;
import com.github.klikli_dev.occultism.common.capability.FamiliarSettingsCapability;
import com.github.klikli_dev.occultism.registry.OccultismCapabilities;
import com.github.klikli_dev.occultism.registry.OccultismEffects;
import com.github.klikli_dev.occultism.registry.OccultismItems;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.InteractionHand;
import net.minecraft.util.InteractionResult;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.UUID;

public class OtherworldBirdEntity extends ParrotEntity implements IFamiliar {

    // region Fields
    public static final float MAX_BOOST_DISTANCE = 8f;
    public LivingEntity ownerCached;

    public SitGoal sitGoal;
    // endregion Fields

    // region Initialization
    public OtherworldBirdEntity(EntityType<? extends ParrotEntity> type, Level worldIn) {
        super(type, worldIn);
    }
    // endregion Initialization

    // region Getter / Setter
    public LivingEntity getOwnerCached() {
        if (this.ownerCached != null)
            return this.ownerCached;
        this.ownerCached = this.getOwner();
        return this.ownerCached;
    }
    // endregion Getter / Setter

    // region Overrides

    @Override
    protected void registerGoals() {
        // same as parrot, except we don't land on shoulders.
        this.sitGoal = new SitGoal(this);
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(2, this.sitGoal);
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.0D, 5.0F, 1.0F, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new FollowMobGoal(this, 1.0D, 3.0F, 7.0F));
    }

    @Override
    public void aiStep() {
        // Every 10 ticks, attempt to refresh the owner buff
        if (!this.level.isClientSide && this.level.getGameTime() % 10 == 0 && this.isTamed()) {
            LivingEntity owner = this.getOwnerCached();
            if (owner != null && this.getDistance(owner) < MAX_BOOST_DISTANCE) {
                // close enough to boost
                for (EffectInstance effect : this.getFamiliarEffects())
                    owner.addPotionEffect(effect);
            }
        }

        super.aiStep();
    }

    @Override
    public void setOwnerId(@Nullable UUID ownerId) {
        this.ownerCached = null;
        super.setOwnerId(ownerId);
    }

    @Override
    public LivingEntity getFamiliarOwner() {
        return this.getOwnerCached();
    }

    @Override
    public void setFamiliarOwner(LivingEntity owner) {
        this.setOwnerId(owner.getUniqueID());
    }

    @Override
    public Entity getEntity() {
        return this;
    }

    @Override
    public Iterable<EffectInstance> getFamiliarEffects() {

        //only provide effect if enabled
        if (this.getFamiliarOwner().getCapability(OccultismCapabilities.FAMILIAR_SETTINGS)
                .map(FamiliarSettingsCapability::isOtherworldBirdEnabled).orElse(false)) {

            return ImmutableList.of(new EffectInstance(Effects.JUMP_BOOST, 60, 5, false, false),
                    new EffectInstance(Effects.SLOW_FALLING,
                            20 * Occultism.SERVER_CONFIG.spiritJobs.drikwingFamiliarSlowFallingSeconds.get(), 0, false,
                            false),
                    new EffectInstance(OccultismEffects.DOUBLE_JUMP.get(), 120, 4, false, false));

        }
        return Collections.emptyList();
    }

    @Override
    public InteractionResult getEntityInteractionResult(Player playerIn, InteractionHand hand) {
        ItemStack stack = playerIn.getHeldItem(hand);
        if (stack.getItem() == OccultismItems.FAMILIAR_RING.get()) {
            return stack.interactWithEntity(playerIn, this, hand);
        }
        return super.getEntityInteractionResult(playerIn, hand);
    }
    // endregion Overrides

    //region Static Methods
    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return ParrotEntity.func_234213_eS_(); // =registerAttributes
    }
//endregion Static Methods
}
