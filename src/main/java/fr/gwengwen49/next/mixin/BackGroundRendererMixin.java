package fr.gwengwen49.next.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.gwengwen49.next.Next;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public class BackGroundRendererMixin {

    @Shadow private static float red;

    @Shadow private static float green;

    @Shadow private static float blue;

    @Inject(at = @At("TAIL"), method = "render")
    private static void dontRenderIf(Camera camera, float tickDelta, ClientWorld world, int viewDistance, float skyDarkness, CallbackInfo ci) {

    }
}
