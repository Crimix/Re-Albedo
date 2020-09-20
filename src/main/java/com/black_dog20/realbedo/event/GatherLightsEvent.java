package com.black_dog20.realbedo.event;

import com.black_dog20.realbedo.lighting.Light;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.eventbus.api.Event;

import java.util.ArrayList;

public class GatherLightsEvent extends Event {
    private final ArrayList<Light> lights;
    private final float maxDistance;
    private final Vector3d cameraPosition;
    private final ClippingHelper camera;

    public GatherLightsEvent(ArrayList<Light> lights, float maxDistance, Vector3d cameraPosition, ClippingHelper camera) {
        this.lights = lights;
        this.maxDistance = maxDistance;
        this.cameraPosition = cameraPosition;
        this.camera = camera;
    }

    public ImmutableList<Light> getLightList() {
        return ImmutableList.copyOf(lights);
    }

    public float getMaxDistance() {
        return maxDistance;
    }

    public Vector3d getCameraPosition() {
        return cameraPosition;
    }

    public ClippingHelper getCamera() {
        return camera;
    }

    public void add(Light light) {
    	float radius = light.radius();
        if(cameraPosition!=null) {
            double dist = MathHelper.sqrt(cameraPosition.squareDistanceTo(light.x, light.y, light.z));
            if (dist > radius + maxDistance) {
                return;
            }
        }

        if (camera != null && !camera.isBoundingBoxInFrustum(new AxisAlignedBB(
                light.x - radius,
                light.y - radius,
                light.z - radius,
                light.x + radius,
                light.y + radius,
                light.z + radius
        ))) {
            return;
        }
        lights.add(light);
    }

    @Override
    public boolean isCancelable() {
        return false;
    }
}