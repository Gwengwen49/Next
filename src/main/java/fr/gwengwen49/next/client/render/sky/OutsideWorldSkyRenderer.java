package fr.gwengwen49.next.client.render.sky;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.SimplexNoise;

public class OutsideWorldSkyRenderer implements DimensionRenderingRegistry.SkyRenderer {
    private float rotation = 0.0f;
    private static final Identifier NEBULA = new Identifier("next","textures/environment/nebula.png");
    private static final Identifier METEORES = new Identifier("next","textures/environment/meteore.png");
    private static final Identifier SUN = new Identifier("textures/environment/sun.png");
    private float transparency = 0.2f;
    private float supernovaeSize = 1.0f;
    private boolean rise1;
    private boolean rise;
    private ClientWorld world;
    private MatrixStack matrices;
    private BufferBuilder bufferBuilder;
    private Matrix4f projectionMatrix;
    private final int fallingStarsNumber = 40;
    private float tickDelta;
    private int [][] starsPositions;
    private int[] supernovaePosition;
    @Nullable
    private VertexBuffer lightSkyBuffer;
    @Nullable
    private VertexBuffer darkSkyBuffer;

    public OutsideWorldSkyRenderer(){
        rise = true;
        rise1 = true;
    }

    public void setupFallingStarsPositions(Random random){
         this.starsPositions = new int[this.fallingStarsNumber][2];
        for(int i = 0; i < this.fallingStarsNumber; i++){
            this.starsPositions[i] = new int[]{random.nextBetween(0, 300), random.nextBetween(0, 300)};
        }
    }

    public void setupSupernovaePosition(Random random){
        this.supernovaePosition = new int[2];
        this.supernovaePosition[0] = random.nextBetween(0, 100);
        this.supernovaePosition[1] = random.nextBetween(0, 100);
    }
    @Override
    public void render(WorldRenderContext context) {
        if(this.starsPositions == null){
            this.setupFallingStarsPositions(context.world().random);
        }
        if (this.supernovaePosition == null){
            this.setupSupernovaePosition(context.world().random);
        }
        this.matrices = context.matrixStack();
        Camera camera = context.camera();
        GameRenderer gameRenderer = context.gameRenderer();
        this.world = context.world();
        this.projectionMatrix = context.projectionMatrix();
        this.tickDelta = context.tickDelta();
        if(lightSkyBuffer == null || darkSkyBuffer == null){
            this.renderLightSky();
            this.renderDarkSky();
        }
        CameraSubmersionType cameraSubmersionType = camera.getSubmersionType();
        if (cameraSubmersionType == CameraSubmersionType.POWDER_SNOW || cameraSubmersionType == CameraSubmersionType.LAVA) {
            return;
        }
        if(this.transparency < 1.0f && rise){
            transparency+=0.001f;
        }
        if(this.transparency >= 1.0f){
            rise = false;
        }
        if(this.transparency > 0.0f && !rise) {
            transparency-=0.001f;
        }
        if(this.transparency <= 0.0f && !rise) {
            rise = true;
        }

        if(this.supernovaeSize > 300.0f && rise1){
            supernovaeSize-=0.01f;
        }
        if(this.supernovaeSize <= 10.0f){
            rise1 = false;
        }
        if(this.supernovaeSize < 300.0f && !rise1) {
            supernovaeSize+=0.05f;
        }
        if(this.supernovaeSize >= 300.0f && !rise1) {
            rise1 = true;
        }
        this.bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.depthMask(false);
        RenderSystem.setShaderColor(0.0f, 0.0f, 0.24f, 1.0f);
        ShaderProgram shaderProgram = RenderSystem.getShader();
        this.lightSkyBuffer.bind();
        this.lightSkyBuffer.draw(matrices.peek().getPositionMatrix(), projectionMatrix, shaderProgram);
        VertexBuffer.unbind();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        this.drawFallingStars();
        this.drawSun();
        this.drawStars();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.depthMask(true);
    }
    private void renderDarkSky() {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        if (this.darkSkyBuffer != null) {
            this.darkSkyBuffer.close();
        }

        this.darkSkyBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        BufferBuilder.BuiltBuffer builtBuffer = renderSky(bufferBuilder, -16.0F);
        this.darkSkyBuffer.bind();
        this.darkSkyBuffer.upload(builtBuffer);
        VertexBuffer.unbind();
    }

