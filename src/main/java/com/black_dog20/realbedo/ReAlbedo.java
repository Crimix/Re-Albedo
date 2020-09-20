package com.black_dog20.realbedo;

import com.black_dog20.realbedo.event.GatherLightsEvent;
import com.black_dog20.realbedo.lighting.DefaultLightProvider;
import com.black_dog20.realbedo.lighting.ILightProvider;
import com.black_dog20.realbedo.lighting.Light;
import com.black_dog20.realbedo.util.ShaderUtil;
import com.black_dog20.realbedo.util.TriConsumer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.INBT;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Mod(ReAlbedo.MOD_ID)
public class ReAlbedo {
    public static final String MOD_ID = "realbedo";
    private static final Logger LOGGER = LogManager.getLogger();

    public static Map<Block, TriConsumer<BlockPos, BlockState, GatherLightsEvent>> MAP = new HashMap<>();

    public ReAlbedo() {
        IEventBus event = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.spec);
        Config.loadConfig(Config.spec, FMLPaths.CONFIGDIR.get().resolve(MOD_ID + "-client.toml"));

        event.addListener(this::loadComplete);
        event.addListener(this::commonSetup);
        event.addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @CapabilityInject(ILightProvider.class)
    public static Capability<ILightProvider> LIGHT_PROVIDER_CAPABILITY;

    public void commonSetup(FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(ILightProvider.class, new Capability.IStorage<ILightProvider>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<ILightProvider> capability, ILightProvider instance, Direction side) {
                return null;
            }

            @Override
            public void readNBT(Capability<ILightProvider> capability, ILightProvider instance, Direction side, INBT nbt) {

            }
        }, DefaultLightProvider::new);
    }


    public void loadComplete(FMLLoadCompleteEvent event) {
        event.enqueueWork(() -> ((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener(new ShaderUtil()));
    }

    public void registerBlockHandler(Block block, TriConsumer<BlockPos, BlockState, GatherLightsEvent> consumer) {
        MAP.put(block, consumer);
    }

    public void clientSetup(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventManager());
        registerBlockHandler(Blocks.REDSTONE_TORCH, (pos, state, evt) -> {
            if (state.get(RedstoneTorchBlock.LIT)) {
                evt.add(Light.builder()
                        .pos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5)
                        .color(1.0f, 0, 0, 1.0f)
                        .radius(6)
                        .build());
            }
        });
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
