package com.black_dog20.realbedo.event;

import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.eventbus.api.Event;

public class RenderChunkUniformsEvent extends Event {
    private final Chunk renderChunk;

    public RenderChunkUniformsEvent(Chunk r) {
        super();
        this.renderChunk = r;
    }

    public Chunk getChunk() {
        return renderChunk;
    }

    @Override
    public boolean isCancelable() {
        return false;
    }
}
