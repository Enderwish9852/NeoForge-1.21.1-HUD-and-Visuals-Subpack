package net.enderwish.HUD_Visuals_Subpack.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.enderwish.HUD_Visuals_Subpack.HUDVisualsSubpack;
import net.enderwish.HUD_Visuals_Subpack.core.ModAttachments;
import net.enderwish.HUD_Visuals_Subpack.core.WristCapability;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * SPORTS WATCH HUD
 * Clean version: Removed debug mode and fixed shader reset for a stable UI.
 */
public class SportsWatchHUD {

    private static final ResourceLocation LIMBS_TEXTURE = ResourceLocation.fromNamespaceAndPath(HUDVisualsSubpack.MOD_ID, "textures/gui/limbs.png");

    private static final int WATCH_FACE_WIDTH = 100;
    private static final int WATCH_FACE_HEIGHT = 80;
    private static final int MARGIN = 10;
    private static final float SCALE = 2.0F;

    public static final LayeredDraw.Layer SPORTS_WATCH_ELEMENT = (graphics, deltaTracker) -> {
        render(graphics, deltaTracker);
    };

    public static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null || player.isSpectator()) return;

        WristCapability cap = player.getData(ModAttachments.WRIST_CAP);
        if (cap == null || !cap.hasWatchEquipped()) return;

        int sw = mc.getWindow().getGuiScaledWidth();
        int sh = mc.getWindow().getGuiScaledHeight();

        // 1. Render status bars (Hunger, Energy, etc.)
        renderStatusBars(graphics, mc, cap, player, sw, sh);

        // 2. Position and Scale the Watch Face (Paper Doll area)
        int watchX = sw - (int)(WATCH_FACE_WIDTH * SCALE) - MARGIN;
        int watchY = sh - (int)(WATCH_FACE_HEIGHT * SCALE) - MARGIN;

        graphics.pose().pushPose();
        graphics.pose().translate(watchX, watchY, 0);
        graphics.pose().scale(SCALE, SCALE, 1.0F);

        // Prepare Render State
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        // --- DRAW LIMBS (Adjust x and y here to position them manually) ---
        drawLimb(graphics, 44, 5, 8, 8, 12, 12, cap.getHeadPct());      // Head
        drawLimb(graphics, 40, 18, 48, 12, 20, 26, cap.getTorsoPct());  // Torso
        drawLimb(graphics, 27, 18, 84, 12, 12, 24, cap.getLArmPct());   // Left Arm
        drawLimb(graphics, 61, 18, 4, 52, 12, 24, cap.getRArmPct());    // Right Arm
        drawLimb(graphics, 40, 45, 44, 50, 10, 22, cap.getLLegPct());   // Left Leg
        drawLimb(graphics, 50, 45, 84, 50, 10, 22, cap.getRLegPct());   // Right Leg
        drawLimb(graphics, 39, 67, 5, 82, 11, 7, cap.getLFootPct());    // Left Foot
        drawLimb(graphics, 50, 67, 45, 82, 11, 7, cap.getRFootPct());   // Right Foot

        // Draw BPM Text
        graphics.drawString(mc.font, cap.getBPM() + " BPM", 5, 5, 0xFFFFFF, true);

        graphics.pose().popPose();

        // CRITICAL: Reset the global shader color to White.
        // This fixes the "Black Screen" issue by restoring standard rendering colors.
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static void renderStatusBars(GuiGraphics graphics, Minecraft mc, WristCapability cap, Player player, int sw, int sh) {
        int leftX = sw / 2 - 91;
        int rightX = sw / 2 + 10;
        int row1Y = sh - 39;
        int row2Y = sh - 51;

        drawStatusBar(graphics, mc, leftX, row1Y, cap.getEnergy(), 0xFF00AAFF, "ENERGY");
        float foodLevel = (player.getFoodData().getFoodLevel() / 20.0f) * 100.0f;
        drawStatusBar(graphics, mc, rightX, row1Y, foodLevel, 0xFFFF9900, "HUNGER");
        drawStatusBar(graphics, mc, rightX, row2Y, 100f, 0xFF00FFFF, "THIRST");

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
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
        // Dynamic coloring based on health
        if (pct >= 0.75f) RenderSystem.setShaderColor(0.2F, 1.0F, 0.2F, 1.0F); // Good
        else if (pct >= 0.4f) RenderSystem.setShaderColor(1.0F, 1.0F, 0.0F, 1.0F); // Wounded
        else RenderSystem.setShaderColor(1.0F, 0.0F, 0.0F, 1.0F); // Critical

        graphics.blit(LIMBS_TEXTURE, x, y, u, v, width, height, 128, 128);

        // Local reset to white
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}