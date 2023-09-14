package fr.gwengwen49.next.registry;


import fr.gwengwen49.next.client.render.entity.model.MeteoreEntityModel;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

public class ModEntityModelLayers {

    public static EntityModelLayer METEORE = new EntityModelLayer(new Identifier("next", "main"), "main");

    public static void setup(){
        EntityModelLayerRegistry.registerModelLayer(METEORE, MeteoreEntityModel::getTexturedModelData);
    }

}
