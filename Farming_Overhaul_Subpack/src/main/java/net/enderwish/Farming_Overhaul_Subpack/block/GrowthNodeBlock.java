package net.enderwish.Farming_Overhaul_Subpack.block;

import com.mojang.serialization.MapCodec;
import net.enderwish.Farming_Overhaul_Subpack.block.entity.GrowthNodeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public class GrowthNodeBlock extends BaseEntityBlock implements BonemealableBlock {
    public static final MapCodec<GrowthNodeBlock> CODEC = simpleCodec(GrowthNodeBlock::new);

    // Growth stages: 0, 1, 2
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 2);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public GrowthNodeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
    }

    // --- RANDOM TICKING ---
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Grow naturally if light is sufficient
        if (level.getMaxLocalRawBrightness(pos.above()) >= 9 && random.nextInt(7) == 0) {
            this.performBonemeal(level, random, pos, state);
        }
    }

    // --- BONEMEAL INTERFACE ---
    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return state.getValue(STAGE) < 2;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int currentStage = state.getValue(STAGE);
        if (currentStage < 2) {
            level.setBlock(pos, state.setValue(STAGE, currentStage + 1), 2);
        } else {
            // Logic to trigger tree growth

            // level.removeBlock(pos, false);
            // Spawn your tree features here
        }
    }

    // --- BLOCK ENTITY & RENDER ---
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GrowthNodeBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }
}