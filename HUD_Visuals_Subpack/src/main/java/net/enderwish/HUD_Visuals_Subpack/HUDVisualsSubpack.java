package net.enderwish.HUD_Visuals_Subpack;

import net.enderwish.HUD_Visuals_Subpack.command.SeasonCommand;
import net.enderwish.HUD_Visuals_Subpack.command.WeatherCommand;
import net.enderwish.HUD_Visuals_Subpack.core.weather.WeatherRegistry;
import net.enderwish.HUD_Visuals_Subpack.event.SeasonEventHandler;
import net.enderwish.HUD_Visuals_Subpack.network.ModMessages;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(HUDVisualsSubpack.MOD_ID)
public class HUDVisualsSubpack {

    public static final String MOD_ID = "gh_hud_visuals";

    public HUDVisualsSubpack(IEventBus modEventBus) {

        // ── Mod bus events (fired by NeoForge during startup) ─────────────────
        modEventBus.addListener(this::registerNetworking);
        modEventBus.addListener(this::onRegisterGuiLayers);

        // ── NeoForge bus events (fired during gameplay) ───────────────────────
        NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);

        NeoForge.EVENT_BUS.register(SeasonEventHandler.class);

        // ── Register WeatherRegistry as a reload listener ─────────────────────
        // This makes NeoForge call WeatherRegistry.apply() when the world loads
        // or when /reload is run — automatically loading all weather JSON files
        NeoForge.EVENT_BUS.addListener(EventPriority.NORMAL, false,
                AddReloadListenerEvent.class,
                e -> e.addListener(WeatherRegistry.INSTANCE)
        );
    }

    // ── Event handlers ────────────────────────────────────────────────────────

    /**
     * Networking — registered on the MOD event bus.
     */
    private void registerNetworking(final RegisterPayloadHandlersEvent event) {
        ModMessages.register(event);
    }

    /**
     * Commands — registered on the NEOFORGE event bus.
     */
    private void onRegisterCommands(RegisterCommandsEvent event) {
        WeatherCommand.register(event.getDispatcher());
        SeasonCommand.register(event.getDispatcher());
    }

    /**
     * GUI Layers — registered on the MOD event bus.
     * Replaces vanilla HUD elements with our custom ones.
     */
    private void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
        event.replaceLayer(VanillaGuiLayers.PLAYER_HEALTH,  (graphics, delta) -> {});
        event.replaceLayer(VanillaGuiLayers.FOOD_LEVEL,     (graphics, delta) -> {});
        event.replaceLayer(VanillaGuiLayers.ARMOR_LEVEL,    (graphics, delta) -> {});
        event.replaceLayer(VanillaGuiLayers.EXPERIENCE_BAR, (graphics, delta) -> {});
        event.replaceLayer(VanillaGuiLayers.AIR_LEVEL,      (graphics, delta) -> {});
    }
}