package net.enderwish.HUD_Visuals_Subpack;

import net.enderwish.HUD_Visuals_Subpack.client.gui.SportsWatchHUD;
import net.enderwish.HUD_Visuals_Subpack.core.LimbDamageEventHandler;
import net.enderwish.HUD_Visuals_Subpack.core.ModAttachments;
import net.enderwish.HUD_Visuals_Subpack.event.HealthRegenEvents;
import net.enderwish.HUD_Visuals_Subpack.network.ModMessages;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

/**
 * Main Mod Class for HUD Visuals Subpack.
 * Handles the initialization of networking, HUD layers, and event subscribers.
 */
@Mod(HUDVisualsSubpack.MOD_ID)
public class HUDVisualsSubpack {
    public static final String MOD_ID = "gh_hud_visuals";

    public HUDVisualsSubpack(IEventBus modEventBus) {
        // --- MOD BUS REGISTRATION ---

        // Register Data Attachments (Limb Data storage)
        ModAttachments.ATTACHMENT_TYPES.register(modEventBus);

        // Register Networking Payloads (including LimbSyncS2CPacket)
        modEventBus.addListener(this::registerNetworking);

        // --- FORGE BUS REGISTRATION ---

        // Register server-side events for health and limb logic
        NeoForge.EVENT_BUS.register(new HealthRegenEvents());
        NeoForge.EVENT_BUS.register(new LimbDamageEventHandler());

        // --- CLIENT SETUP ---
        if (FMLEnvironment.dist.isClient()) {
            modEventBus.addListener(this::onRegisterGuiLayers);
        }
    }

    /**
     * Registers custom network payloads using the NeoForge RegisterPayloadHandlersEvent.
     * This is required to sync limb data from the server to the client's SportsWatchHUD.
     */
    private void registerNetworking(final RegisterPayloadHandlersEvent event) {
        ModMessages.register(event);
    }

    /**
     * Handles the registration and modification of HUD elements.
     * This runs on the Mod Event Bus.
     */
    private void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
        // Register the custom Sports Watch HUD above the Hotbar
        event.registerAbove(
                VanillaGuiLayers.HOTBAR,
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "sports_watch"),
                SportsWatchHUD.SPORTS_WATCH_ELEMENT
        );

        // Hide specific Vanilla HUD layers to make room for the new UI
        event.replaceLayer(VanillaGuiLayers.PLAYER_HEALTH, (gui, delta) -> {});
        event.replaceLayer(VanillaGuiLayers.FOOD_LEVEL, (gui, delta) -> {});
        event.replaceLayer(VanillaGuiLayers.ARMOR_LEVEL, (gui, delta) -> {});
        event.replaceLayer(VanillaGuiLayers.EXPERIENCE_BAR, (gui, delta) -> {});
        event.replaceLayer(VanillaGuiLayers.AIR_LEVEL, (gui, delta) -> {});
    }
}