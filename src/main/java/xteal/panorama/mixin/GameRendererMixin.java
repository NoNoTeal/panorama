package xteal.panorama.mixin;

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
}
