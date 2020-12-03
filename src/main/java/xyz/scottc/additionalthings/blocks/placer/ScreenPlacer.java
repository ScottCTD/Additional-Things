package xyz.scottc.additionalthings.blocks.placer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.NotNull;
import xyz.scottc.additionalthings.registries.TexturesRegistry;

public class ScreenPlacer extends ContainerScreen<ContainerPlacer> {

    private final int textureWidth = 176;
    private final int textureHeight = 166;

    public ScreenPlacer(ContainerPlacer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize = textureWidth;
        this.ySize = textureHeight;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(@NotNull MatrixStack matrixStack, float partialTicks, int x, int y) {
        if (super.minecraft != null) {
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.minecraft.getTextureManager().bindTexture(TexturesRegistry.PLACER);
            int i = (this.width - this.xSize) / 2;
            int j = (this.height - this.ySize) / 2;
            blit(matrixStack, i, j, 0, 0, this.textureWidth, this.textureHeight);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(@NotNull MatrixStack matrixStack, int x, int y) {
        drawString(matrixStack, this.font, "Items here will be placed!", 30, 30, 0xFFFFFF);
        super.drawGuiContainerForegroundLayer(matrixStack, x, y);
    }

    @Override
    public void render(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

}
