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

package com.github.klikli_dev.occultism.common.entity.ai;

import com.github.klikli_dev.occultism.Occultism;
import com.github.klikli_dev.occultism.common.entity.spirit.SpiritEntity;
import com.github.klikli_dev.occultism.common.job.LumberjackJob;
import com.github.klikli_dev.occultism.network.MessageSelectBlock;
import com.github.klikli_dev.occultism.network.OccultismPackets;
import com.github.klikli_dev.occultism.registry.OccultismTags;
import com.github.klikli_dev.occultism.util.Math3DUtil;
import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FellTreesGoal extends Goal {
    //region Fields
    public static final int WORKAREA_EMPTY_REFRESH_TIME = 20 * 15;

    protected final SpiritEntity entity;
    protected final BlockSorter targetSorter;
    protected BlockPos targetBlock = null;
    protected BlockPos moveTarget = null;
    protected int breakingTime;
    protected int previousBreakProgress;
    protected boolean isTargetTree;
    protected long lastWorkareaEmptyTime;

    //endregion Fields

    //region Initialization
    public FellTreesGoal(SpiritEntity entity) {
        this.entity = entity;
        this.targetSorter = new BlockSorter(entity);
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }
    //endregion Initialization

    //region Overrides
    @Override
    public boolean shouldExecute() {
        if (!this.entity.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
            return false; //if already holding an item we need to first store it.
        }
        this.resetTarget();
        return this.targetBlock != null;
    }

    @Override
    public boolean shouldContinueExecuting() {
        //only continue execution if a tree is available and entity is not carrying anything.
        return this.targetBlock != null && this.entity.getHeldItem(Hand.MAIN_HAND).isEmpty();
    }

    public void resetTask() {
        this.entity.getNavigator().clearPath();
        this.targetBlock = null;
        this.moveTarget = null;
    }

    @Override
    public void tick() {
        if (this.targetBlock != null) {

            this.entity.getNavigator().setPath(
                    this.entity.getNavigator().getPathToPos(this.moveTarget, 0), 1.0f);

            if (Occultism.DEBUG.debugAI) {
                OccultismPackets.sendToTracking(this.entity, new MessageSelectBlock(this.targetBlock, 5000, 0xffffff));
                OccultismPackets.sendToTracking(this.entity, new MessageSelectBlock(this.moveTarget, 5000, 0x00ff00));
            }

            if (isLog(this.entity.world, this.targetBlock)) {
                double distance = this.entity.getPositionVec().distanceTo(Math3DUtil.center(this.moveTarget));
                if (distance < 2.5F) {
                    //start breaking when close
                    if (distance < 1F) {
                        //Stop moving if very close
                        this.entity.setMotion(0, 0, 0);
                        this.entity.getNavigator().clearPath();
                    }

                    //only when spirit gets to target do we check if it really is a tree
                    //this way spirit moves around a bit more, but we space out the intensive tree-identification
                    if (this.isTargetTree || this.isTree(this.targetBlock)) {
                        //cache isTargetTree until we broke it
                        this.isTargetTree = true;
                        this.updateBreakBlock();
                    } else {
                        this.isTargetTree = false;
                        this.entity.getJob().map(j -> (LumberjackJob) j).ifPresent(j -> {
                            j.getIgnoredTrees().add(this.targetBlock);
                        });
                        this.resetTarget();
                    }
                }
            } else {
                this.resetTask();
            }
        }
    }
    //endregion Overrides

    //region Static Methods

    public static boolean isTreeSoil(World world, BlockPos pos) {
        return OccultismTags.TREE_SOIL.contains(world.getBlockState(pos).getBlock());
    }

    public static boolean isLog(World world, BlockPos pos) {
        return BlockTags.LOGS.contains(world.getBlockState(pos).getBlock());
    }

    public static boolean isLeaf(World world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        return block instanceof LeavesBlock || BlockTags.LEAVES.contains(block);
    }
    //endregion Static Methods

    //region Methods
    public void updateBreakBlock() {
        this.breakingTime++;
        this.entity.swingArm(Hand.MAIN_HAND);
        int i = (int) ((float) this.breakingTime / 160.0F * 10.0F);
        if (this.breakingTime % 10 == 0) {
            this.entity.playSound(SoundEvents.BLOCK_WOOD_HIT, 1, 1);
            this.entity.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1, 0.5F);
        }
        if (i != this.previousBreakProgress) {
            this.entity.world.sendBlockBreakProgress(this.entity.getEntityId(), this.targetBlock, i);
            this.previousBreakProgress = i;
        }
        if (this.breakingTime == 160) {
            this.entity.playSound(SoundEvents.BLOCK_WOOD_BREAK, 1, 1);
            this.breakingTime = 0;
            this.previousBreakProgress = -1;
            this.fellTree();
            this.entity.getJob().map(j -> (LumberjackJob) j).ifPresent(j -> j.setLastFelledTree(this.targetBlock));
            this.targetBlock = null;
            this.resetTask();
        }

    }

    private void resetTarget() {
        this.isTargetTree = false;
        World world = this.entity.world;

        //if work area was recently empty, wait until refresh time has elapsed
        if (world.getGameTime() - this.lastWorkareaEmptyTime < WORKAREA_EMPTY_REFRESH_TIME)
            return;

        Set<BlockPos> ignoredTrees = this.entity.getJob().map(j -> (LumberjackJob) j).map(LumberjackJob::getIgnoredTrees).orElse(new HashSet<>());

        BlockPos workAreaCenter = this.entity.getWorkAreaCenter();
        //get work area, but only half height, we don't need full.
        int workAreaSize = this.entity.getWorkAreaSize().getValue();
        Stream<BlockPos> stream = BlockPos.getAllInBox(
                        workAreaCenter.add(-workAreaSize, -workAreaSize / 2, -workAreaSize),
                        workAreaCenter.add(workAreaSize, workAreaSize / 2, workAreaSize))
                .map(BlockPos::toImmutable);

        //filter potential stumps
        List<BlockPos> potentialStumps = stream.filter(pos ->
                isLog(world, pos) && isTreeSoil(world, pos.down()) && !ignoredTrees.contains(pos)
        ).collect(Collectors.toList());

        if (!potentialStumps.isEmpty()) {
            potentialStumps.sort(this.targetSorter);
            this.targetBlock = potentialStumps.get(0);

            //Find a nearby empty block to move to
            this.moveTarget = null;
            for (Direction facing : Direction.Plane.HORIZONTAL) {
                BlockPos pos = this.targetBlock.offset(facing);
                if (this.entity.world.isAirBlock(pos)) {
                    this.moveTarget = pos;
                    break;
                }
            }

            //none found -> invalid target
            if (this.moveTarget == null) {
                this.targetBlock = null;
            }
        } else {
            //if we found nothing in our work area, go on a slow tick;
            this.lastWorkareaEmptyTime = world.getGameTime();
            this.moveTarget = null;
            this.targetBlock = null;
        }
    }

    private boolean isTree(BlockPos potentialStump) {
        if (isLog(this.entity.world, potentialStump)) {

            //find top of tree
            BlockPos topOfTree = new BlockPos(potentialStump);
            while (!this.entity.world.isAirBlock(topOfTree.up()) && topOfTree.getY() < this.entity.world.getHeight()) {
                topOfTree = topOfTree.up();
            }

            //find the stump of the tree
            if (isLeaf(this.entity.world, topOfTree)) {
                BlockPos logPos = this.getStump(topOfTree);
                if (isLog(this.entity.world, logPos))
                    return true;
            }
        }
        return false;
    }

    /**
     * Gets the stump for the given log.
     *
     * @param log the log
     * @return the stump block position.
     */
    private BlockPos getStump(BlockPos log) {
        if (log.getY() > 0) {
            //for all nearby logs and leaves, move one block down and recurse.
            for (BlockPos pos : BlockPos.getAllInBox(log.add(-4, -4, -4), log.add(4, 0, 4)).map(BlockPos::toImmutable)
                    .collect(
                            Collectors.toList())) {
                if (isLog(this.entity.world, pos.down()) || isLeaf(this.entity.world, pos.down())) {
                    return this.getStump(pos.down());
                }
            }
        }
        return log;
    }

    private void fellTree() {
        World world = this.entity.world;
        BlockPos base = new BlockPos(this.targetBlock);
        Queue<BlockPos> blocks = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();
        blocks.add(base);

        while (!blocks.isEmpty()) {

            BlockPos pos = blocks.remove();
            if (!visited.add(pos)) {
                continue;
            }

            if (!isLog(world, pos)) {
                continue;
            }

            for (Direction facing : Direction.Plane.HORIZONTAL) {
                BlockPos pos2 = pos.offset(facing);
                if (!visited.contains(pos2)) {
                    blocks.add(pos2);
                }
            }

            for (int x = 0; x < 3; x++) {
                for (int z = 0; z < 3; z++) {
                    BlockPos pos2 = pos.add(-1 + x, 1, -1 + z);
                    if (!visited.contains(pos2)) {
                        blocks.add(pos2);
                    }
                }
            }

            world.destroyBlock(pos, true);
        }

    }

    //endregion Methods

}
