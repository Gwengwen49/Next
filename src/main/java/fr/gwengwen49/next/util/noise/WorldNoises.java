package fr.gwengwen49.next.util.noise;

import net.minecraft.util.math.noise.SimplexNoiseSampler;
import net.minecraft.util.math.random.Random;

public class WorldNoises {

    private static SimplexNoiseSampler WORLD_NOISE_SAMPLER;
    private static double SURFACE_NOISE;
    private static double MOUNTAIN_NOISE;
    private static double SPIKE_NOISE;
    public static void initialize(long seed){
        WORLD_NOISE_SAMPLER = new SimplexNoiseSampler(Random.create(seed));
    }

    public static SimplexNoiseSampler getWorldNoiseSampler() {
        return WORLD_NOISE_SAMPLER;
    }

    public static double getSurfaceNoise(double x, double z) {
        return (SURFACE_NOISE = WORLD_NOISE_SAMPLER.sample(x*0.025, z*0.025));
    }

    public static double getMountainNoise(double x, double z) {
        return (MOUNTAIN_NOISE = WORLD_NOISE_SAMPLER.sample(x*0.0025, z*0.0025));
    }

    public static double getSpikeNoise(double x, double z) {
        return (SPIKE_NOISE = WORLD_NOISE_SAMPLER.sample(x*0.025, z*0.025));
    }
}
