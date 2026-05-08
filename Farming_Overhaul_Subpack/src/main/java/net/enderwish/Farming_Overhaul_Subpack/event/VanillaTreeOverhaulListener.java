package net.enderwish.Farming_Overhaul_Subpack.event;

import net.enderwish.Farming_Overhaul_Subpack.FarmingOverhaulSubpack;
import net.enderwish.Farming_Overhaul_Subpack.util.BranchingMathCore;
import net.enderwish.Farming_Overhaul_Subpack.world.tree.TreeDNA;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockGrowFeatureEvent;

@EventBusSubscriber(modid = FarmingOverhaulSubpack.MODID)
public class VanillaTreeOverhaulListener {

    @SubscribeEvent
    public static void onSaplingGrow(BlockGrowFeatureEvent event) {
        // 1. Ensure we are on the server and the event hasn't been canceled
        if (!(event.getLevel() instanceof ServerLevel level) || event.isCanceled()) return;

        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        TreeDNA dna = null;

        // 2. Determine which DNA to use based on the vanilla sapling type
        if (state.is(Blocks.OAK_SAPLING)) {
            dna = TreeDNA.OAK;
        } else if (state.is(Blocks.BIRCH_SAPLING)) {
            dna = TreeDNA.BIRCH;
        } else if (state.is(Blocks.SPRUCE_SAPLING)) {
            dna = TreeDNA.SPRUCE;
        } else if (state.is(Blocks.JUNGLE_SAPLING)) {
            dna = TreeDNA.JUNGLE;
        }
        // Note: You can add Acacia and Dark Oak here once you define them in your TreeDNA class

        // 3. Hijack the growth process
        if (dna != null) {
            // Cancel the vanilla tree feature placement so it doesn't spawn a vanilla tree
            event.setCanceled(true);

            // Clear the sapling block to make room for your custom trunk
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

            // FIX: Pass the full 'dna' record instead of just dna.branchBlock()
            BranchingMathCore.growTree(level, pos, dna);

            // Debug log to confirm the hijack worked
            System.out.println("Procedural VTO: Hijacked " + state.getBlock().getName().getString() + " at " + pos);
        }
    }
}