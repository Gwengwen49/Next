package fr.gwengwen49.next;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import fr.gwengwen49.next.network.packet.s2c.Packets;
import fr.gwengwen49.next.registry.*;
import fr.gwengwen49.next.server.command.ForceSkyEventCommand;
import fr.gwengwen49.next.sky.WorldExtension;
import fr.gwengwen49.next.util.noise.WorldNoises;
import fr.gwengwen49.next.world.biome.source.OutsideWorldBiomeSource;
import fr.gwengwen49.next.world.gen.chunk.OutsideWorldChunkGenerator;
import fr.gwengwen49.next.world.gen.densityfunction.RiftNoise;
import fr.gwengwen49.next.world.gen.feature.GlowMushroomFeature;
import fr.gwengwen49.next.world.rift.RiftPortal;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.NetherBiomes;
import net.fabricmc.fabric.api.block.v1.FabricBlock;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.DynamicRegistrySetupCallback;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.impl.client.registry.sync.FabricRegistryClientInit;
import net.fabricmc.fabric.impl.dimension.FabricDimensionInternals;
import net.fabricmc.fabric.mixin.client.rendering.WorldRendererMixin;
import net.fabricmc.fabric.mixin.command.CommandManagerMixin;
import net.fabricmc.fabric.mixin.registry.sync.RegistriesAccessor;
import net.fabricmc.fabric.mixin.registry.sync.RegistriesMixin;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdatePlayerAbilitiesC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.BuiltinBiomes;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

import java.util.EnumSet;

import static net.minecraft.registry.RegistryKeys.CONFIGURED_FEATURE;

public class Next implements ModInitializer {
    public static final TagKey<Biome> IS_NEXT_DIMENSION = TagKey.of(RegistryKeys.BIOME, new Identifier("next","is_next_dimension"));
    public static final RegistryKey<World> OUTSIDE_WORLD_DIMENSION_KEY = RegistryKey.of(RegistryKeys.WORLD, new Identifier("next", "outside_world"));
    public static final RegistryKey<Biome> MAGIC_FOREST_BIOME = RegistryKey.of(RegistryKeys.BIOME, new Identifier("next","magic_forest"));
    public static final RegistryKey<Biome> RIFT_EROSION = RegistryKey.of(RegistryKeys.BIOME, new Identifier("next","rift_erosion"));
    public static final GlowMushroomFeature GLOW_MUSHROOM_FEATURE = new GlowMushroomFeature(DefaultFeatureConfig.CODEC);
    public static final ConfiguredFeature<DefaultFeatureConfig, GlowMushroomFeature> GLOW_MUSHROOM_CONFIGURED_FEATURE = new ConfiguredFeature<>(GLOW_MUSHROOM_FEATURE, new DefaultFeatureConfig());

    @Override
    public void onInitialize() {
        WorldNoises.initialize(18751872158L);
        ModBlocks.init();
        ModItems.init();
        ModBlockEntityTypes.init();
        ModBlockEntityRenderers.setup();
        ModEntityType.setup();
        ModEntityModelLayers.setup();
        ModEntityRenderers.setup();
        Registry.register(Registries.BIOME_SOURCE, new Identifier("next", "outside_world"), OutsideWorldBiomeSource.CODEC);
        BiomeModifications.addFeature(biomeSelectionContext -> biomeSelectionContext.hasTag(BiomeTags.IS_NETHER), GenerationStep.Feature.UNDERGROUND_DECORATION, OrePlacedFeatures.ORE_EMERALD);
        ServerTickEvents.START_WORLD_TICK.register(WorldExtension::onTick);
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> ForceSkyEventCommand.register(dispatcher));
        ClientPlayNetworking.registerGlobalReceiver(Packets.SKY_EVENT_STARTED_ID, (client, handler, buf, responseSender) -> {
           if(client.world != null){
               Identifier identifier = buf.readIdentifier();
               if (new Identifier("next", "falling_stars").equals(identifier)) {
                   client.world.startFallingStarsEvent(buf.readBoolean());
               } else if (new Identifier("next", "supernovae").equals(identifier)) {
                   client.world.startNebulaEvent(buf.readBoolean());
               }
           }

        });
        Registry.register(Registries.FEATURE, new Identifier("next", "glow_mushroom"), GLOW_MUSHROOM_FEATURE);
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, new Identifier("next", "rift_noise"), RiftNoise.CODEC_HOLDER.codec());
        Registry.register(Registries.CHUNK_GENERATOR, new Identifier("next","outside_world_chunk_generator"), OutsideWorldChunkGenerator.CODEC);


    }

}
