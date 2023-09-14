package fr.gwengwen49.next.world.gen.densityfunction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.SimplexNoiseSampler;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.densityfunction.DensityFunctions;

public class RiftNoise implements DensityFunction.Base {
    public static final CodecHolder<RiftNoise> CODEC_HOLDER = CodecHolder.of(MapCodec.unit(new RiftNoise(0L)));
    private static final float field_37677 = -0.9f;
    private final SimplexNoiseSampler sampler;

    public RiftNoise(long seed) {
        CheckedRandom random = new CheckedRandom(seed);
        random.skip(17292);
        this.sampler = new SimplexNoiseSampler(random);
    }

    public static float sample(SimplexNoiseSampler sampler, int x, int z) {
        int i = x / 2;
        int j = z / 2;
        int k = x % 2;
        int l = z % 2;
        float f = 100.0f - MathHelper.sqrt(x * x + z * z) * 8.0f;
        f = MathHelper.clamp(f, -100.0f, 80.0f);
        for (int m = -12; m <= 12; ++m) {
            for (int n = -12; n <= 12; ++n) {
                long o = i + m;
                long p = j + n;
                if (o * o + p * p <= 4096L || !(sampler.sample(o, p) < (double)-0.9f)) continue;
                float g = (MathHelper.abs(o) * 3439.0f + MathHelper.abs(p) * 147.0f) % 13.0f + 9.0f;
                float h = k - m * 2;
                float q = l - n * 2;
                float r = 100.0f - MathHelper.sqrt(h * h + q * q) * g;
                r = MathHelper.clamp(r, -100.0f, 80.0f);
                f = Math.max(f, r);
            }
        }
        return f;
    }

    @Override
    public double sample(DensityFunction.NoisePos pos) {
        return ((double) RiftNoise.sample(this.sampler, pos.blockX() / 16, pos.blockZ() / 16) - 8.0) / 118.0;
    }

    @Override
    public double minValue() {
        return -0.84375;
    }

    @Override
    public double maxValue() {
        return 0.5625;
    }

    @Override
    public CodecHolder<? extends DensityFunction> getCodecHolder() {
        return CODEC_HOLDER;
    }

}
