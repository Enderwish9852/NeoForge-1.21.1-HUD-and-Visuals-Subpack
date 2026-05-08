package net.enderwish.HUD_Visuals_Subpack.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

/**
 * A lightweight snapshot of the current world climate.
 * Sent to the client to handle visuals and HUD updates.
 */
public record ClimateData(
        String seasonId,         // e.g., "mid_winter"
        ResourceLocation weatherId, // e.g., "modid:blizzard"
        float intensity,         // 0.001 to 1.0
        int ticksRemaining,      // Remaining duration of current weather
        boolean isSpecial        // True if 24000 ticks / 1.0 intensity
) {
    public static final Codec<ClimateData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("season").forGetter(ClimateData::seasonId),
                    ResourceLocation.CODEC.fieldOf("weather").forGetter(ClimateData::weatherId),
                    Codec.FLOAT.fieldOf("intensity").forGetter(ClimateData::intensity),
                    Codec.INT.fieldOf("duration").forGetter(ClimateData::ticksRemaining),
                    Codec.BOOL.fieldOf("special").forGetter(ClimateData::isSpecial)
            ).apply(instance, ClimateData::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ClimateData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ClimateData::seasonId,
            ResourceLocation.STREAM_CODEC, ClimateData::weatherId,
            ByteBufCodecs.FLOAT, ClimateData::intensity,
            ByteBufCodecs.VAR_INT, ClimateData::ticksRemaining,
            ByteBufCodecs.BOOL, ClimateData::isSpecial,
            ClimateData::new
    );

    /**
     * Default state used when the world first loads.
     */
    public static ClimateData getDefault() {
        return new ClimateData("spring", ResourceLocation.withDefaultNamespace("clear"), 0.0f, 12000, false);
    }
}