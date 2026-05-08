package net.enderwish.Farming_Overhaul_Subpack.util;

import net.enderwish.Farming_Overhaul_Subpack.block.AbstractBranchBlock;
import net.enderwish.Farming_Overhaul_Subpack.world.tree.TreeDNA;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class BranchingMathCore {

    /**
     * Entry point for growing the tree using TreeDNA rules.
     */
    public static void growTree(LevelAccessor level, BlockPos pos, TreeDNA dna) {
        RandomSource random = level.getRandom();
        // Use the maxDepth from DNA for the starting thickness
        executeBranch(level, pos, Direction.UP, dna.maxDepth(), dna, random);
    }

    private static void executeBranch(LevelAccessor level, BlockPos pos, Direction direction, int depth, TreeDNA dna, RandomSource random) {
        // TERMINATION: If depth is 0, place the DNA-specific leaves and stop.
        if (depth <= 0) {
            placeLeaves(level, pos, dna);
            return;
        }

        // Segment length: Uses DNA rules to determine length per "joint"
        int segmentLength = dna.averageLogLength() + random.nextInt(2);
        BlockPos currentPos = pos;

        for (int i = 0; i < segmentLength; i++) {
            currentPos = currentPos.relative(direction);

            if (!level.hasChunkAt(currentPos)) return;

            BlockState currentState = level.getBlockState(currentPos);
            if (currentState.isAir() || currentState.canBeReplaced()) {

                // THICKNESS: Based on current recursion depth
                int thickness = Math.max(1, depth);

                // Apply the DNA's branch block and properties
                BlockState branchState = dna.branchBlock().defaultBlockState()
                        .setValue(AbstractBranchBlock.THICKNESS, thickness)
                        .setValue(AbstractBranchBlock.FACING, direction);

                level.setBlock(currentPos, branchState, 3);
            } else {
                return;
            }
        }

        // DECISION: Split based on DNA branchProbability
        if (random.nextFloat() < dna.branchProbability()) {
            // Split into two branches
            executeBranch(level, currentPos, getRandomSideDirection(random, direction), depth - 1, dna, random);
            executeBranch(level, currentPos, getRandomSideDirection(random, direction), depth - 1, dna, random);
        } else {
            // Continue growing in a slightly varied direction
            Direction nextDir = random.nextFloat() < 0.2f ? getRandomSideDirection(random, direction) : direction;
            executeBranch(level, currentPos, nextDir, depth - 1, dna, random);
        }
    }

    private static void placeLeaves(LevelAccessor level, BlockPos pos, TreeDNA dna) {
        // Place a small clump of leaves defined in the DNA
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos leafPos = pos.offset(x, y, z);
                    if (level.getBlockState(leafPos).isAir()) {
                        level.setBlock(leafPos, dna.leafBlock().defaultBlockState(), 3);
                    }
                }
            }
        }
    }

    private static Direction getRandomSideDirection(RandomSource random, Direction current) {
        Direction dir = Direction.getRandom(random);
        if (dir == Direction.DOWN || dir == current.getOpposite()) {
            return Direction.UP;
        }
        return dir;
    }
}