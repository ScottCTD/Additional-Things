package xyz.scottc.additionalthings;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.scottc.additionalthings.blocks.placer.ScreenPlacer;
import xyz.scottc.additionalthings.registries.BlockRegistry;
import xyz.scottc.additionalthings.registries.ContainerTypeRegistry;
import xyz.scottc.additionalthings.registries.ItemRegistry;
import xyz.scottc.additionalthings.registries.TileentityTypeRegistry;

@Mod(AdditionalThings.MODID)
public class AdditionalThings {

    public static final String MODID = "additionalthings";
    public static final Logger LOGGER = LogManager.getLogger();

    // Item Table
    public static final ItemGroup ITEM_TABLE = new ItemGroup(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ItemRegistry.BREAKER.get());
        }
    };

    public AdditionalThings() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        bus.addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register Containers
        ContainerTypeRegistry.CONTAINERS.register(bus);
        // Register Blocks
        BlockRegistry.BLOCKS.register(bus);
        // Register Items
        ItemRegistry.ITEMS.register(bus);
        // Register Tileentities
        TileentityTypeRegistry.TILEENTITIES.register(bus);
    }

    private void setup(final FMLCommonSetupEvent event) {}

    private void doClientStuff(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ContainerTypeRegistry.PLACER.get(), ScreenPlacer::new);
    }
}
