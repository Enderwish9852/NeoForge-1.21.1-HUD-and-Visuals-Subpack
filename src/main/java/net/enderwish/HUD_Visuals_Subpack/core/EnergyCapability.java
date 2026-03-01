package net.enderwish.HUD_Visuals_Subpack.core;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;

public class EnergyCapability {
    // Standardizing on 100.0 for easy percentage math (100%)
    public static final float MAX_ENERGY = 100.0f;
    private float energy;

    public static final Codec<EnergyCapability> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.FLOAT.fieldOf("energy").forGetter(EnergyCapability::getEnergy)
    ).apply(inst, EnergyCapability::new));

    public EnergyCapability() {
        this.energy = MAX_ENERGY;
    }

    public EnergyCapability(float energy) {
        this.energy = energy;
    }

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float value) {
        this.energy = Mth.clamp(value, 0.0f, MAX_ENERGY);
    }

    public void consume(float amount) {
        this.setEnergy(this.energy - amount);
    }

    public void recover(float amount) {
        this.setEnergy(this.energy + amount);
    }
}