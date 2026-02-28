package net.enderwish.HUD_Visuals_Subpack.core;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.enderwish.HUD_Visuals_Subpack.HUDVisualsSubpack;

import java.util.Random;

@EventBusSubscriber(modid = HUDVisualsSubpack.MOD_ID)
public class LimbDamageEventHandler {
    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public static void onPlayerDamage(LivingDamageEvent.Post event) {
        // Only process for players on the server side
        if (!(event.getEntity() instanceof Player player) || player.level().isClientSide) {
            return;
        }

        // 1. GET THE DATA DIRECTLY
        // In 1.21.1, getData returns the object itself, not an Optional.
        WristCapability cap = player.getData(ModAttachments.WRIST_CAP);

        // 2. LOGIC
        float damageAmount = event.getNewDamage() * 0.1f; // Scale damage for limb health
        double hitY = RANDOM.nextDouble(); // Simulated hit height

        if (hitY > 0.8) {
            cap.damageHead(damageAmount);
        } else if (hitY > 0.4) {
            cap.damageTorso(damageAmount);
        } else if (hitY > 0.2) {
            // Randomly pick an arm
            if (RANDOM.nextBoolean()) cap.damageLeftArm(damageAmount);
            else cap.damageRightArm(damageAmount);
        } else {
            // Randomly pick a leg
            if (RANDOM.nextBoolean()) cap.damageLeftLeg(damageAmount);
            else cap.damageRightLeg(damageAmount);
        }

        // 3. SYNC THE DATA
        // Calling setData marks the attachment as "dirty" so NeoForge
        // knows it needs to be synced to the client.
        player.setData(ModAttachments.WRIST_CAP, cap);
    }
}