package xyz.scottc.additionalthings;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.scottc.additionalthings.registries.*;
import xyz.scottc.additionalthings.utils.network.Network;

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
        // Load Config
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);

        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::doClientStuff);
        bus.addListener(this::setup);

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

    private void setup(final FMLCommonSetupEvent event) {
        Network.registerMessages();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ScreenRegistry.register();
    }

}
