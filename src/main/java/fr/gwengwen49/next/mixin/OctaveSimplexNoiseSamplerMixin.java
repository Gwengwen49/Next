package fr.gwengwen49.next.mixin;

import fr.gwengwen49.next.hooks.NoiseAccessor;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(OctaveSimplexNoiseSampler.class)
public class OctaveSimplexNoiseSamplerMixin implements NoiseAccessor {

    @Mutable
    @Shadow @Final private double persistence;

    @Mutable
    @Shadow @Final private double lacunarity;

    @Override
    public double next_getPersistence() {
        return this.persistence;
    }

    @Override
    public void next_setPersistence(double persistence) {
        this.persistence = persistence;
    }

    @Override
    public double next_getLacunarity() {
        return lacunarity;
    }

    @Override
    public void next_setLacunarity(double lacunarity) {
        this.lacunarity = lacunarity;
    }
}
