package fr.gwengwen49.next.client.render;

import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class OutsideWorldDimensionEffects extends DimensionEffects {
    public OutsideWorldDimensionEffects() {
        super(192.0F, true, DimensionEffects.SkyType.NORMAL, false, false);
    }

    @Override
    public Vec3d adjustFogColor(Vec3d color, float sunHeight) {
        return new Vec3d(0.0f, 0.0f, 0.20f);
    }

    @Override
    public boolean useThickFog(int camX, int camY) {
        return false;
    }

    @Nullable
    @Override
    public float[] getFogColorOverride(float skyAngle, float tickDelta) {
        return null;
    }



}
