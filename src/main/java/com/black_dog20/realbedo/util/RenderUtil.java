package com.black_dog20.realbedo.util;

import com.black_dog20.realbedo.Config;
import com.black_dog20.realbedo.EventManager;
import com.black_dog20.realbedo.event.RenderChunkUniformsEvent;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;

public class RenderUtil {
    public static boolean lightingEnabled = false;
    public static ShaderManager previousShader;
    public static boolean enabledLast = false;
    public static ItemCameraTransforms.TransformType itemTransformType = TransformType.NONE;

    public static void renderChunkUniforms(Chunk c) {
        MinecraftForge.EVENT_BUS.post(new RenderChunkUniformsEvent(c));
    }

    public static void enableLightingUniforms() {
        if (!EventManager.isGui && Config.isLightingEnabled()) {
            if (enabledLast) {
                if (previousShader != null)
                    previousShader.useShader();
                enabledLast = false;
            }
            if (ShaderManager.isCurrentShader(ShaderUtil.entityLightProgram)) {
                ShaderUtil.entityLightProgram.setUniform("lightingEnabled", true);
            }
        }
    }

    public static void disableLightingUniforms() {
        if (!EventManager.isGui && Config.isLightingEnabled()) {
            if (ShaderManager.isCurrentShader(ShaderUtil.entityLightProgram)) {
                ShaderUtil.entityLightProgram.setUniform("lightingEnabled", false);
            }
            if (!enabledLast) {
                previousShader = ShaderManager.getCurrentShader();
                enabledLast = true;
                ShaderManager.stopShader();
            }
        }
    }

    public static void setTransform(TransformType t) {
        itemTransformType = t;
    }

}
