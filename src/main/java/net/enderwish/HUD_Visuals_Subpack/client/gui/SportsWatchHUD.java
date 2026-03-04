package net.enderwish.HUD_Visuals_Subpack.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.enderwish.HUD_Visuals_Subpack.HUDVisualsSubpack;
import net.enderwish.HUD_Visuals_Subpack.core.ModAttachments;
import net.enderwish.HUD_Visuals_Subpack.core.WristCapability;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * SPORTS WATCH HUD
 * Updated for Minecraft 1.21.1 / NeoForge (DeltaTracker support).
 */
public class SportsWatchHUD {

    private static final ResourceLocation LIMBS_TEXTURE = ResourceLocation.fromNamespaceAndPath(HUDVisualsSubpack.MOD_ID, "textures/gui/limbs.png");

    // HUD Dimensions
    private static final int BASE_WIDTH = 100;
    private static final int BASE_HEIGHT = 75;
    private static final int MARGIN = 10;

    /**
     * Updated to use DeltaTracker instead of float to match 1.21.1 requirements.
     */
    public static final LayeredDraw.Layer SPORTS_WATCH_ELEMENT = (graphics, deltaTracker) -> {
        render(graphics, deltaTracker);
    };

    public static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null || player.isSpectator()) return;

        WristCapability cap = player.getData(ModAttachments.WRIST_CAP);

        // Render condition: System is always active, but HUD only shows with Watch
        if (cap == null || !cap.hasWatchEquipped()) return;

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // --- 1. STATUS BARS (Bottom Center Area) ---
        int leftX = screenWidth / 2 - 91;
        int rightX = screenWidth / 2 + 10;
        int row1Y = screenHeight - 39;
        int row2Y = screenHeight - 51;

        // Energy Bar (Left)
        drawStatusBar(graphics, mc, leftX, row1Y, cap.getEnergy(), 0xFF00AAFF, "ENERGY");

        // Hunger Bar (Right)
        float foodLevel = (player.getFoodData().getFoodLevel() / 20.0f) * 100.0f;
        drawStatusBar(graphics, mc, rightX, row1Y, foodLevel, 0xFFFF9900, "HUNGER");

        // Thirst Bar (Right, Above Hunger)
        drawStatusBar(graphics, mc, rightX, row2Y, 100f, 0xFF00FFFF, "THIRST");

        // --- 2. WATCH FACE (Bottom Right Area) ---
        int watchX = screenWidth - (BASE_WIDTH * 2) - MARGIN;
        int watchY = screenHeight - (BASE_HEIGHT * 2) - MARGIN;

        graphics.pose().pushPose();
        graphics.pose().translate(watchX, watchY, 0);
        graphics.pose().scale(2.0F, 2.0F, 1.0F);

        // Limbs rendering using unique health percentages
        // Head
        drawLimb(graphics, 44, 5,  8,  8,  12, 12, cap.getHeadPct());
        // Torso
        drawLimb(graphics, 40, 18, 48, 12, 20, 26, cap.getTorsoPct());
        // Left Arm
        drawLimb(graphics, 27, 18, 84, 12, 12, 24, cap.getLArmPct());
        // Right Arm
        drawLimb(graphics, 61, 18, 4,  52, 12, 24, cap.getRArmPct());
        // Left Leg
        drawLimb(graphics, 40, 45, 44, 50, 10, 22, cap.getLLegPct());
        // Right Leg
        drawLimb(graphics, 50, 45, 84, 50, 10, 22, cap.getRLegPct());
        // Left Foot
        drawLimb(graphics, 39, 67, 5,  82, 11, 7,  cap.getLFootPct());
        // Right Foot
        drawLimb(graphics, 50, 67, 45, 82, 11, 7,  cap.getRFootPct());

        // Heart Rate Text
        graphics.drawString(mc.font, cap.getBPM() + " BPM", 5, 5, 0xFFFFFF, true);

        graphics.pose().popPose();

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }

    private static void drawStatusBar(GuiGraphics graphics, Minecraft mc, int x, int y, float percent, int color, String label) {
        int barWidth = 81;
        int barHeight = 7;
        graphics.fill(x, y, x + barWidth, y + barHeight, 0x44000000);
        int fillWidth = (int) ((percent / 100.0f) * (barWidth - 2));
        if (fillWidth > 0) {
            graphics.fill(x + 1, y + 1, x + 1 + fillWidth, y + barHeight - 1, color);
        }
        graphics.pose().pushPose();
        graphics.pose().scale(0.5f, 0.5f, 0.5f);
        graphics.drawString(mc.font, label, (x) * 2, (y - 6) * 2, color, true);
        graphics.pose().popPose();
    }

    private static void drawLimb(GuiGraphics graphics, int x, int y, int u, int v, int width, int height, float pct) {
        if (pct >= 0.75f) {
            RenderSystem.setShaderColor(0.2F, 1.0F, 0.2F, 1.0F); // Green
        } else if (pct >= 0.4f) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 0.0F, 1.0F); // Yellow
        } else {
            RenderSystem.setShaderColor(1.0F, 0.0F, 0.0F, 1.0F); // Red
        }
        graphics.blit(LIMBS_TEXTURE, x, y, u, v, width, height, 128, 128);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}