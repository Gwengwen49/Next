package fr.gwengwen49.next.client.biome;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.util.Function16;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.client.render.entity.animation.SnifferAnimations;
import net.minecraft.client.render.entity.animation.WardenAnimations;
import net.minecraft.command.argument.EnumArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.world.biome.Biome;

import java.awt.*;


public class ModBiomeGrassColorModifier {
    public static final Codec<ModBiomeGrassColorModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(Type.CODEC.optionalFieldOf("modded_grass_color_modifier", Type.DUMMY).forGetter(modBiomeGrassColorModifier -> modBiomeGrassColorModifier.type)).apply(instance, ModBiomeGrassColorModifier::new));
    private final Type type;
    public  ModBiomeGrassColorModifier(Type type){
        this.type = type;
    }
    public Type getType() {
        return type;
    }

    public enum Type implements StringIdentifiable {
        MAGIC_FOREST("magic_forest"){

            @Override
            public int getColor(int x, int z) {
                double d = new OctaveSimplexNoiseSampler(new ChunkRandom(new CheckedRandom(25630L)), ImmutableList.of(0)).sample(x*0.0125f, z*0.0125f, false);
                if(d < -0.1){
                    return 4882190;
                }
                if(d > 0.5){
                    return 5996288;
                }
                else {
                    return 2064142;
                }
            }
        },
        DUMMY("dummy") {
            @Override
            public int getColor(int x, int z) {
                return x*z;
            }
        };

        Type(String name){
            this.name = name;
        }

        public abstract int getColor(int x, int z);
        @Override
        public String asString() {
            return name;
        }
        private final String name;
        public static final com.mojang.serialization.Codec<Type> CODEC = StringIdentifiable.createCodec(Type::values);
    }

}
