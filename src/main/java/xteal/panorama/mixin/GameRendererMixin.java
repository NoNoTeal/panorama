package xteal.panorama.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static xteal.panorama.Events.takePanorama;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(
            method = "shouldRenderBlockOutline",
            at = @At("HEAD"),
            cancellable = true
    )
    private void shouldRenderBlockOutline(CallbackInfoReturnable<Boolean> cir) {
        if(takePanorama) {
            cir.setReturnValue(false);
        } else {
            cir.setReturnValue(true);
        }
    }

    @Inject(
            method = "getFov",
            at = @At("RETURN"),
            cancellable = true
    )
    private void getFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        if(takePanorama) {
            cir.setReturnValue(90.0);
        }
    }
}
