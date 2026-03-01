package net.enderwish.HUD_Visuals_Subpack.core;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import java.util.function.Supplier;

/**
 * This class handles the registration of the "Limb Health" data attachment.
 * Place this in your core package.
 */
public class LimbDataProvider {

    // The DeferredRegister for AttachmentTypes
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, "limbhealth_hud");

    /**
     * This is the "Key" used to access the limb data on a player.
     * Use player.getData(LimbDataProvider.LIMB_HEALTH_DATA) to read/write.
     */
    public static final Supplier<AttachmentType<LimbHealth>> LIMB_HEALTH_DATA = ATTACHMENT_TYPES.register(
            "limb_health",
            () -> AttachmentType.builder(() -> LimbHealth.FULL_HEALTH) // Default value is full health
                    .serialize(LimbHealth.CODEC)                      // Use the codec we made in LimbHealth.java
                    .copyOnDeath()                                    // Persist health data after the player respawns
                    .build()
    );

    /**
     * Call this method in your main Mod class constructor!
     * Example: LimbDataProvider.register(modEventBus);
     */
    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}