package xyz.scottc.additionalthings.registries;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import xyz.scottc.additionalthings.blocks.treecutter.RendererTreeCutter;

public class RendererRegistry {

    public static void register() {
        ClientRegistry.bindTileEntityRenderer(TileentityTypeRegistry.TREE_CUTTER.get(), RendererTreeCutter::new);
    }

}
