package com.black_dog20.realbedo.lighting;

import com.black_dog20.realbedo.event.GatherLightsEvent;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface ILightProvider {
    @OnlyIn(Dist.CLIENT)
    void gatherLights(GatherLightsEvent event, Entity context);
}
