package net.enderwish.HUD_Visuals_Subpack.network;

import net.enderwish.HUD_Visuals_Subpack.HUDVisualsSubpack;
import net.enderwish.HUD_Visuals_Subpack.core.LimbSyncPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModMessages {

    /**
     * Registers the networking channel and payloads.
     * Ensure this is called via: modEventBus.addListener(ModMessages::register);
     */
    public static void register(final RegisterPayloadHandlersEvent event) {
        // Use a versioned registrar to prevent client/server mismatches
        final PayloadRegistrar registrar = event.registrar(HUDVisualsSubpack.MOD_ID)
                .versioned("1.0");

        // playToClient means Server -> Client (S2C)
        // This is where the HUD data is sent to the player's screen
        registrar.playToClient(
                LimbSyncPacket.TYPE,
                LimbSyncPacket.STREAM_CODEC,
                LimbSyncPacket::handle
        );
    }

    /**
     * Helper to send a packet to a specific player.
     * Useful for syncing limb data when damage occurs or on login.
     */
    public static void sendToPlayer(CustomPacketPayload packet, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, packet);
    }
}