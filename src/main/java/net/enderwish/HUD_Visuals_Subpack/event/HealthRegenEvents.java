package net.enderwish.HUD_Visuals_Subpack.event;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;

public class HealthRegenEvents {

    @SubscribeEvent
    public void onLivingHeal(LivingHealEvent event) {
        if (event.getEntity() instanceof Player) {
            // FIX: In 1.21.1, you can usually just call setCanceled directly
            // if the event is cancellable (which LivingHealEvent is).
            event.setCanceled(true);

            // ALTERNATIVE: If cancellation is still picky,
            // setting the amount to 0 effectively stops the heal.
            // event.setAmount(0f);
        }
    }
}