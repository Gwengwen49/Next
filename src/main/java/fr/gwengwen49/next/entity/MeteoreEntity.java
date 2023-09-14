package fr.gwengwen49.next.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.SlimeBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class MeteoreEntity extends ProjectileEntity {

    public MeteoreEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.setVelocity(Vec3d.ZERO);
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    @Override
    public boolean isCollidable() {
        return true;
    }


    @Override
    public boolean collidesWith(Entity other) {
        other.setVelocity(this.getVelocity());
        return super.collidesWith(other);
    }

    @Override
    public void tick() {
        this.setVelocity(this.getVelocity().add(0.0, -0.01, 0.0));
        this.move(MovementType.SELF, this.getVelocity());
        this.setVelocity(this.getVelocity().multiply(0.99f));
        super.tick();
    }

    @Override
    protected Box calculateBoundingBox() {
        return super.calculateBoundingBox();
    }
}
