package com.black_dog20.realbedo.mixin;

import com.black_dog20.realbedo.event.RenderTileEntityEvent;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityRendererDispatcher.class)
public class TileEntityRendererDispatcherMixin {

    @Inject(method = "render(Lnet/minecraft/client/renderer/tileentity/TileEntityRenderer;Lnet/minecraft/tileentity/TileEntity;FLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;)V", at = @At("RETURN"))
    private static <T extends TileEntity> void render(TileEntityRenderer<T> renderer, T tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, CallbackInfo ci) {
        RenderTileEntityEvent.postNewEvent(tileEntity);
    }
}
