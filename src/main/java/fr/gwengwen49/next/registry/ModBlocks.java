package fr.gwengwen49.next.registry;

import fr.gwengwen49.next.blocks.RiftPortalBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MushroomBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static Block GLOW_MUSHROOM = new Block(FabricBlockSettings.create());
    public static Block RIFT_PORTAL_BLOCK = new RiftPortalBlock(FabricBlockSettings.create().noCollision());
    public static Block STRANGE_GRASS_BLOCK = new Block(FabricBlockSettings.create());
    public static Block ISOTITE_ORE_BLOCK = new Block(FabricBlockSettings.create());
    public static Block OUTSIDE_ROCK = new Block(FabricBlockSettings.create());
    public static void init(){
        register("rift_portal", RIFT_PORTAL_BLOCK);
        register("strange_grass_block", STRANGE_GRASS_BLOCK);
        register("isotite_ore", ISOTITE_ORE_BLOCK);
        register("outside_rock", OUTSIDE_ROCK);
        register("glow_mushroom", GLOW_MUSHROOM);
    }

    public static void register(String name, Block block){
        Registry.register(Registries.BLOCK, new Identifier("next", name), block);
    }
}
