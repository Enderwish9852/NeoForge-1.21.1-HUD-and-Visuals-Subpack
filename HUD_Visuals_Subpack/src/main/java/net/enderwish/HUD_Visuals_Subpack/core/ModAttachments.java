package net.enderwish.HUD_Visuals_Subpack.core;

import net.enderwish.HUD_Visuals_Subpack.HUDVisualsSubpack;
import net.enderwish.HUD_Visuals_Subpack.api.ClimateData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, HUDVisualsSubpack.MOD_ID);


    /**
     * Climate State (Level-based)
     * Attached directly to the Level to track World Weather.
     */
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ClimateData>> CLIMATE =
            ATTACHMENT_TYPES.register("climate_state", () -> AttachmentType.builder(ClimateData::getDefault)
                    .serialize(ClimateData.CODEC)
                    .build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}