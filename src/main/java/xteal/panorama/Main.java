package xteal.panorama;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xteal.panorama.util.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Main implements ModInitializer {

    public static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final KeyBinding makePanoramaKeybind = new KeyBinding("key.tealbs.panorama", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F4, "category.tealbs");
    public static RotatingCubeMapRenderer SKYBOX;

    @Override
    public void onInitialize() {
        KeyBindingHelper.registerKeyBinding(makePanoramaKeybind);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Events.screenshotEvent(client);
        });

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            Registry.setup();
            try {
                File f = new File("panorama.dat");
                if (f.exists()) {
                    BufferedReader reader = new BufferedReader(new FileReader(f));
                    String pack = reader.readLine();
                    Util.loadPack(pack);
                    reader.close();
                }
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        });
    }

    public static KeyBinding getMakePanoramaKeybind() {
        return makePanoramaKeybind;
    }
}
