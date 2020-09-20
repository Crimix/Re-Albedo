package com.black_dog20.realbedo.mixin;

import com.black_dog20.realbedo.event.ProfilerStartEvent;
import net.minecraft.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Profiler.class)
public class ProfilerMixin {

    @Inject(method = "endStartSection(Ljava/lang/String;)V", at = @At("RETURN"))
    private void endStartSection(String section, CallbackInfo ci) {
        ProfilerStartEvent.postNewEvent(section);
    }
}
