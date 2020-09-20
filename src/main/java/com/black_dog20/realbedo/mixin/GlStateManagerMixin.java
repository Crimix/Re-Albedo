package com.black_dog20.realbedo.mixin;

import com.black_dog20.realbedo.util.RenderUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GlStateManager.class)
public class GlStateManagerMixin {

    @Inject(method = "enableLighting()V", at = @At("RETURN"))
    private static void enable(CallbackInfo ci) {
        RenderUtil.enableLightingUniforms();
    }

    @Inject(method = "disableLighting()V", at = @At("RETURN"))
    private static void disable(CallbackInfo ci) {
        RenderUtil.disableLightingUniforms();
    }
}
