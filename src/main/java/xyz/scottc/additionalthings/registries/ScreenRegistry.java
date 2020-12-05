package xyz.scottc.additionalthings.registries;

import net.minecraft.client.gui.ScreenManager;
import xyz.scottc.additionalthings.blocks.diamondgenerator.ScreenDiamondGenerator;
import xyz.scottc.additionalthings.blocks.placer.ScreenPlacer;

public class ScreenRegistry {

    public static void register() {
        ScreenManager.registerFactory(ContainerTypeRegistry.PLACER.get(), ScreenPlacer::new);
        ScreenManager.registerFactory(ContainerTypeRegistry.DIAMOND_GENERATOR.get(), ScreenDiamondGenerator::new);
    }

}
