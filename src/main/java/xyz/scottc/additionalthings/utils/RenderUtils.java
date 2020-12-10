package xyz.scottc.additionalthings.utils;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.util.math.vector.Matrix4f;

import java.awt.*;

public class RenderUtils {

    public static void renderGroundPanel(IVertexBuilder builder, Matrix4f positionMatrix, Color defaultColor) {
        RenderUtils.renderGroundPanel(builder, positionMatrix, defaultColor, false, defaultColor);
    }

    public static void renderGroundPanel(IVertexBuilder builder, Matrix4f positionMatrix, Color defaultColor, boolean isHighlighted, Color highlightedColor) {
        // 复原状态
        GlStateManager.color4f(1, 1, 1, 1);
        float startX = 0, startY = 0, startZ = 0, endX = 1, endY = 1, endZ = 1;
        float r = defaultColor.getRed() / 255F, g = defaultColor.getGreen() / 255F, b = defaultColor.getBlue() / 255F, a = defaultColor.getAlpha() / 255F;
        if (isHighlighted) {
            r = highlightedColor.getRed() / 255F;
            g = highlightedColor.getGreen() / 255F;
            b = highlightedColor.getBlue() / 255F;
            a = highlightedColor.getAlpha() / 255F;
        }
        builder.pos(positionMatrix, startX, startY, startZ).color(r, g, b, a).endVertex();
        builder.pos(positionMatrix, startX, startY, endZ).color(r, g, b, a).endVertex();
        builder.pos(positionMatrix, endX, startY, endZ).color(r, g, b, a).endVertex();
        builder.pos(positionMatrix, endX, startY, startZ).color(r, g, b, a).endVertex();
    }

}
