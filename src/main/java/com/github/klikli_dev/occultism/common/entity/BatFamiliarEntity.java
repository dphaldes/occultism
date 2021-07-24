/*
 * MIT License
 *
 * Copyright 2021 vemerion
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

import com.github.klikli_dev.occultism.common.capability.FamiliarSettingsCapability;
import com.github.klikli_dev.occultism.registry.OccultismCapabilities;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.core.BlockPos;
import net.minecraft.util.math.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.EnumSet;

public class BatFamiliarEntity extends FamiliarEntity implements IFlyingAnimal {

    public BatFamiliarEntity(EntityType<? extends BatFamiliarEntity> type, Level worldIn) {
        super(type, worldIn);
        this.moveController = new FlyingMovementController(this, 20, true);
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return FamiliarEntity.registerAttributes().createMutableAttribute(Attributes.FLYING_SPEED, 0.4);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25));
        SitGoal sitGoal = new SitGoal(this);
        sitGoal.setMutexFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.goalSelector.addGoal(2, sitGoal);
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1, 4, 1));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1));
        this.goalSelector.addGoal(6, new FollowMobGoal(this, 1, 3, 7));
    }

    @Override
    protected PathNavigator createNavigator(Level level) {
        FlyingPathNavigator navigator = new FlyingPathNavigator(this, level) {
            @Override
            public boolean canEntityStandOnPos(BlockPos pos) {
                BlockState state = this.level.getBlockState(pos);
                return state.getBlock().isAir(state, this.level, pos) || !state.getMaterial().blocksMovement();
            }
        };
        return navigator;
    }

    @Override
    public boolean onLivingFall(float fallDistance, float damageMultiplier) {
        return false;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isSitting())
            this.setMotion(Vec3.ZERO);
    }
    
    public float getAnimationHeight(float partialTicks) {
        return Mth.cos((ticksExisted + partialTicks) / 5);
    }

    @Override
    public Iterable<EffectInstance> getFamiliarEffects() {
        if (this.getFamiliarOwner().getCapability(OccultismCapabilities.FAMILIAR_SETTINGS)
                .map(FamiliarSettingsCapability::isBatEnabled).orElse(false)) {
            return ImmutableList.of(new EffectInstance(Effects.NIGHT_VISION, 300, 1, false, false));
        }
        return Collections.emptyList();
    }
}
