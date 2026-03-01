package net.enderwish.HUD_Visuals_Subpack.core;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;

/**
 * Data class representing the health of individual limbs.
 * Uses Java Records for immutability and NeoForge Codecs for NBT serialization.
 */
public record LimbHealth(float head, float torso, float leftArm, float rightArm, float leftLeg, float rightLeg) {

    public static final float MAX_HEALTH = 20.0f;

    // Default state for a healthy player
    public static final LimbHealth FULL_HEALTH = new LimbHealth(MAX_HEALTH, MAX_HEALTH, MAX_HEALTH, MAX_HEALTH, MAX_HEALTH, MAX_HEALTH);

    // Codec for automatic NBT saving/loading
    public static final Codec<LimbHealth> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.FLOAT.fieldOf("head").forGetter(LimbHealth::head),
            Codec.FLOAT.fieldOf("torso").forGetter(LimbHealth::torso),
            Codec.FLOAT.fieldOf("leftArm").forGetter(LimbHealth::leftArm),
            Codec.FLOAT.fieldOf("rightArm").forGetter(LimbHealth::rightArm),
            Codec.FLOAT.fieldOf("leftLeg").forGetter(LimbHealth::leftLeg),
            Codec.FLOAT.fieldOf("rightLeg").forGetter(LimbHealth::rightLeg)
    ).apply(inst, LimbHealth::new));

    /**
     * Clamps values to ensure health stays within valid bounds (0 to MAX_HEALTH).
     */
    public LimbHealth {
        head = Mth.clamp(head, 0, MAX_HEALTH);
        torso = Mth.clamp(torso, 0, MAX_HEALTH);
        leftArm = Mth.clamp(leftArm, 0, MAX_HEALTH);
        rightArm = Mth.clamp(rightArm, 0, MAX_HEALTH);
        leftLeg = Mth.clamp(leftLeg, 0, MAX_HEALTH);
        rightLeg = Mth.clamp(rightLeg, 0, MAX_HEALTH);
    }

    // --- Helper Modification Methods ---

    public LimbHealth damageHead(float amount) { return new LimbHealth(head - amount, torso, leftArm, rightArm, leftLeg, rightLeg); }
    public LimbHealth damageTorso(float amount) { return new LimbHealth(head, torso - amount, leftArm, rightArm, leftLeg, rightLeg); }
    public LimbHealth damageLeftArm(float amount) { return new LimbHealth(head, torso, leftArm - amount, rightArm, leftLeg, rightLeg); }
    public LimbHealth damageRightArm(float amount) { return new LimbHealth(head, torso, leftArm, rightArm - amount, leftLeg, rightLeg); }
    public LimbHealth damageLeftLeg(float amount) { return new LimbHealth(head, torso, leftArm, rightArm, leftLeg - amount, rightLeg); }
    public LimbHealth damageRightLeg(float amount) { return new LimbHealth(head, torso, leftArm, rightArm, leftLeg, rightLeg - amount); }

    public LimbHealth healAll(float amount) {
        return new LimbHealth(head + amount, torso + amount, leftArm + amount, rightArm + amount, leftLeg + amount, rightLeg + amount);
    }

    // --- Utility Methods for Rendering ---

    public float getHeadPercent() { return head / MAX_HEALTH; }
    public float getTorsoPercent() { return torso / MAX_HEALTH; }
    public float getLeftArmPercent() { return leftArm / MAX_HEALTH; }
    public float getRightArmPercent() { return rightArm / MAX_HEALTH; }
    public float getLeftLegPercent() { return leftLeg / MAX_HEALTH; }
    public float getRightLegPercent() { return rightLeg / MAX_HEALTH; }

    /**
     * Checks if any limb is critically damaged (below 25% health)
     */
    public boolean hasCriticalLimb() {
        float threshold = MAX_HEALTH * 0.25f;
        return head <= threshold || torso <= threshold || leftArm <= threshold ||
                rightArm <= threshold || leftLeg <= threshold || rightLeg <= threshold;
    }
}