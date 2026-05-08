package net.enderwish.Farming_Overhaul_Subpack.world.tree.feature;

import com.mojang.serialization.Codec;
import net.enderwish.Farming_Overhaul_Subpack.util.BranchingMathCore;
import net.enderwish.Farming_Overhaul_Subpack.world.tree.TreeDNA;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class BranchingTreeFeature extends Feature<NoneFeatureConfiguration> {
    private final TreeDNA dna;

    public BranchingTreeFeature(Codec<NoneFeatureConfiguration> codec, TreeDNA dna) {
        super(codec);
        this.dna = dna;
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();

        // 1. Check if the block above the ground is replaceable (Air, Grass, etc.)
        if (!level.getBlockState(pos).canBeReplaced()) {
            return false;
        }

        // 2. Ensure we are planting on valid soil (Dirt, Grass, etc.)
        if (level.getBlockState(pos.below()).is(BlockTags.DIRT)) {

            // Trigger the growth!
            // Suggestion: Make sure growTree takes (WorldGenLevel, BlockPos, TreeDNA)
            BranchingMathCore.growTree(level, pos, dna);

            return true;
        }

        return false;
    }
}
