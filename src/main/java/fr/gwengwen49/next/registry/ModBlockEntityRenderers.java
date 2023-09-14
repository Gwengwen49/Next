package fr.gwengwen49.next.registry;

import fr.gwengwen49.next.client.render.blockentity.RiftPortalBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class ModBlockEntityRenderers {

    public static void setup(){
        BlockEntityRendererFactories.register(ModBlockEntityTypes.RIFT_PORTAL_BLOCK_ENTITY_TYPE, RiftPortalBlockEntityRenderer::new);

    }
}
