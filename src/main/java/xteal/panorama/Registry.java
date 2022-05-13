package xteal.panorama;

import net.minecraft.client.texture.NativeImageBackedTexture;
import xteal.panorama.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Registry {
    public static List PANORAMAS = new ArrayList();

    public static void setup() {
        PANORAMAS.clear();
        File file = new File("mods/panorama");
        if (!file.exists()) {
            file.mkdirs();
        }

        File[] var1 = file.listFiles();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            File f = var1[var3];
            if (f.getName().endsWith(".zip")) {
                try {
                    Util.loadPackIcon(f.getName());
                } catch (Exception var6) {
                    var6.printStackTrace();
                }
            }
        }
    }

    public static void addPanorama(String s, NativeImageBackedTexture icon) {
        Entry p = new Entry(s);
        p.setIcon(icon);
        PANORAMAS.add(p);
    }

    public static List getAllForName(String s) {
        List l = new ArrayList();

        for(int i = 0; i < PANORAMAS.size(); ++i) {
            Entry p = (Entry)PANORAMAS.get(i);
            if (p.getPanoramaName().toLowerCase().contains(s.toLowerCase())) {
                l.add(p);
            }
        }

        return l;
    }
}
