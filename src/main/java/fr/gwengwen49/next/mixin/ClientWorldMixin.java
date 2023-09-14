package fr.gwengwen49.next.mixin;

import fr.gwengwen49.next.Next;
import fr.gwengwen49.next.client.biome.ModBiomeGrassColorModifier;
import fr.gwengwen49.next.hooks.NextWeatherEventHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.mixin.client.rendering.InGameHudMixin;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.BiomeColorCache;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.biome.ColorResolver;
import net.minecraft.world.gen.feature.ChorusPlantFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BooleanSupplier;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin implements NextWeatherEventHandler {

    @Shadow public abstract int calculateColor(BlockPos pos, ColorResolver colorResolver);

    @Shadow @Final private ClientPlayNetworkHandler networkHandler;
    private boolean fallingStarsEvent;
    private boolean nebulaEvent;
    private final int eventChance = 1000;

    public ClientWorldMixin(){
        this.fallingStarsEvent = false;
        this.nebulaEvent = false;
    }
    @Inject(at = @At("HEAD"), method = "tick")
    public void next_onWorldTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci){
        this.weatherTick();
    }

    @Override
    public boolean isFallingStarsEvent() {
        return fallingStarsEvent;
    }

    @Override
    public boolean isNebulaEvent() {
        return nebulaEvent;
    }

    @Override
    public void weatherTick() {
        if(Random.create().nextBetween(0, eventChance) == 1){
            startFallingStarsEvent(true);

        }
    }

    @Override
    public void startFallingStarsEvent(boolean value) {
        this.fallingStarsEvent = value;
    }

    @Override
    public void startNebulaEvent(boolean value) {
       this.nebulaEvent = value;
    }

    @Inject(at = @At("HEAD"), method = "getColor", cancellable = true)
    public void addCustomBiomeColor(BlockPos pos, ColorResolver colorResolver, CallbackInfoReturnable<Integer> cir){
        if(networkHandler.getWorld().getBiome(pos).isIn(Next.IS_NEXT_DIMENSION)) {
            BiomeColorCache biomeColorCache = new BiomeColorCache(blockPos -> this.calculateColor(blockPos, (biome, x, z) -> ModBiomeGrassColorModifier.Type.MAGIC_FOREST.getColor((int) x, (int) z)));
            cir.setReturnValue(biomeColorCache.getBiomeColor(pos));
        }
    }
}
