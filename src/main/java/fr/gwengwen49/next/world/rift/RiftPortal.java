package fr.gwengwen49.next.world.rift;

import fr.gwengwen49.next.registry.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RiftPortal {




    public static void createRiftAt(World world, BlockPos pos){
        BlockState riftPortal = ModBlocks.RIFT_PORTAL_BLOCK.getDefaultState();
        BlockState portalBound = Blocks.MAGMA_BLOCK.getDefaultState();
        int size = 4;
        for(int x = -size; x <= size ; x++){
                for(int z = -size; z <= size ; z++){
                    BlockPos pos1 = new BlockPos(pos.getX()+x, pos.getY(), pos.getZ()+z);
                    if(world.getBlockState(pos1).isAir()){
                        if(x == size || z == size || x == -size || z == -size){
                            world.setBlockState(pos1, portalBound);
                        }
                        else {
                            world.setBlockState(pos1, riftPortal);
                        }
                    }
                }
        }
    }
}
