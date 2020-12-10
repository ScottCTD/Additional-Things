package xyz.scottc.additionalthings.blocks.treecutter;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.scottc.additionalthings.render.WorkingRangeRenderType;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class RendererTreeCutter extends TileEntityRenderer<TileentityTreeCutter> {

    public RendererTreeCutter(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileentityTreeCutter tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn,
                       int combinedLightIn, int combinedOverlayIn) {
        if (tileEntityIn.renderRange) {
            matrixStackIn.push();
            BlockPos self = tileEntityIn.getPos();
            // TODO 这玩意还是不理解为啥要平移才能得到正常的位置
            matrixStackIn.translate(-self.getX(), -self.getY(), -self.getZ());
            IVertexBuilder builder = bufferIn.getBuffer(WorkingRangeRenderType.SOLID_COLOUR);
            BlockPos[] workingArea = TileentityTreeCutter.getWorkingArea(tileEntityIn.getPos(), tileEntityIn.getBlockState());
            for (int i = 0; i < workingArea.length; i++) {
                matrixStackIn.push();
                float scale = 0.7F, margin = (1 - scale) / 2;
                matrixStackIn.translate(workingArea[i].getX() + margin, workingArea[i].getY() + margin, workingArea[i].getZ() + margin);
                matrixStackIn.scale(scale, scale, scale);
                Matrix4f positionMatric = matrixStackIn.getLast().getMatrix();
                float startX = 0, startY = 0, startZ = 0, endX = 1, endY = 1, endZ = 1;

                Color color = new Color(220, 220, 220);
                if (i == tileEntityIn.workingIndex) {
                    color = new Color(218, 40, 40);
                }

                // TODO 诡异的透明度问题
                float r = color.getRed() / 255F, g = color.getGreen() / 255F, b = color.getBlue() / 255F, a = 0.1F;
                builder.pos(positionMatric, startX, startY, startZ).color(r, g, b, a).endVertex();
                builder.pos(positionMatric, startX, startY, endZ).color(r, g, b, a).endVertex();
                builder.pos(positionMatric, endX, startY, endZ).color(r, g, b, a).endVertex();
                builder.pos(positionMatric, endX, startY, startZ).color(r, g, b, a).endVertex();

/*                // down
                builder.pos(positionMatric, startX, startY, startZ).color(220, 220, 220, 0.1F).endVertex();
                builder.pos(positionMatric, endX, startY, startZ).color(220, 220, 220, 0.1F).endVertex();
                builder.pos(positionMatric, endX, startY, endZ).color(220, 220, 220, 0.1F).endVertex();
                builder.pos(positionMatric, startX, startY, endZ).color(220, 220, 220, 0.1F).endVertex();

                // up
                builder.pos(positionMatric, startX, endY, startZ).color(220, 220, 220, 0.1F).endVertex();
                builder.pos(positionMatric, startX, endY, endZ).color(220, 220, 220, 0.1F).endVertex();
                builder.pos(positionMatric, endX, endY, endZ).color(220, 220, 220, 0.1F).endVertex();
                builder.pos(positionMatric, endX, endY, startZ).color(220, 220, 220, 0.1F).endVertex();

                // south
                builder.pos(positionMatric, startX, startY, startZ).color(220, 220, 220, 0.1F).endVertex();
                builder.pos(positionMatric, startX, endY, startZ).color(220, 220, 220, 0.1F).endVertex();
                builder.pos(positionMatric, endX, endY, startZ).color(220, 220, 220, 0.1F).endVertex();
                builder.pos(positionMatric, endX, startY, startZ).color(220, 220, 220, 0.1F).endVertex();

                // north
                builder.pos(positionMatric, startX, startY, endZ).color(220, 220, 220, 0.1F).endVertex();
                builder.pos(positionMatric, endX, startY, endZ).color(220, 220, 220, 0.1F).endVertex();
                builder.pos(positionMatric, endX, endY, endZ).color(220, 220, 220, 0.1F).endVertex();
                builder.pos(positionMatric, startX, endY, endZ).color(220, 220, 220, 0.1F).endVertex();

                // east
                builder.pos(positionMatric, endX, startY, startZ).color(220, 220, 220, 0.1F).endVertex();
                builder.pos(positionMatric, endX, endY, startZ).color(220, 220, 220, 0.1F).endVertex();
                builder.pos(positionMatric, endX, endY, endZ).color(220, 220, 220, 0.1F).endVertex();
                builder.pos(positionMatric, endX, startY, endZ).color(220, 220, 220, 0.1F).endVertex();

                // west
                builder.pos(positionMatric, startX, startY, startZ).color(220, 220, 220, 0.1F).endVertex();
                builder.pos(positionMatric, startX, startY, endZ).color(220, 220, 220, 0.1F).endVertex();
                builder.pos(positionMatric, startX, endY, endZ).color(220, 220, 220, 0.1F).endVertex();
                builder.pos(positionMatric, startX, endY, startZ).color(220, 220, 220, 0.1F).endVertex();*/

                matrixStackIn.pop();
            }
            matrixStackIn.pop();

        }
    }
}
