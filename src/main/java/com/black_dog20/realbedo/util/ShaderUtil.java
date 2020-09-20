package com.black_dog20.realbedo.util;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.resource.VanillaResourceType;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ShaderUtil implements ISelectiveResourceReloadListener {

    //public static int currentProgram = -1;
    public static ShaderManager fastLightProgram;
    public static ShaderManager entityLightProgram;
    public static ShaderManager depthProgram;

    private static int VERTEX = 35633;
    private static int FRAGMENT = 35632;
    
    public ShaderUtil() {
        init(Minecraft.getInstance().getResourceManager());
    }

    public static void init(IResourceManager manager) {
        fastLightProgram = new ShaderManager(new ResourceLocation("realbedo:fastlight"), manager);
        entityLightProgram = new ShaderManager(new ResourceLocation("realbedo:entitylight"), manager);
        depthProgram = new ShaderManager(new ResourceLocation("realbedo:depth"), manager);
    }

    public static int loadProgram(String vsh, String fsh, IResourceManager manager) {
        int vertexShader = createShader(vsh, VERTEX, manager);
        int fragmentShader = createShader(fsh, FRAGMENT, manager);
        int program = GlStateManager.createProgram();
        GlStateManager.attachShader(program, vertexShader);
        GlStateManager.attachShader(program, fragmentShader);
        GlStateManager.linkProgram(program);
        String s = GL20.glGetProgramInfoLog(program);
        System.out.println("GL LOG: "+s);
        return program;
    }

    public static int createShader(String filename, int shaderType, IResourceManager manager) {
        int shader = GlStateManager.createShader(shaderType);
        if (shader == 0)
            return 0;
        try {
            String s =readFileAsString(filename, manager);
            GlStateManager.shaderSource(shader, s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        GlStateManager.compileShader(shader);

        if (GL20.glGetShaderi(shader, 35713) == GL11.GL_FALSE)
            throw new RuntimeException("Error creating shader: " + getLogInfo(shader));

        return shader;
    }

    public static String getLogInfo(int obj) {
        return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }

    public static String readFileAsString(String filename, IResourceManager manager) throws Exception {
        System.out.println("Loading shader [" + filename + "]...");
        InputStream in = null;
        try {
            IResource resource = manager.getResource(new ResourceLocation(filename));
            in = resource.getInputStream();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String s = "";

        if (in != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
                s = reader.lines().collect(Collectors.joining("\n"));
            }
        }
        return s;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
        if (resourcePredicate.test(VanillaResourceType.SHADERS)) {
            init(resourceManager);
        }
    }
}
