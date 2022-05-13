package xteal.panorama.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import xteal.panorama.Events;
import xteal.panorama.Registry;
import xteal.panorama.SkyBox;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Util {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");

    public static void zipFiles(final String images) {
        (new Thread(() -> {
            NativeImage[] imgs = (NativeImage[]) Events.images.remove(images);
            String string = "panorama-" + DATE_FORMAT.format(new Date());
            int e = 1;
            File file;
            while ((file = new File("mods/panorama", string + (e == 1 ? "" : "_" + e) + ".zip")).exists()) {
                ++e;
            }
            if (imgs != null) {
                try {
                    FileOutputStream fos = new FileOutputStream("mods/panorama/" + file.getName());
                    ZipOutputStream zos = new ZipOutputStream(fos);

                    for(int i = 0; i < imgs.length; ++i) {
                        NativeImage image = imgs[i];
                        zos.putNextEntry(new ZipEntry("panorama_" + i + ".png"));
                        zos.write(image.getBytes());
                        zos.closeEntry();
                    }

                    zos.close();
                } catch (IOException ioException) {
                    System.out.println("Error creating zip file: " + ioException);
                }
            }

            File finalFile = file;
            MutableText text = new LiteralText("Panorama saved as ").append(new LiteralText(file.getName()).formatted(Formatting.UNDERLINE).styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, finalFile.getAbsolutePath()))));
            MinecraftClient.getInstance().player.sendMessage(text, false);
            Registry.addPanorama(file.getName(), new NativeImageBackedTexture(imgs[0]));
        })).start();
    }


    public static void loadPack(String packName) throws Exception {
        if (packName != null && (packName == null || packName.length() > 0)) {
            NativeImageBackedTexture[] textures = new NativeImageBackedTexture[6];
            Identifier[] tex = new Identifier[6];
            FileInputStream fis = new FileInputStream("mods/panorama/" + packName);

            try {
                BufferedInputStream bis = new BufferedInputStream(fis);

                try {
                    ZipInputStream zis = new ZipInputStream(bis);

                    try {
                        int index = 0;

                        while(true) {
                            ZipEntry ze;
                            if ((ze = zis.getNextEntry()) != null) {
                                if (!ze.getName().endsWith(".png")) {
                                    continue;
                                }

                                textures[index] = TextureUtil.loadInputstream(zis);
                                ++index;
                                if (index != 6) {
                                    continue;
                                }
                            }

                            bis.close();
                            zis.close();
                            break;
                        }
                    } catch (Throwable var11) {
                        try {
                            zis.close();
                        } catch (Throwable var10) {
                            var11.addSuppressed(var10);
                        }

                        throw var11;
                    }

                    zis.close();
                } catch (Throwable var12) {
                    try {
                        bis.close();
                    } catch (Throwable var9) {
                        var12.addSuppressed(var9);
                    }

                    throw var12;
                }

                bis.close();
            } catch (Throwable var13) {
                try {
                    fis.close();
                } catch (Throwable var8) {
                    var13.addSuppressed(var8);
                }

                throw var13;
            }

            fis.close();
            boolean cont = true;
            int var17 = textures.length;
            byte var18 = 0;
            if (var18 < var17) {
                NativeImageBackedTexture t = textures[var18];
                if (t == null) {
                    cont = false;
                }
            }

            for(int i = 0; i < textures.length; ++i) {
                tex[i] = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture("panorama/frame_" + i, textures[i]);
            }

            if (cont) {
                SkyBox.textures = tex;
                FileWriter fw = new FileWriter("panorama.dat");
                fw.write(packName);
                fw.close();
            }

        } else {
            SkyBox.textures = null;
        }
    }

    public static void loadPackIcon(String packName) throws Exception {
        NativeImageBackedTexture texture = null;
        FileInputStream fis = new FileInputStream("mods/panorama/" + packName);

        try {
            BufferedInputStream bis = new BufferedInputStream(fis);

            try {
                ZipInputStream zis = new ZipInputStream(bis);

                try {
                    ZipEntry ze;
                    while((ze = zis.getNextEntry()) != null) {
                        if (ze.getName().equals("icon.png")) {
                            texture = TextureUtil.loadInputstream(zis);
                            break;
                        }

                        if (ze.getName().equalsIgnoreCase("panorama_0.png")) {
                            texture = TextureUtil.loadInputstream(zis);
                        }
                    }

                    zis.close();
                    bis.close();
                } catch (Throwable var10) {
                    try {
                        zis.close();
                    } catch (Throwable var9) {
                        var10.addSuppressed(var9);
                    }

                    throw var10;
                }

                zis.close();
            } catch (Throwable var11) {
                try {
                    bis.close();
                } catch (Throwable var8) {
                    var11.addSuppressed(var8);
                }

                throw var11;
            }

            bis.close();
        } catch (Throwable var12) {
            try {
                fis.close();
            } catch (Throwable var7) {
                var12.addSuppressed(var7);
            }

            throw var12;
        }

        fis.close();
        if (texture != null) {
            Registry.addPanorama(packName, texture);
        }

    }
}
