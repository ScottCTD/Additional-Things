package xyz.scottc.additionalthings.blocks.treecutter;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import xyz.scottc.additionalthings.render.WorkingRangeRenderType;
import xyz.scottc.additionalthings.utils.RenderUtils;
import xyz.scottc.additionalthings.utils.ShapeUtils;

import java.awt.*;

public class RendererTreeCutter extends TileEntityRenderer<TileentityTreeCutter> {

    public RendererTreeCutter(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileentityTreeCutter tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn,
                       int combinedLightIn, int combinedOverlayIn) {
        if (tileEntityIn.renderRange) {

            //GlStateManager.color4f(1, 1, 1, 1); 复原状态
            matrixStackIn.push();
            BlockPos self = tileEntityIn.getPos();
            matrixStackIn.translate(-self.getX(), -self.getY(), -self.getZ());
            IVertexBuilder builder = bufferIn.getBuffer(WorkingRangeRenderType.SOLID_COLOUR);

            BlockPos[] workingArea = ShapeUtils.getSquareInFrontOf(tileEntityIn.getPos(), tileEntityIn.getBlockState(), TileentityTreeCutter.WORKING_RADIUS);

            for (int i = 0; i < workingArea.length; i++) {
                matrixStackIn.push();
                float scale = 0.7F, margin = (1 - scale) / 2;
                matrixStackIn.translate(workingArea[i].getX() + margin, workingArea[i].getY() + margin, workingArea[i].getZ() + margin);
                matrixStackIn.scale(scale, scale, scale);
                Matrix4f positionMatric = matrixStackIn.getLast().getMatrix();

                RenderUtils.renderGroundPanel(builder, positionMatric, new Color(230, 230, 230, 25),
                        i == tileEntityIn.workingIndex, new Color(255, 255, 255, 255));

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
