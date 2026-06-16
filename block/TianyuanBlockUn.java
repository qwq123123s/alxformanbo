/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.material.MapColor
 *  net.minecraft.world.level.material.PushReaction
 */
package com.manbo.v2c.block;

import com.manbo.v2c.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class TianyuanBlockUn
extends Block {
    public TianyuanBlockUn() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN).strength(5.0f, 3600000.0f).requiresCorrectToolForDrops().pushReaction(PushReaction.BLOCK).randomTicks());
    }

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.checkWaterAndTransform(level, pos);
    }

    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (level.isClientSide()) {
            return;
        }
        this.checkWaterAndTransform((ServerLevel)level, pos);
    }

    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        if (level.isClientSide()) {
            return;
        }
        this.checkWaterAndTransform((ServerLevel)level, pos);
    }

    private void checkWaterAndTransform(ServerLevel level, BlockPos pos) {
        boolean hasWater;
        BlockPos above = pos.above();
        BlockState aboveState = level.getBlockState(above);
        boolean bl = hasWater = aboveState.is(Blocks.WATER) || level.getBlockState(pos.north()).is(Blocks.WATER) || level.getBlockState(pos.south()).is(Blocks.WATER) || level.getBlockState(pos.east()).is(Blocks.WATER) || level.getBlockState(pos.west()).is(Blocks.WATER);
        if (hasWater) {
            level.setBlock(pos, ((Block)ModBlocks.TIANYUAN_BLOCK.get()).defaultBlockState(), 3);
        }
    }
}

