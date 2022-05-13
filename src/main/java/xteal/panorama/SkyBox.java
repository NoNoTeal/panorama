package xteal.panorama;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.CubeMapRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

public class SkyBox extends CubeMapRenderer {
    public static Identifier[] textures;
    private final Identifier panoramaDefault = new Identifier("textures/gui/title/background/panorama");
    private final Identifier[] faces = new Identifier[6];

    public SkyBox() {
        super(new Identifier("textures/gui/title/background/panorama"));
        for (int i = 0; i < 6; ++i) {
            this.faces[i] = new Identifier(panoramaDefault.getNamespace(), panoramaDefault.getPath() + "_" + i + ".png");
        }
    }

    @Override
    public void draw(MinecraftClient client, float x, float y, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        Matrix4f matrix4f = Matrix4f.viewboxMatrix(85.0, (float)client.getWindow().getFramebufferWidth() / (float)client.getWindow().getFramebufferHeight(), 0.05f, 10.0f);
        RenderSystem.backupProjectionMatrix();
        RenderSystem.setProjectionMatrix(matrix4f);
        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.push();
        matrixStack.loadIdentity();
        matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(180.0f));
        RenderSystem.applyModelViewMatrix();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        for (int j = 0; j < 4; ++j) {
            matrixStack.push();
            float f = ((float)(j % 2) / 2.0f - 0.5f) / 256.0f;
            float g = ((float)(j / 2) / 2.0f - 0.5f) / 256.0f;
            matrixStack.translate(f, g, 0.0);
            matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(x));
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(y));
            RenderSystem.applyModelViewMatrix();
            for (int k = 0; k < 6; ++k) {
                if(textures != null) {
                    RenderSystem.setShaderTexture(0, textures[k]);
                } else {
                    RenderSystem.setShaderTexture(0, faces[k]);
                }
                bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
                int l = Math.round(255.0f * alpha) / (j + 1);
                if (k == 0) {
                    bufferBuilder.vertex(-1.0, -1.0, 1.0).texture(0.0f, 0.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(-1.0, 1.0, 1.0).texture(0.0f, 1.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(1.0, 1.0, 1.0).texture(1.0f, 1.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(1.0, -1.0, 1.0).texture(1.0f, 0.0f).color(255, 255, 255, l).next();
                }
                if (k == 1) {
                    bufferBuilder.vertex(1.0, -1.0, 1.0).texture(0.0f, 0.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(1.0, 1.0, 1.0).texture(0.0f, 1.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(1.0, 1.0, -1.0).texture(1.0f, 1.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(1.0, -1.0, -1.0).texture(1.0f, 0.0f).color(255, 255, 255, l).next();
                }
                if (k == 2) {
                    bufferBuilder.vertex(1.0, -1.0, -1.0).texture(0.0f, 0.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(1.0, 1.0, -1.0).texture(0.0f, 1.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(-1.0, 1.0, -1.0).texture(1.0f, 1.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(-1.0, -1.0, -1.0).texture(1.0f, 0.0f).color(255, 255, 255, l).next();
                }
                if (k == 3) {
                    bufferBuilder.vertex(-1.0, -1.0, -1.0).texture(0.0f, 0.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(-1.0, 1.0, -1.0).texture(0.0f, 1.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(-1.0, 1.0, 1.0).texture(1.0f, 1.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(-1.0, -1.0, 1.0).texture(1.0f, 0.0f).color(255, 255, 255, l).next();
                }
                if (k == 4) {
                    bufferBuilder.vertex(-1.0, -1.0, -1.0).texture(0.0f, 0.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(-1.0, -1.0, 1.0).texture(0.0f, 1.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(1.0, -1.0, 1.0).texture(1.0f, 1.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(1.0, -1.0, -1.0).texture(1.0f, 0.0f).color(255, 255, 255, l).next();
                }
                if (k == 5) {
                    bufferBuilder.vertex(-1.0, 1.0, 1.0).texture(0.0f, 0.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(-1.0, 1.0, -1.0).texture(0.0f, 1.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(1.0, 1.0, -1.0).texture(1.0f, 1.0f).color(255, 255, 255, l).next();
                    bufferBuilder.vertex(1.0, 1.0, 1.0).texture(1.0f, 0.0f).color(255, 255, 255, l).next();
                }
                tessellator.draw();
            }
            matrixStack.pop();
            RenderSystem.applyModelViewMatrix();
            RenderSystem.colorMask(true, true, true, false);
        }
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.restoreProjectionMatrix();
        matrixStack.pop();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
    }
}
