package fr.gwengwen49.next.client;

import fr.gwengwen49.next.Next;
import fr.gwengwen49.next.client.render.OutsideWorldDimensionEffects;
import fr.gwengwen49.next.client.render.clouds.OutsideWorldCloudsRenderer;
import fr.gwengwen49.next.client.render.sky.OutsideWorldSkyRenderer;
import fr.gwengwen49.next.client.render.weather.OutsideWorldWeatherRenderer;
import fr.gwengwen49.next.registry.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;
import net.fabricmc.fabric.mixin.biome.BiomeSourceMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.network.packet.c2s.play.UpdatePlayerAbilitiesC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import org.apache.commons.compress.archivers.sevenz.CLI;
import org.lwjgl.glfw.GLFW;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class DimensionModClient implements ClientModInitializer {
    KeyBinding flyingKey = new KeyBinding("keys.flying", GLFW.GLFW_KEY_U, KeyBinding.GAMEPLAY_CATEGORY);
       @Override
    public void onInitializeClient() {
           DimensionRenderingRegistry.registerSkyRenderer(Next.OUTSIDE_WORLD_DIMENSION_KEY, new OutsideWorldSkyRenderer());
           DimensionRenderingRegistry.registerWeatherRenderer(Next.OUTSIDE_WORLD_DIMENSION_KEY, new OutsideWorldWeatherRenderer());
           DimensionRenderingRegistry.registerCloudRenderer(Next.OUTSIDE_WORLD_DIMENSION_KEY, new OutsideWorldCloudsRenderer());
           DimensionRenderingRegistry.registerDimensionEffects(new Identifier("next", "outside_world"), new OutsideWorldDimensionEffects());
           BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.STRANGE_GRASS_BLOCK, RenderLayer.getCutoutMipped());
           KeyBindingHelper.registerKeyBinding(flyingKey);
           while (flyingKey.wasPressed()){
            }
       }
}
