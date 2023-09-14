package fr.gwengwen49.next.registry;

import fr.gwengwen49.next.entity.MeteoreEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModEntityType {

public static EntityType<MeteoreEntity> METEORE;

public static void setup(){
    METEORE = (EntityType<MeteoreEntity>) register("meteore", EntityType.Builder.create(MeteoreEntity::new, SpawnGroup.AMBIENT).setDimensions(3.0f, 3.0f));
}

public static EntityType<?> register(String name, EntityType.Builder builder){
    return Registry.register(Registries.ENTITY_TYPE, new Identifier("next", name), builder.build(name));
}
}
