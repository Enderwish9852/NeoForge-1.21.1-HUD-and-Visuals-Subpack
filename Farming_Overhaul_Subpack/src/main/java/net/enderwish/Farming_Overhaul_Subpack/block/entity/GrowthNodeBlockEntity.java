package net.enderwish.Farming_Overhaul_Subpack.block.entity;

import net.enderwish.Farming_Overhaul_Subpack.block.ModBlocks;
import net.enderwish.Farming_Overhaul_Subpack.init.ModBlockEntities;
import net.enderwish.Farming_Overhaul_Subpack.util.BranchingMathCore;
import net.enderwish.Farming_Overhaul_Subpack.world.tree.TreeDNA;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class GrowthNodeBlockEntity extends BlockEntity {

    public GrowthNodeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GROWTH_NODE.get(), pos, state);
    }

    /**
     * Triggered by the GrowthNodeBlock when STAGE reaches 2.
     */
    public void triggerTreeGrowth() {
        if (this.level instanceof ServerLevel serverLevel) {
            // 1. Remove any sapling sitting on top of the node
            BlockPos saplingPos = this.worldPosition.above();
            if (serverLevel.getBlockState(saplingPos).is(Blocks.OAK_SAPLING) ||
                    serverLevel.getBlockState(saplingPos).is(ModBlocks.OAK_ADAPTIVE_SAPLING.get())) {
                serverLevel.setBlockAndUpdate(saplingPos, Blocks.AIR.defaultBlockState());
            }

            // 2. Trigger the procedural branching math using TreeDNA
            // FIX: Changed from passing a Block to passing the TreeDNA record
            BranchingMathCore.growTree(serverLevel, this.worldPosition, TreeDNA.OAK);

            // 3. Transform the node back into regular dirt so the process finishes
            serverLevel.setBlockAndUpdate(this.worldPosition, Blocks.DIRT.defaultBlockState());
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
    }
}