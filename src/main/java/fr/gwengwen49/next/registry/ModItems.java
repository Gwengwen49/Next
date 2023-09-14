package fr.gwengwen49.next.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static Item RIFT_PORTAL_ITEM = new BlockItem(ModBlocks.RIFT_PORTAL_BLOCK, new FabricItemSettings());
    public static Item STRANGE_GRASS_BLOCK = new BlockItem(ModBlocks.STRANGE_GRASS_BLOCK, new FabricItemSettings());
    public static Item ISOTITE_ORE = new BlockItem(ModBlocks.ISOTITE_ORE_BLOCK, new FabricItemSettings());
    public static Item OUTSIDE_ROCK = new BlockItem(ModBlocks.OUTSIDE_ROCK, new FabricItemSettings());
    public static void init(){
        register("rift_portal", RIFT_PORTAL_ITEM);
        register("strange_grass_block", STRANGE_GRASS_BLOCK);
        register("isotite_ore", ISOTITE_ORE);
        register("outside_rock", OUTSIDE_ROCK);
    }

    public static void register(String name, Item item){
        Registry.register(Registries.ITEM, new Identifier("next", name), item);

    }

}
