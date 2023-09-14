package fr.gwengwen49.next.client.render.entity;

import fr.gwengwen49.next.client.render.entity.model.MeteoreEntityModel;
import fr.gwengwen49.next.entity.MeteoreEntity;
import fr.gwengwen49.next.registry.ModEntityModelLayers;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class MeteoreEntityRenderer extends EntityRenderer<MeteoreEntity> {
    private static final Identifier TEXTURE = new Identifier("next", "textures/entity/end_portal.png");
    private final MeteoreEntityModel model;

    public MeteoreEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new MeteoreEntityModel(ctx.getPart(ModEntityModelLayers.METEORE));
    }

    @Override
    public void render(MeteoreEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.scale(-1.0f, -1.0f, 1.0f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90.0f));
        matrices.translate(0.0f, -1.5f, 0.0f);
        this.model.render(matrices, vertexConsumers.getBuffer(model.getLayer(TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(MeteoreEntity entity) {
        return TEXTURE;
    }
}
