package xteal.panorama;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

public class Entry {
    private String panoramaName;
    private NativeImageBackedTexture icon;
    private Identifier iconTex;

    public Entry(String s) {
        this.panoramaName = s;
    }

    public Identifier getIcon() {
        return this.iconTex;
    }

    public void setIcon(NativeImageBackedTexture icon) {
        this.icon = icon;
        String s = this.panoramaName.toLowerCase().replaceAll("[^a-z0-9/._-]", "");
        this.iconTex = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture("icons/" + s, icon);
    }

    public int getIconWidth() {
        return this.icon.getImage().getWidth();
    }

    public int getIconHeight() {
        return this.icon.getImage().getHeight();
    }

    public String getPanoramaName() {
        return this.panoramaName;
    }
}
