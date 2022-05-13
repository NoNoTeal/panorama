package xteal.panorama.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;
import xteal.panorama.Entry;
import xteal.panorama.Main;
import xteal.panorama.Registry;
import xteal.panorama.util.Util;

import java.util.ArrayList;
import java.util.List;

public class GUIPanoramaSelector extends Screen {
    private TextFieldWidget searchBox;
    private List panoramas;
    private List panoButtons = new ArrayList();
    private ButtonWidget btnPreviousPage;
    private ButtonWidget btnNextPage;
    private int page = 0;
    private Text tooltip;

    public GUIPanoramaSelector() {
        super(Text.of("Panorama Selection GUI"));
        this.panoramas = Registry.PANORAMAS;
    }

    protected void init() {
        super.init();
        this.page = 0;
        this.searchBox = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, this.width / 2 - 148, this.height / 2 + 80, 70, 20, null, new TranslatableText("panorama.search"));
        this.addDrawableChild(this.searchBox);
        this.setInitialFocus(this.searchBox);
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 72, this.height / 2 + 80, 46, 20, Text.of("Search"), (button) -> {
            if (this.searchBox.getText() != null && this.searchBox.getText().length() > 0) {
                this.page = 0;
                this.panoramas = Registry.getAllForName(this.searchBox.getText());
                this.btnNextPage.active = this.panoramas.size() > 8;

                this.refreshButtons();
            }

        }));
        this.addDrawableChild(this.btnPreviousPage = new ButtonWidget(this.width / 2 - 22, this.height / 2 + 80, 80, 20, Text.of("Previous Page"), (button) -> {
            if (this.page - 1 >= 0) {
                --this.page;
                this.btnNextPage.active = true;
                this.refreshButtons();
                if (this.page - 1 < 0) {
                    this.btnPreviousPage.active = false;
                }
            }

        }));
        this.btnPreviousPage.active = false;
        this.addDrawableChild(this.btnNextPage = new ButtonWidget(this.width / 2 + 62, this.height / 2 + 80, 80, 20, Text.of("Next Page"), (button) -> {
            System.out.println(this.panoramas.size());
            if ((this.page + 1) * 8 < this.panoramas.size()) {
                ++this.page;
                this.btnPreviousPage.active = true;
                this.refreshButtons();
                if ((this.page + 1) * 8 >= this.panoramas.size()) {
                    this.btnNextPage.active = false;
                }
            }

        }));
        this.addDrawableChild(new ButtonWidget(4, 4, 60, 20, Text.of("Back"), (button) -> MinecraftClient.getInstance().setScreen(new TitleScreen())));
        this.addDrawableChild(new ButtonWidget(this.width - 64, 4, 60, 20, Text.of("Reload"), (button) -> {
            this.page = 0;
            Registry.setup();
            this.panoramas = Registry.PANORAMAS;
            this.btnNextPage.active = this.panoramas.size() > 8;

            this.refreshButtons();
        }));
        this.addDrawableChild(new ButtonWidget(this.width - 128, 4, 60, 20, Text.of("Reset"), (button) -> {
            try {
                Util.loadPack("");
            } catch (Exception var2) {
                var2.printStackTrace();
            }

        }));
        if (this.panoramas.size() <= 8) {
            this.btnNextPage.active = false;
        }

        this.refreshButtons();
    }

    public void refreshButtons() {
        for (int i = 0; i < this.panoButtons.size(); i++) {
            this.remove((Element) this.panoButtons.get(i));
        }

        for(int i = this.page * 8; i < this.page * 8 + 8; ++i) {
            if (i < this.panoramas.size()) {
                Entry pan = (Entry)this.panoramas.get(i);
                this.addPanoramaButton(new PanoramaButton(pan, this.width / 2 - 148 + i % 4 * 74, this.height / 2 + i % 8 / 4 * 74 - 74));
            }
        }

    }

    public void addPanoramaButton(PanoramaButton btn) {
        this.addDrawableChild(btn);
        this.panoButtons.add(btn);
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Main.SKYBOX.render(partialTicks, MathHelper.clamp(1.0F, 0.0F, 1.0F));
        drawCenteredText(matrixStack, this.textRenderer, "Panorama Selector", this.width / 2, this.height / 2 - 102, -1);
        if (this.panoramas.size() <= 0) {
            drawCenteredText(matrixStack, this.textRenderer, "No Panoramas Found", this.width / 2, this.height / 2, -1);
        }

        if (this.searchBox != null) {
            this.searchBox.render(matrixStack, mouseX, mouseY, partialTicks);
        }

        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.tooltip = null;

        for(int i = 0; i < this.children().size(); ++i) {
            Drawable b = (Drawable) this.children().get(i);
            if (b instanceof PanoramaButton bb) {
                if (bb.isHovered() || bb.isFocused()) {
                    this.tooltip = bb.getMessage();
                }
            }
        }

        if (this.tooltip != null) {
            this.renderTooltip(matrixStack, this.tooltip, mouseX, mouseY);
        }

    }

    public void tick() {
        if (this.searchBox != null) {
            this.searchBox.tick();
        }

        super.tick();
    }
}