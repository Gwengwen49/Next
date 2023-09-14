package fr.gwengwen49.next.blocks;

import fr.gwengwen49.next.Next;
import fr.gwengwen49.next.blocks.entity.RiftPortalBlockEntity;
import net.fabricmc.fabric.impl.dimension.FabricDimensionInternals;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class RiftPortalBlock extends BlockWithEntity {

    public RiftPortalBlock(Settings settings) {
        super(settings);
    }
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RiftPortalBlockEntity(pos, state);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
            if (world instanceof ServerWorld && entity.canUsePortals()) {
                RegistryKey<World> registryKey = Next.OUTSIDE_WORLD_DIMENSION_KEY;
                ServerWorld serverWorld = ((ServerWorld)world).getServer().getWorld(registryKey);
                if (serverWorld == null) {
                    return;
                }
                FabricDimensionInternals.changeDimension(entity, serverWorld, new TeleportTarget(new Vec3d((double)entity.getX() + 0.5, entity.getY(), (double)entity.getZ() + 0.5), entity.getVelocity(), entity.getYaw(), entity.getPitch()));
            }
    }
}
