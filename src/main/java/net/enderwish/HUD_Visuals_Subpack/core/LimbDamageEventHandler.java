package net.enderwish.HUD_Visuals_Subpack.core;

import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.enderwish.HUD_Visuals_Subpack.HUDVisualsSubpack;

import java.util.Random;

/**
 * Merged handler that uses your ModAttachments and scaling logic
 * while adding specific logic for falls and explosions.
 */
@EventBusSubscriber(modid = HUDVisualsSubpack.MOD_ID)
public class LimbDamageEventHandler {
    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public static void onPlayerDamage(LivingDamageEvent.Post event) {
        // Only process for players on the server side
        if (!(event.getEntity() instanceof Player player) || player.level().isClientSide) {
            return;
        }

        // GET THE DATA DIRECTLY
        WristCapability cap = player.getData(ModAttachments.WRIST_CAP);

        // CONFIGURATION
        float amount = event.getNewDamage() * 0.1f; // Your original 0.1f scale

        // DETERMINISTIC LOGIC (Specific sources)
        if (event.getSource().is(DamageTypes.FALL)) {
            // Fall damage splits damage between both legs
            float legDamage = amount / 2f;
            cap.damageLeftLeg(legDamage);
            cap.damageRightLeg(legDamage);
        }
        else if (event.getSource().is(DamageTypes.EXPLOSION)) {
            // Explosions are messy: Torso and Legs
            cap.damageTorso(amount * 0.5f);
            cap.damageLeftLeg(amount * 0.25f);
            cap.damageRightLeg(amount * 0.25f);
        }
        else {
            // RANDOMIZED COMBAT LOGIC (Your original height-based system)
            double hitY = RANDOM.nextDouble();

            if (hitY > 0.8) {
                cap.damageHead(amount);
            } else if (hitY > 0.4) {
                cap.damageTorso(amount);
            } else if (hitY > 0.2) {
                // Randomly pick an arm
                if (RANDOM.nextBoolean()) cap.damageLeftArm(amount);
                else cap.damageRightArm(amount);
            } else {
                // Randomly pick a leg
                if (RANDOM.nextBoolean()) cap.damageLeftLeg(amount);
                else cap.damageRightLeg(amount);
            }
        }

        // SYNC THE DATA
        // Calling setData marks the attachment as "dirty" for NeoForge syncing
        player.setData(ModAttachments.WRIST_CAP, cap);
    }
}