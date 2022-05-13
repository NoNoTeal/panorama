package xteal.panorama.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import xteal.panorama.Entry;
import xteal.panorama.util.Util;

public class PanoramaButton extends ButtonWidget {
    public static Identifier BORDER = new Identifier("panorama", "textures/gui/border.png");
    private final int textureWidth;
    private final int textureHeight;
    private Entry panorama;

    public PanoramaButton(Entry panorama, int x, int y) {
        super(x, y, 64, 64, Text.of(""), null);
        this.textureWidth = panorama.getIconWidth();
        this.textureHeight = panorama.getIconHeight();
        this.panorama = panorama;
        this.setMessage(Text.of(panorama.getPanoramaName()));
    }

    public void setPosition(int xIn, int yIn) {
        this.x = xIn;
        this.y = yIn;
    }

    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.setShaderTexture(0, this.panorama.getIcon());
        RenderSystem.enableDepthTest();
        drawTexture(matrixStack, this.x, this.y, 0.0F, 0.0F, 64, 64, 64, 64);
        RenderSystem.setShaderTexture(0, BORDER);
        drawTexture(matrixStack, this.x - 2, this.y - 2, 0.0F, 0.0F, 70, 70, 70, 70);
        if (this.isHovered() || this.isFocused()) {
            this.renderTooltip(matrixStack, mouseX, mouseY);
        }

    }

    public void onPress() {
        try {
            Util.loadPack(this.panorama.getPanoramaName());
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public Entry getPanorama() {
        return this.panorama;
    }
}
