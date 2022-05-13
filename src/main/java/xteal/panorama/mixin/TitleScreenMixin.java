package xteal.panorama.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xteal.panorama.Main;
import xteal.panorama.SkyBox;
import xteal.panorama.ui.GUIPanoramaSelector;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    @Mutable
    @Shadow @Final private RotatingCubeMapRenderer backgroundRenderer;

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(
            method = "initWidgetsNormal",
            at = @At("RETURN")
    )
    private void initWidgetsNormal(int y, int spacingY, CallbackInfo ci) {
        this.backgroundRenderer = Main.SKYBOX = new RotatingCubeMapRenderer(new SkyBox());
        this.addDrawableChild(new ButtonWidget(4, 4, 60, 20, Text.of("Panorama"), (button) -> MinecraftClient.getInstance().setScreen(new GUIPanoramaSelector())));
    }
}
