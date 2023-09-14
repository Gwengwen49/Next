package fr.gwengwen49.next.world.gen.chunk;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.gwengwen49.next.Next;
import fr.gwengwen49.next.hooks.NoiseAccessor;
import fr.gwengwen49.next.registry.ModBlocks;
import fr.gwengwen49.next.util.noise.WorldNoises;
import fr.gwengwen49.next.util.noise.WorleyNoise;
import net.fabricmc.fabric.mixin.event.lifecycle.MinecraftServerMixin;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.SpectatorTeleportC2SPacket;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkRenderDistanceCenterS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.util.math.noise.SimplexNoiseSampler;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeCoords;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.feature.EndIslandFeature;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.structure.Structures;
import org.joml.SimplexNoise;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class OutsideWorldChunkGenerator extends ChunkGenerator {
    private final BlockState STONE = Blocks.STONE.getDefaultState();
    private final BlockState ROCK1 = Blocks.GILDED_BLACKSTONE.getDefaultState();
    private final BlockState ROCK2 = Blocks.BLACKSTONE.getDefaultState();
    private final BlockState ROCK3 = Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.getDefaultState();
    private final BlockState ROCK4 = Blocks.DEEPSLATE_TILES.getDefaultState();
    private final BlockState SAND1 = Blocks.RED_SAND.getDefaultState();
    private final BlockState SAND2 = Blocks.RED_SANDSTONE.getDefaultState();
    private final BlockState SAND3 = Blocks.SAND.getDefaultState();
    private final BlockState SAND4 = Blocks.SANDSTONE.getDefaultState();
    private final BlockState ICE1 = Blocks.GRASS_BLOCK.getDefaultState();
    private final BlockState ICE2 = Blocks.GRASS_BLOCK.getDefaultState();
    private final BlockState ICE3 = Blocks.DIRT_PATH.getDefaultState();
    private final BlockState ICE4 = Blocks.STONE.getDefaultState();
    private final int baseY = 80;

    public static final Codec<OutsideWorldChunkGenerator> CODEC = RecordCodecBuilder.create(outsideWorldChunkGeneratorInstance -> outsideWorldChunkGeneratorInstance.group(BiomeSource.CODEC.fieldOf("biome_source").forGetter(outsideWorldChunkGenerator -> outsideWorldChunkGenerator.biomeSource)).apply(outsideWorldChunkGeneratorInstance, OutsideWorldChunkGenerator::new));
    public OutsideWorldChunkGenerator(BiomeSource biomeSource) {
        super(biomeSource);
    }
    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public void carve(ChunkRegion chunkRegion, long seed, NoiseConfig noiseConfig, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk, GenerationStep.Carver carverStep) {
    }

    @Override
    public void buildSurface(ChunkRegion region, StructureAccessor structures, NoiseConfig noiseConfig, Chunk chunk) {
//        BlockPos.Mutable blockPos = new BlockPos.Mutable();
//        for (int chunkX = 0; chunkX < 16; chunkX++) {
//            for (int chunkZ = 0; chunkZ < 16; chunkZ++) {
//                double posX = chunk.getPos().getStartX() + chunkX;
//                double posZ = chunk.getPos().getStartZ() + chunkZ;
//                double surfaceNoise = WorldNoises.getSurfaceNoise(posX, posZ);
//                double mountainsNoise = WorldNoises.getMountainNoise(posX, posZ);
//                double spikesNoise = WorldNoises.getSpikeNoise(posX, posZ);
//                int noiseY = (int) (this.baseY + (surfaceNoise * 5));
//                int noiseY2 = (int) (noiseY + (mountainsNoise * 100));
//                int noiseY3 = (int) (noiseY + (spikesNoise * 70));
//                //generate surface
//                for (int y = 0; y < noiseY; y++) {
//                    switch (new java.util.Random().nextInt(4)) {
//                        case 0 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), ROCK1, false);
//                        case 1 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), ROCK2, false);
//                        case 2 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), ROCK3, false);
//                        case 3 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), ROCK4, false);
//                    }
//
//                }
//                //generate mountains
//                for (int y = noiseY; y < noiseY2; y++) {
//                    if (mountainsNoise <= 0.3) {
//                        switch (new java.util.Random().nextInt(4)) {
//                            case 0 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), ROCK1, false);
//                            case 1 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), ROCK2, false);
//                            case 2 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), ROCK3, false);
//                            case 3 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), ROCK4, false);
//                        }
//                    }
//
//
//                    if (mountainsNoise > 0.3 && mountainsNoise <= 0.35) {
//                        switch (new java.util.Random().nextInt(2)) {
//                            case 0 -> {
//                                switch (new java.util.Random().nextInt(4)) {
//                                    case 0 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), ROCK1, false);
//                                    case 1 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), ROCK2, false);
//                                    case 2 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), ROCK3, false);
//                                    case 3 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), ROCK4, false);
//                                }
//                            }
//                            case 1 -> {
//                                switch (new java.util.Random().nextInt(2)) {
//                                    case 0 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), SAND1, false);
//                                    case 1 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), SAND2, false);
//                                }
//                            }
//                        }
//                    }
//
//
//                    if (mountainsNoise > 0.35 && mountainsNoise <= 0.4) {
//                        switch (new java.util.Random().nextInt(4)) {
//                            case 0 -> {
//                                switch (new java.util.Random().nextInt(4)) {
//                                    case 0 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), ROCK1, false);
//                                    case 1 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), ROCK2, false);
//                                    case 2 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), ROCK3, false);
//                                    case 3 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), ROCK4, false);
//                                }
//                            }
//                            case 1, 2, 3 -> {
//                                switch (new java.util.Random().nextInt(2)) {
//                                    case 0 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), SAND1, false);
//                                    case 1 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), SAND2, false);
//                                }
//                            }
//                        }
//                    }
//
//
//                    if (mountainsNoise > 0.4 && mountainsNoise <= 0.5) {
//                        switch (new java.util.Random().nextInt(2)) {
//                            case 0 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), SAND1, false);
//                            case 1 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), SAND2, false);
//                        }
//                    }
//
//
//                    if (mountainsNoise > 0.5 && mountainsNoise <= 0.55) {
//                        switch (new java.util.Random().nextInt(2)) {
//                            case 0 -> {
//                                switch (new java.util.Random().nextInt(2)) {
//                                    case 0 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), SAND1, false);
//                                    case 1 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), SAND2, false);
//                                }
//                            }
//                            case 1 -> {
//                                switch (new java.util.Random().nextInt(2)) {
//                                    case 0 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), SAND3, false);
//                                    case 1 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), SAND4, false);
//                                }
//                            }
//                        }
//                    }
//
//
//                    if (mountainsNoise > 0.55 && mountainsNoise <= 0.6) {
//                        switch (new java.util.Random().nextInt(4)) {
//                            case 0 -> {
//                                switch (new java.util.Random().nextInt(2)) {
//                                    case 0 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), SAND1, false);
//                                    case 1 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), SAND2, false);
//                                }
//                            }
//                            case 1, 2, 3 -> {
//                                switch (new java.util.Random().nextInt(2)) {
//                                    case 0 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), SAND3, false);
//                                    case 1 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), SAND4, false);
//                                }
//                            }
//                        }
//                    }
//
//
//                    if (mountainsNoise > 0.6 && mountainsNoise <= 0.7) {
//                        switch (new java.util.Random().nextInt(2)) {
//                            case 0 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), SAND3, false);
//                            case 1 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), SAND4, false);
//                        }
//                    }
//
//
//                    if (mountainsNoise > 0.7 && mountainsNoise <= 0.75) {
//                        switch (new java.util.Random().nextInt(2)) {
//                            case 0 -> {
//                                switch (new java.util.Random().nextInt(2)) {
//                                    case 0 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), SAND3, false);
//                                    case 1 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), SAND4, false);
//                                }
//                            }
//                            case 1 -> {
//                                switch (new java.util.Random().nextInt(3)) {
//                                    case 0 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), ICE1, false);
//                                    case 1 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), ICE2, false);
//                                    case 2 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), ICE3, false);
//                                }
//                            }
//                        }
//                    }
//
//
//                    if (mountainsNoise > 0.75 && mountainsNoise <= 0.8) {
//                        switch (new java.util.Random().nextInt(4)) {
//                            case 0 -> {
//                                switch (new java.util.Random().nextInt(2)) {
//                                    case 0 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), SAND3, false);
//                                    case 1 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), SAND4, false);
//                                }
//                            }
//                            case 1, 2, 3 -> {
//                                switch (new java.util.Random().nextInt(4)) {
//                                    case 0 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), ICE1, false);
//                                    case 1 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), ICE2, false);
//                                    case 2 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), ICE3, false);
//                                    case 3 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), ICE4, false);
//                                }
//                            }
//                        }
//                    }
//
//
//                    if (mountainsNoise > 0.8) {
//                        switch (new java.util.Random().nextInt(4)) {
//                            case 0 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), ICE1, false);
//                            case 1 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), ICE2, false);
//                            case 2 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), ICE3, false);
//                            case 3 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), ICE4, false);
//                        }
//                    }
//                }
//                for (int y = noiseY - 25; y <= noiseY3 - 45; y++) {
//                    switch (new java.util.Random().nextInt(3)) {
//                        case 0 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), Blocks.POLISHED_GRANITE.getDefaultState(), false);
//                        case 1 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), Blocks.GRANITE.getDefaultState(), false);
//                        case 2 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), Blocks.DRIPSTONE_BLOCK.getDefaultState(), false);
//                    }
//                }
//                for (int y = (int) noiseY - 15; y < noiseY + 2; y++) {
//                    if (surfaceNoise < -0.3) {
//                        if (y <= noiseY - 2) {
//                            chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), Blocks.LAVA.getDefaultState(), false);
//                        } else if (y > noiseY - 2) {
//                            chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), Blocks.AIR.getDefaultState(), false);
//                        }
//                    }
//                    if (surfaceNoise >= -0.3 && surfaceNoise < -0.2) {
//                        switch (new java.util.Random().nextInt(4)) {
//                            case 0 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), Blocks.BASALT.getDefaultState(), false);
//                            case 1 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), Blocks.BLACKSTONE.getDefaultState(), false);
//                            case 2 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), Blocks.GILDED_BLACKSTONE.getDefaultState(), false);
//                            case 3 -> chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), Blocks.POLISHED_BLACKSTONE.getDefaultState(), false);
//                        }
//                    }
//                }
//            }
//        }
    }

    @Override
    public void populateEntities(ChunkRegion region) {
    }

    @Override
    public int getWorldHeight() {
        return 320;
    }

    @Override
    public CompletableFuture<Chunk> populateNoise(Executor executor, Blender blender, NoiseConfig noiseConfig, StructureAccessor structureAccessor, Chunk chunk) {
        BlockPos.Mutable blockPos = new BlockPos.Mutable();
        for(int chunkX = 0; chunkX < 16; chunkX++){
            for (int chunkZ = 0; chunkZ < 16; chunkZ++){
                double posX = chunk.getPos().getStartX()+chunkX;
                double posZ = chunk.getPos().getStartZ()+chunkZ;
                double surfaceNoise = WorldNoises.getSurfaceNoise(posX, posZ);
                double mountainsNoise = WorldNoises.getMountainNoise(posX, posZ);
                double spikesNoise = WorldNoises.getSpikeNoise(posX, posZ);
                int noiseY = (int) (this.baseY+(surfaceNoise*5));
                int noiseY2 = (int) (noiseY+(mountainsNoise*100));
                int noiseY3 = (int) (noiseY+(spikesNoise*70));
                //generate surface
                for(int y = 0; y < noiseY; y++){
                    chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), STONE, false);
                }
                //generate mountains
                for(int y = noiseY; y < noiseY2; y++){
                    chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), STONE, false);
                }
                //generate spikes
                for(int y = noiseY-25; y <= noiseY3-45; y++) {
                    chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), STONE, false);
                }
                //generate lava pools
                for(int y = (int) noiseY-15; y < noiseY+2; y++){
                    if(surfaceNoise < -0.3) {
                        if(y <= noiseY-2) {
                            chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), Blocks.LAVA.getDefaultState(), false);
                        }
                        else if(y > noiseY-2) {
                            chunk.setBlockState(blockPos.add(chunkX, y, chunkZ), Blocks.AIR.getDefaultState(), false);
                        }
                    }
                }
            }
        }
    return CompletableFuture.completedFuture(chunk);
    }

    @Override
    public int getSeaLevel() {
        return -60;
    }

    @Override
    public int getMinimumY() {
        return -64;
    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world, NoiseConfig noiseConfig) {
        return 384;
    }

    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world, NoiseConfig noiseConfig) {
        return new VerticalBlockSample(world.getBottomY(), new BlockState[]{ModBlocks.OUTSIDE_ROCK.getDefaultState()});
    }

    @Override
    public void getDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos) {

    }

}
