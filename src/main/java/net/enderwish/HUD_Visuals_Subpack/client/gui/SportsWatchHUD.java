package net.enderwish.HUD_Visuals_Subpack.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.enderwish.HUD_Visuals_Subpack.HUDVisualsSubpack;
import net.enderwish.HUD_Visuals_Subpack.core.ModAttachments;
import net.enderwish.HUD_Visuals_Subpack.core.WristCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * SPORTS WATCH HUD
 * Updated with specific texture coordinates for all limbs including feet.
 */
public class SportsWatchHUD {

    private static final ResourceLocation LIMBS_TEXTURE = ResourceLocation.fromNamespaceAndPath(HUDVisualsSubpack.MOD_ID, "textures/gui/limbs.png");

    private static final int BASE_WIDTH = 100;
    private static final int BASE_HEIGHT = 75;
    private static final int MARGIN = 10;

    public static final LayeredDraw.Layer SPORTS_WATCH_ELEMENT = (graphics, delta) -> {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null || player.isSpectator()) return;

        WristCapability cap = player.getData(ModAttachments.WRIST_CAP);
        if (cap == null || !cap.hasWatchEquipped()) return;

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // --- POSITIONING CONSTANTS FOR BARS ---
        int leftX = screenWidth / 2 - 91;  // Energy (Vanilla Health pos)
        int rightX = screenWidth / 2 + 10; // Hunger/Thirst (Vanilla Hunger pos)
        int row1Y = screenHeight - 39;     // Bottom Row
        int row2Y = screenHeight - 51;     // Top Row (Thirst)

        // 1. STATUS BARS
        drawStatusBar(graphics, mc, leftX, row1Y, cap.getEnergy(), 0xFF00AAFF, "ENERGY");
        float foodLevel = (player.getFoodData().getFoodLevel() / 20.0f) * 100.0f;
        drawStatusBar(graphics, mc, rightX, row1Y, foodLevel, 0xFFFF9900, "HUNGER");
        drawStatusBar(graphics, mc, rightX, row2Y, 100f, 0xFF00FFFF, "THIRST");

        // --- WATCH FACE (Bottom Right, 2x Scale) ---
        int watchX = screenWidth - (BASE_WIDTH * 2) - MARGIN;
        int watchY = screenHeight - (BASE_HEIGHT * 2) - MARGIN;

        graphics.pose().pushPose();
        graphics.pose().translate(watchX, watchY, 0);
        graphics.pose().scale(2.0F, 2.0F, 1.0F);

        // Dark background for the watch face
        graphics.fill(0, 0, BASE_WIDTH, BASE_HEIGHT, 0x66000000);

        // 2. LIMBS (Using provided U/V texture coordinates)
        // drawLimb(graphics, screenX, screenY, texU, texV, width, height, percent)

        // Head
        drawLimb(graphics, 44, 5,  8,  8,  12, 12, cap.getHeadPct());
        // Torso
        drawLimb(graphics, 40, 18, 48, 12, 20, 26, cap.getTorsoPct());
        // Arms
        drawLimb(graphics, 28, 18, 84, 12, 12, 24, cap.getLArmPct());
        drawLimb(graphics, 60, 18, 4,  52, 12, 24, cap.getRArmPct());
        // Legs
        drawLimb(graphics, 40, 45, 44, 50, 10, 22, cap.getLLegPct());
        drawLimb(graphics, 50, 45, 84, 50, 10, 22, cap.getRLegPct());
        // Feet (New Coordinates)
        drawLimb(graphics, 38, 62, 5,  82, 11, 7,  cap.getLFootPct()); // Foot L (5, 82)
        drawLimb(graphics, 51, 62, 45, 82, 11, 7,  cap.getRFootPct()); // Foot R (45, 82)

        // BPM Text
        graphics.drawString(mc.font, cap.getBPM() + " BPM", 5, 5, 0xFFFFFF, true);

        graphics.pose().popPose();
        RenderSystem.disableBlend();
    };

    /**
     * Draws the rectangular status bars for Energy, Hunger, and Thirst.
     */
    private static void drawStatusBar(GuiGraphics graphics, Minecraft mc, int x, int y, float percent, int color, String label) {
        int barWidth = 81;
        int barHeight = 7;

        graphics.fill(x, y, x + barWidth, y + barHeight, 0xFF222222);

        int fillWidth = (int) ((percent / 100.0f) * (barWidth - 2));
        if (fillWidth > 0) {
            graphics.fill(x + 1, y + 1, x + 1 + fillWidth, y + barHeight - 1, color);
        }

        graphics.pose().pushPose();
        graphics.pose().scale(0.5f, 0.5f, 0.5f);
        graphics.drawString(mc.font, label, (x) * 2, (y - 6) * 2, color, true);
        graphics.pose().popPose();
    }

    /**
     * Draws a limb icon from the texture sheet with color-coding based on health.
     */
    private static void drawLimb(GuiGraphics graphics, int x, int y, int u, int v, int width, int height, float pct) {
        // Simple health-based coloring
        if (pct > 0.7f) RenderSystem.setShaderColor(0.3F, 1.0F, 0.3F, 1.0F);      // Green (Healthy)
        else if (pct > 0.3f) RenderSystem.setShaderColor(1.0F, 0.9F, 0.2F, 1.0F); // Yellow (Injured)
        else RenderSystem.setShaderColor(1.0F, 0.2F, 0.2F, 1.0F);                // Red (Critical)

        graphics.blit(LIMBS_TEXTURE, x, y, u, v, width, height, 128, 128);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F); // Reset color
    }
}