package fr.gwengwen49.next.registry;

import fr.gwengwen49.next.client.render.entity.MeteoreEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ModEntityRenderers {

    public static void setup(){
        EntityRendererRegistry.register(ModEntityType.METEORE, MeteoreEntityRenderer::new);
    }
}
