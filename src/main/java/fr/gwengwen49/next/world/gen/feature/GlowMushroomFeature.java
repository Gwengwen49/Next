package fr.gwengwen49.next.world.gen.feature;

import com.mojang.serialization.Codec;
import fr.gwengwen49.next.registry.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.densityfunction.DensityFunctions;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.noise.BuiltinNoiseParameters;
import net.minecraft.world.gen.noise.NoiseParametersKeys;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import org.lwjgl.system.Pointer;

public class GlowMushroomFeature extends Feature<DefaultFeatureConfig> {


    public GlowMushroomFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }
    public int getTrunkHeight(Random random) {
        return random.nextBetween(3, 8);
    }
    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        BlockPos.Mutable pos = context.getOrigin().mutableCopy();
        StructureWorldAccess world = context.getWorld();
        if(world.getBlockState(pos.down()).isOf(Blocks.GRASS_BLOCK)) {
           int height = this.getTrunkHeight(context.getRandom());
           for(int i = 0; i < height; i++){
               if(world.getBlockState(pos.add(0, i, 0)).isOpaqueFullCube(world, pos.add(0, i, 0))) continue;
               world.setBlockState(pos.add(0, i, 0), ModBlocks.GLOW_MUSHROOM.getDefaultState(), 0);
           }
           for(int y = -1; y <= 0; y++){
               for(int x = -2; x <= 2; x++){
                   for(int z = -2; z <= 2; z++){
                       if(x == 2 || x == -2 || z == 2 || z == -2 ){
                           world.setBlockState(pos.add(x, height+y, z), ModBlocks.GLOW_MUSHROOM.getDefaultState(), 0);
                       }
                       if(y == 0){
                            world.setBlockState(pos.add(x, y+1, z), ModBlocks.GLOW_MUSHROOM.getDefaultState(), 0);
                       }
                   }
               }
           }
            return true;
    }
        return false;
    }


}
