package xteal.panorama.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xteal.panorama.Events;

import static xteal.panorama.Events.*;

@Mixin(RenderTickCounter.class)
public abstract class RenderTickCounterMixin
{
    @Shadow
    private float lastFrameDuration;

    @Inject(method = "beginRenderTick",
            at = {@At(value = "FIELD",
            target = "Lnet/minecraft/client/render/RenderTickCounter;prevTimeMillis:J",
            opcode = Opcodes.PUTFIELD,
            ordinal = 0)}
    )
    public void beginRenderTick(long timeMillis, CallbackInfoReturnable<Integer> cir) {
        if(takePanorama) {
            MinecraftClient client = MinecraftClient.getInstance();
            if(index < 6) {
                Events.Facing f = Events.Facing.getIndex(index);
                client.player.setPosition(position);
                client.getCameraEntity().setYaw(f.yaw);
                client.getCameraEntity().setPitch(f.pitch);
                client.options.fov = 90.0;
                client.options.hudHidden = true;
            }
            Events.cameraEvent(MinecraftClient.getInstance());
            lastFrameDuration *= 0.1E-10;
        } else lastFrameDuration *= 1;
    }
}