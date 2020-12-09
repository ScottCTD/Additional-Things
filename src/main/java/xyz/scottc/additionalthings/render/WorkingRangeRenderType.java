package xyz.scottc.additionalthings.render;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.lwjgl.opengl.GL11;

public class WorkingRangeRenderType extends RenderType {

    public WorkingRangeRenderType(String nameIn, VertexFormat formatIn, int drawModeIn, int bufferSizeIn, boolean useDelegateIn,
                                  boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
        super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }

    public static RenderType SOLID_COLOUR = makeType("solidColour",
            DefaultVertexFormats.POSITION_COLOR, GL11.GL_QUADS, 256,
            RenderType.State.getBuilder()
                    .transparency(ADDITIVE_TRANSPARENCY)
                    .texture(NO_TEXTURE)
                    .depthTest(DEPTH_LEQUAL)
                    .cull(CULL_DISABLED)
                    .lightmap(LIGHTMAP_DISABLED)
                    .writeMask(COLOR_DEPTH_WRITE)
                    .build(true));
}
