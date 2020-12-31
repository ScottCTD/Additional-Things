package xyz.scottc.additionalthings.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
import xyz.scottc.additionalthings.AdditionalThings;

public class TestGui extends Screen {

    private static final ResourceLocation TEXTURE = new ResourceLocation(AdditionalThings.MODID, "textures/gui/testgui.png");

    private static final int TEXTURE_WIDTH = 251;
    private static final int TEXTURE_HEIGHT = 139;

    protected TestGui() {
        super(new TranslationTextComponent("screen." + AdditionalThings.MODID + "testgui"));
    }

    @Override
    protected void init() {
        int realX = (this.width - TEXTURE_WIDTH) / 2;
        int realY = (this.height - TEXTURE_HEIGHT) / 2;

        int margin = 10;

        this.addButton(new ExtendedButton(realX + margin, realY + margin, 60, 20,
                new TranslationTextComponent("screen.button." + AdditionalThings.MODID + "testbutton"),
                (button) -> AdditionalThings.LOGGER.info("Pressed!")));

        super.init();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        int realX = (this.width - TEXTURE_WIDTH) / 2;
        int realY = (this.height - TEXTURE_HEIGHT) / 2;
        this.blit(matrixStack, realX, realY, 0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    public static void openGui() {
        Minecraft.getInstance().displayGuiScreen(new TestGui());
    }
}
