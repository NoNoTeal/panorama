package xteal.panorama;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.util.math.Vec3d;
import xteal.panorama.util.Util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Events {
    public static Map images = new HashMap();
    public static Vec3d position;
    public static String currentName = "";
    public static NativeImage[] screenshots;
    public static boolean takePanorama = false;
    public static int index = 0;
    public static double backupFov = 70.0;
    static int timer = 0;

    public static void screenshotEvent(MinecraftClient client) {
        if (MinecraftClient.getInstance().world != null && MinecraftClient.getInstance().cameraEntity != null && Main.getMakePanoramaKeybind().isPressed() && !takePanorama) {
            ClientPlayerEntity pe = MinecraftClient.getInstance().player;
            position = new Vec3d(pe.getX(), pe.getY(), pe.getZ());
            backupFov = MinecraftClient.getInstance().options.fov;
            MinecraftClient.getInstance().options.fov = 90.0;
            long time = System.currentTimeMillis();
            currentName = "panorama-" + time / 100L;
            screenshots = new NativeImage[6];
            takePanorama = true;
            index = 0;
        }
    }

    public static void takeScreenshot(MinecraftClient client, int index) {
        NativeImage image = ScreenshotRecorder.takeScreenshot(MinecraftClient.getInstance().getFramebuffer());
        File file = new File("temp/panorama_" + index + ".png");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int fromY = 0;
        int dimension;
        int fromX;
        if (imageWidth > imageHeight) {
            dimension = imageHeight;
            fromX = imageWidth / 2 - imageHeight / 2;
        } else {
            dimension = imageWidth;
            fromX = imageWidth / 2 - imageWidth / 2;
        }

        NativeImage image2 = new NativeImage(dimension, dimension, true);
        image.resizeSubRectTo(fromX, fromY, dimension, dimension, image2);
        screenshots[index] = image2;
    }

    public static void cameraEvent(MinecraftClient client) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (takePanorama) {
            if (timer >= 2) {
                timer = 0;
                if (index < 6) {
                    client.options.fov = 90.0;
                    takeScreenshot(mc, index);
                    ++index;
                } else {
                    takePanorama = false;
                    mc.options.fov = backupFov;
                    images.put(currentName, screenshots);

                    try {
                        Util.zipFiles(currentName);
                    } catch (Exception var3) {
                        var3.printStackTrace();
                    }
                }
            } else {
                ++timer;
            }
        } else {
            timer = 0;
        }
    }

    public enum Facing {
        SOUTH(0.0F, 0.0F),
        WEST(90.0F, 0.0F),
        NORTH(180.0F, 0.0F),
        EAST(-90.0F, 0.0F),
        UP(0.0F, -90.0F),
        DOWN(0.0F, 90.0F);

        public float yaw;
        public float pitch;

        Facing(float yaw, float pitch) {
            this.yaw = yaw;
            this.pitch = pitch;
        }

        public static Facing getIndex(int index) {
            return values()[index];
        }
    }
}
