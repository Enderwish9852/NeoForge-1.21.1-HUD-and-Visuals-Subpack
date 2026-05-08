package net.enderwish.Farming_Overhaul_Subpack.world.tree;

import net.enderwish.Farming_Overhaul_Subpack.block.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * TreeDNA defines the "biological rules" for how a specific species grows.
 */
public record TreeDNA(
        Block logBlock,          // The main base log
        Block branchBlock,       // The custom block that supports thickness
        Block leafBlock,         // The leaves to place at the ends
        int maxDepth,            // How many times the tree can branch out
        float branchProbability, // Chance (0.0 to 1.0) of a branch splitting
        float minAngle,          // Minimum spread angle
        float maxAngle,          // Maximum spread angle
        int averageLogLength     // How many blocks to grow before attempting a split
) {

    // --- VANILLA UPGRADE PRESETS ---

    public static final TreeDNA OAK = new TreeDNA(
            Blocks.OAK_LOG,
            ModBlocks.OAK_BRANCH.get(), // Now linked to your custom branch!
            Blocks.OAK_LEAVES,
            5,
            0.45f,
            30.0f,
            60.0f,
            3
    );

    public static final TreeDNA BIRCH = new TreeDNA(
            Blocks.BIRCH_LOG,
            ModBlocks.BIRCH_BRANCH.get(),
            Blocks.BIRCH_LEAVES,
            4,
            0.15f,
            10.0f,
            25.0f,
            5
    );

    public static final TreeDNA SPRUCE = new TreeDNA(
            Blocks.SPRUCE_LOG,
            ModBlocks.SPRUCE_BRANCH.get(),
            Blocks.SPRUCE_LEAVES,
            6,
            0.80f,
            45.0f,
            90.0f,
            1
    );

    public static final TreeDNA JUNGLE = new TreeDNA(
            Blocks.JUNGLE_LOG,
            ModBlocks.JUNGLE_BRANCH.get(),
            Blocks.JUNGLE_LEAVES,
            7,      // Jungle trees are huge
            0.60f,  // Quite branchy
            20.0f,
            45.0f,
            4
    );
}
