package net.enderwish.HUD_Visuals_Subpack.api;

import net.minecraft.resources.ResourceLocation;
import java.util.List;

/**
 * Unified Weather Type.
 * The fields here match the keys in your JSON files.
 */
public record WeatherType(
        int weight,              // For the D100 roll
        float max_temp_offset,   // Degrees at 1.0 intensity
        boolean is_special,      // Forces 24000 ticks and 1.0 intensity
        float wind_speed,        // For visual particle angling
        VisualData visuals,      // Sub-object for fog/sky
        List<String> tags        // For logic checks (is_freezing, etc.)
) {
    public record VisualData(
            String fog_color,    // Hex string like "#D0D0D0"
            float sky_darkness,
            String particle_id
    ) {}

    // Helper to check if a weather has a specific tag
    public boolean hasTag(String tag) {
        return tags != null && tags.contains(tag);
    }
}