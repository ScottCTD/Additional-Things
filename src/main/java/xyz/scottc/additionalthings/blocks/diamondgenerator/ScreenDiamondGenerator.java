package xyz.scottc.additionalthings.blocks.diamondgenerator;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
import org.jetbrains.annotations.NotNull;
import xyz.scottc.additionalthings.registries.TexturesRegistry;
import xyz.scottc.additionalthings.utils.network.Network;
import xyz.scottc.additionalthings.utils.network.PacketToggleEnergyOutputDirection;

import java.util.Arrays;

public class ScreenDiamondGenerator extends ContainerScreen<ContainerDiamondGenerator> {

    private static final int TEXTURE_WIDTH = 176;
    private static final int TEXTURE_HEIGHT = 166;

    private static final int ENERGY_BAR_WIDTH = 16, ENERGY_BAR_HEIGHT = 64;
    private static final int ENERGY_BAR_X = TEXTURE_WIDTH - ENERGY_BAR_WIDTH - 8,
            ENERGY_BAR_Y = 10;

    private final int[] energyOutputDirections;

    public ScreenDiamondGenerator(ContainerDiamondGenerator screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize = TEXTURE_WIDTH;
        this.ySize = TEXTURE_HEIGHT;
        screenContainer.tile.data.energyOutputDirections = new int[] {0, 0, 0, 0, 0, 0};
        this.energyOutputDirections = screenContainer.tile.data.energyOutputDirections;
    }

    @Override
    protected void init() {
        String energyOutputFace = "Energy Output: ";
        int margin = 8, titleEndY = this.titleY + this.font.FONT_HEIGHT, nameWidth = this.font.getStringWidth(energyOutputFace);
        this.addButton(new ExtendedButton(margin, titleEndY, nameWidth, this.font.FONT_HEIGHT, new StringTextComponent(energyOutputFace), button -> this.toggleEnergyOutputFace()));
        super.init();
    }

    private void toggleEnergyOutputFace() {
        if (Arrays.binarySearch(this.energyOutputDirections, 1) >= 0) {
            Arrays.fill(this.energyOutputDirections, 0);
        } else {
            Arrays.fill(this.energyOutputDirections, 1);
        }
        Network.sendToServer(new PacketToggleEnergyOutputDirection(this.container.tile.getPos(), this.energyOutputDirections));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(@NotNull MatrixStack matrixStack, float partialTicks, int x, int y) {
        if (this.minecraft != null) {
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.minecraft.getTextureManager().bindTexture(TexturesRegistry.DIAMOND_GENERATOR);
            int i = (this.width - this.xSize) / 2;
            int j = (this.height - this.ySize) / 2;
            blit(matrixStack, i, j, 0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(@NotNull MatrixStack matrixStack, int x, int y) {
        if (this.minecraft != null) {
            this.minecraft.getTextureManager().bindTexture(TexturesRegistry.ENERGY_BAR_FULL);
            blit(matrixStack, ENERGY_BAR_X, ENERGY_BAR_Y, 0, 0, ENERGY_BAR_WIDTH, ENERGY_BAR_HEIGHT, ENERGY_BAR_WIDTH, ENERGY_BAR_HEIGHT);
            double capacity = TileentityDiamondGenerator.CAPACITY, storedEnergy = this.container.getEnergy(), ratio = storedEnergy / capacity;
            this.minecraft.getTextureManager().bindTexture(TexturesRegistry.ENERGY_BAR);
            blit(matrixStack, ENERGY_BAR_X, ENERGY_BAR_Y, 0, 0, ENERGY_BAR_WIDTH, ENERGY_BAR_HEIGHT - (int) (ENERGY_BAR_HEIGHT * ratio), ENERGY_BAR_WIDTH, ENERGY_BAR_HEIGHT);

            if (this.container.getBlockState().get(BlockDiamondGenerator.START)) {
                drawString(matrixStack, this.font, "Processing", 8, 15, 0xFFFFFF);
            }
            if (Arrays.binarySearch(this.energyOutputDirections, 1) >= 0) {
                drawString(matrixStack, this.font, "Pushing Energy!", 8, 15 + this.font.FONT_HEIGHT, 0xFFFFFF);
            }
        }
        /*String text = "Energy: " + this.container.getEnergy();
        int textWidth = this.font.getStringWidth(text);
        drawString(matrixStack, this.font, text, (this.xSize - textWidth) / 2, 15, 0xFFFFFF);*/
        super.drawGuiContainerForegroundLayer(matrixStack, x, y);
    }

    @Override
    public void render(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);

        // Render the tooltip for energy bar
        if (this.isMouseOver(mouseX, mouseY, ENERGY_BAR_X, ENERGY_BAR_Y, ENERGY_BAR_WIDTH, ENERGY_BAR_HEIGHT)) {
            String text = this.container.getEnergy() + " / " + TileentityDiamondGenerator.CAPACITY;
            this.renderTooltip(matrixStack, new StringTextComponent(text), mouseX, mouseY);
        }
    }

    private boolean isMouseOver(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= this.guiLeft + x && mouseX <= this.guiLeft + x + width
                && mouseY >= this.guiTop + y && mouseY <= this.guiTop + y + height;
    }
}
