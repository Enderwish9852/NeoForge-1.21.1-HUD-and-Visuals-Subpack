package net.enderwish.HUD_Visuals_Subpack.core;

import net.enderwish.HUD_Visuals_Subpack.HUDVisualsSubpack;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModAttachments {
    // This handles the registration process with NeoForge
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, HUDVisualsSubpack.MOD_ID);

    // This defines the "WRIST_CAP" symbol your other class is looking for
    // In 1.21.1, we use .serialize(WristCapability.CODEC) to handle saving/loading
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<WristCapability>> WRIST_CAP =
            ATTACHMENT_TYPES.register("wrist_cap", () -> AttachmentType.builder(WristCapability::new)
                    .serialize(WristCapability.CODEC) // This replaces serializeDefault()
                    .copyOnDeath() // Keeps data when the player dies
                    .build());
}