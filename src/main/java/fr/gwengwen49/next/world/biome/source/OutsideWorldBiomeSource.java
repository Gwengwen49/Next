package fr.gwengwen49.next.world.biome.source;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.gwengwen49.next.Next;
import fr.gwengwen49.next.util.noise.WorldNoises;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.util.math.noise.SimplexNoiseSampler;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.BiomeCoords;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.gen.densityfunction.DensityFunction;

import java.util.stream.Stream;

public class OutsideWorldBiomeSource extends BiomeSource {
    public static final Codec<OutsideWorldBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(RegistryOps.getEntryCodec(BiomeKeys.THE_END), RegistryOps.getEntryCodec(BiomeKeys.SPARSE_JUNGLE), RegistryOps.getEntryCodec(BiomeKeys.END_MIDLANDS), RegistryOps.getEntryCodec(BiomeKeys.SMALL_END_ISLANDS), RegistryOps.getEntryCodec(BiomeKeys.END_BARRENS)).apply(instance, instance.stable(OutsideWorldBiomeSource::new)));
    private final RegistryEntry<Biome> centerBiome;
    private final RegistryEntry<Biome> highlandsBiome;
    private final RegistryEntry<Biome> midlandsBiome;
    private final RegistryEntry<Biome> smallIslandsBiome;
    private final RegistryEntry<Biome> barrensBiome;

    private OutsideWorldBiomeSource(RegistryEntry<Biome> centerBiome, RegistryEntry<Biome> highlandsBiome, RegistryEntry<Biome> midlandsBiome, RegistryEntry<Biome> smallIslandsBiome, RegistryEntry<Biome> barrensBiome) {
        this.centerBiome = centerBiome;
        this.highlandsBiome = highlandsBiome;
        this.midlandsBiome = midlandsBiome;
        this.smallIslandsBiome = smallIslandsBiome;
        this.barrensBiome = barrensBiome;
    }

    @Override
    protected Stream<RegistryEntry<Biome>> biomeStream() {
        return Stream.of(this.centerBiome, this.highlandsBiome, this.midlandsBiome, this.smallIslandsBiome, this.barrensBiome);
    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return CODEC;
    }

    @Override
    public RegistryEntry<Biome> getBiome(int x, int y, int z, MultiNoiseUtil.MultiNoiseSampler noise) {
        double posX = BiomeCoords.toBlock(x);
        double posZ = BiomeCoords.toBlock(z);
        double mountainsNoise = WorldNoises.getMountainNoise(posX, posZ);
        if(mountainsNoise <= 0.3) {
                return centerBiome;
            }


            if(mountainsNoise > 0.3 && mountainsNoise <= 0.35){
                return midlandsBiome;
            }


            if(mountainsNoise > 0.35 && mountainsNoise <= 0.4){
                return midlandsBiome;
            }


            if(mountainsNoise > 0.4 && mountainsNoise <= 0.5){
                return barrensBiome;
            }


            if(mountainsNoise > 0.5 && mountainsNoise <= 0.55){
                return barrensBiome;
            }


            if(mountainsNoise > 0.55 && mountainsNoise <= 0.6){
                return barrensBiome;
            }


            if(mountainsNoise > 0.6 && mountainsNoise <= 0.7){
                return barrensBiome;
            }


            if(mountainsNoise > 0.7 && mountainsNoise <= 0.75){
                return highlandsBiome;
            }


            if(mountainsNoise > 0.75 && mountainsNoise <= 0.8){
                return highlandsBiome;
            }


            if(mountainsNoise > 0.8) {
                return highlandsBiome;
            }

            return centerBiome;
    }
}
