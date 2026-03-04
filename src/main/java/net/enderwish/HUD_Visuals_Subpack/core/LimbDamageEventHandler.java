package net.enderwish.HUD_Visuals_Subpack.core;

import net.enderwish.HUD_Visuals_Subpack.HUDVisualsSubpack;
import net.enderwish.HUD_Visuals_Subpack.network.LimbSyncPacket;
import net.enderwish.HUD_Visuals_Subpack.network.ModMessages;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.Random;

/**
 * Handles logic for the permanent Limb Damage System.
 * The system is always active; the Watch only acts as a display.
 */
@EventBusSubscriber(modid = HUDVisualsSubpack.MOD_ID)
public class LimbDamageEventHandler {
    private static final Random RANDOM = new Random();

    /**
     * Permanent Health Lock and Watch Detection.
     */
    @SubscribeEvent
    public static void onPlayerTick(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            WristCapability cap = player.getData(ModAttachments.WRIST_CAP);
            if (cap == null) return;

            // 1. ALWAYS LOCK HEALTH
            // The player uses the Limb System now. Vanilla health is just a buffer.
            if (player.getHealth() < player.getMaxHealth()) {
                player.setHealth(player.getMaxHealth());
            }

            // 2. Detect if the watch is in the inventory (only for HUD visibility)
            boolean hasWatch = false;
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (stack.is(HUDVisualsSubpack.SPORTS_WATCH.get())) {
                    hasWatch = true;
                    break;
                }
            }

            // Sync watch state to client so HUD knows when to show up
            if (cap.hasWatchEquipped() != hasWatch) {
                cap.setWatchEquipped(hasWatch);
                syncToClient(player, cap);
            }
        }
    }

    /**
     * Always distribute damage to limbs, regardless of watch status.
     */
    @SubscribeEvent
    public static void onPlayerDamage(LivingDamageEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        WristCapability cap = player.getData(ModAttachments.WRIST_CAP);
        if (cap == null) return;

        // Custom Limb Damage Logic - ALWAYS RUNS
        float amount = event.getNewDamage() * 0.5f;

        if (event.getSource().is(DamageTypes.FALL)) {
            cap.damageLeftFoot(amount * 0.4f);
            cap.damageRightFoot(amount * 0.4f);
            cap.damageLeftLeg(amount * 0.2f);
            cap.damageRightLeg(amount * 0.2f);
        } else if (event.getSource().is(DamageTypes.EXPLOSION)) {
            cap.damageTorso(amount * 0.6f);
            cap.damageLeftLeg(amount * 0.2f);
            cap.damageRightLeg(amount * 0.2f);
        } else {
            double hitY = RANDOM.nextDouble();
            if (hitY > 0.85) cap.damageHead(amount);
            else if (hitY > 0.45) cap.damageTorso(amount);
            else if (hitY > 0.25) {
                if (RANDOM.nextBoolean()) cap.damageLeftArm(amount);
                else cap.damageRightArm(amount);
            } else if (hitY > 0.10) {
                if (RANDOM.nextBoolean()) cap.damageLeftLeg(amount);
                else cap.damageRightLeg(amount);
            } else {
                if (RANDOM.nextBoolean()) cap.damageLeftFoot(amount);
                else cap.damageRightFoot(amount);
            }
        }

        // Sync to client so the data is ready whenever they put the watch on
        syncToClient(player, cap);
    }

    private static void syncToClient(ServerPlayer player, WristCapability cap) {
        ModMessages.sendToPlayer(new LimbSyncPacket(
                cap.getBPM(), cap.getEnergy(), cap.hasWatchEquipped(),
                cap.getHeadHealth(), cap.getTorsoHealth(),
                cap.getLeftArmHealth(), cap.getRightArmHealth(),
                cap.getLeftLegHealth(), cap.getRightLegHealth(),
                cap.getLeftFootHealth(), cap.getRightFootHealth()
        ), player);
    }
}