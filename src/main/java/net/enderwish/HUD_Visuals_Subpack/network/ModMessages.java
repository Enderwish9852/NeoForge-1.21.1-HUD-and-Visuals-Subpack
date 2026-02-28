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
     * This method must be called from your main class constructor like this:
     * modEventBus.addListener(ModMessages::register);
     */
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(HUDVisualsSubpack.MOD_ID)
                .versioned("1.0");

        // Register the LimbSyncPacket so the client can receive data from the server
        registrar.playToClient(
                LimbSyncPacket.TYPE,
                LimbSyncPacket.STREAM_CODEC,
                LimbSyncPacket::handle
        );
    }

    public static void sendToPlayer(CustomPacketPayload packet, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, packet);
    }
}