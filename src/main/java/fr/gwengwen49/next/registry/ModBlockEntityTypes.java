package fr.gwengwen49.next.registry;

import com.mojang.datafixers.types.Type;
import fr.gwengwen49.next.blocks.entity.RiftPortalBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class ModBlockEntityTypes {

    public static BlockEntityType<RiftPortalBlockEntity> RIFT_PORTAL_BLOCK_ENTITY_TYPE;

    public static void init(){
        RIFT_PORTAL_BLOCK_ENTITY_TYPE = register("rift_portal", BlockEntityType.Builder.create(RiftPortalBlockEntity::new, ModBlocks.RIFT_PORTAL_BLOCK));
    }

    public static<T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType.Builder<?> blockEntityType){
        Type<?> type = Util.getChoiceType(TypeReferences.BLOCK_ENTITY, name);
        return (BlockEntityType<T>) Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier("next", name), blockEntityType.build(type));
    }
}