    private static BufferBuilder.BuiltBuffer renderSky(BufferBuilder builder, float f) {
        float g = Math.signum(f) * 512.0f;
        float h = 512.0f;
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        builder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION);
        builder.vertex(0.0, f, 0.0).next();
        for (int i = -180; i <= 180; i += 45) {
            builder.vertex(g * MathHelper.cos((float)i * ((float)Math.PI / 180)), f, 512.0f * MathHelper.sin((float)i * ((float)Math.PI / 180))).next();
        }
        return builder.end();
    }

    private void renderLightSky() {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        if (this.lightSkyBuffer != null) {
            this.lightSkyBuffer.close();
        }

        this.lightSkyBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        BufferBuilder.BuiltBuffer builtBuffer = renderSky(bufferBuilder, 16.0F);
        this.lightSkyBuffer.bind();
        this.lightSkyBuffer.upload(builtBuffer);
        VertexBuffer.unbind();
    }
    public void drawStars(){
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((world.getSkyAngle(tickDelta) * 360.0f)*1.5f));
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        Matrix4f matrix4f3 = matrices.peek().getPositionMatrix();
        Random random = Random.create(10842L);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        for(int i1 = 0; i1 < 4000; ++i1) {
            double d = (double) (random.nextFloat() * 2.0F - 1.0F);
            double e = (double) (random.nextFloat() * 2.0F - 1.0F);
            double f = (double) (random.nextFloat() * 2.0F - 1.0F);
            double g = (double) (0.15F + random.nextFloat() * 0.1F);
            double h = d * d + e * e + f * f;
            if (h < 1.0D && h > 0.01D) {
                h = 1.0D / Math.sqrt(h);
                d *= h;
                e *= h;
                f *= h;
                double j = d * 80.0D + random.nextBetween(0, 20);
                double k1 = e * 80.0D + random.nextBetween(0, 20);
                double l = f * 80.0D + random.nextBetween(0, 20);
                double m = Math.atan2(d, f);
                double n = Math.sin(m);
                double o = Math.cos(m);
                double p = Math.atan2(Math.sqrt(d * d + f * f), e);
                double q = Math.sin(p);
                double r = Math.cos(p);
                double s = random.nextDouble() * 3.141592653589793D * 2.0D;
                double t = Math.sin(s);
                double u = Math.cos(s);

                int z = random.nextBetween(1, 255);
                float b = ((float) z) / 100;
                for (int v = 0; v < 4; ++v) {
                    double x = (double) ((v & 2) - 1) * g;
                    double y1 = (double) ((v + 1 & 2) - 1) * g;
                    double aa = x * u - y1 * t;
                    double ab = y1 * u + x * t;
                    double ad = aa * q + 0.0D * r;
                    double ae = 0.0D * q - aa * r;
                    double af = ae * n - ab * o;
                    double ah = ab * n + ae * o;
                    bufferBuilder.vertex(matrix4f3, (float) (j + af), (float) (k1 + ad), (float) (l + ah)).color(2.55f, 0.0f, b, (float) random.nextBetween(2, 10)/10).next();
                }
            }

        }
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        matrices.pop();
    }

    public void drawSun(){
        matrices.push();
        RenderSystem.setShaderColor(2.55f, 0.66f, 0.0f, 1.0f);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((world.getSkyAngle(tickDelta) * 360.0f)));
        Matrix4f matrix4f2 = matrices.peek().getPositionMatrix();
        float k = 20.0f;
        float y = 50.0f;
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, SUN);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f2, -k, y, -k).texture(0.0f, 0.0f).next();
        bufferBuilder.vertex(matrix4f2, k, y, -k).texture(1.0f, 0.0f).next();
        bufferBuilder.vertex(matrix4f2, k, y, k).texture(1.0f, 1.0f).next();
        bufferBuilder.vertex(matrix4f2, -k, y, k).texture(0.0f, 1.0f).next();
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.setShaderColor(2.55f, 0.57f, 0.0f, transparency);
        matrices.pop();
    }

    public void drawFallingStars(){
        matrices.push();
        if(world.isFallingStarsEvent()) {
            for (int c = 1; c < this.fallingStarsNumber; c++) {
                int starPosX = this.starsPositions[c][0];
                int starPosY = this.starsPositions[c][1];
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, transparency);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(world.getSkyAngle(tickDelta) * 360.0f * 3.0f));
                matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees((world.getSkyAngle(tickDelta) * 360.0f) * 3.0f));
                Matrix4f matrix4f2 = matrices.peek().getPositionMatrix();
                float k = 3.0f;
                float y = 300.0f;
                RenderSystem.setShader(GameRenderer::getPositionTexProgram);
                RenderSystem.setShaderTexture(0, METEORES);
                bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
                bufferBuilder.vertex(matrix4f2, -k + starPosX, y, -k + starPosY).texture(0.0f, 0.0f).next();
                bufferBuilder.vertex(matrix4f2, k + starPosX, y, -k + starPosY).texture(1.0f, 0.0f).next();
                bufferBuilder.vertex(matrix4f2, k + starPosX, y, k + starPosY).texture(1.0f, 1.0f).next();
                bufferBuilder.vertex(matrix4f2, -k + starPosX, y, k + starPosY).texture(0.0f, 1.0f).next();
                BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
            }
        }
        matrices.pop();
    }

}
