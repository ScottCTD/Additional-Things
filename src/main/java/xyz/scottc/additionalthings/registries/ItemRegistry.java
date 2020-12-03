package xyz.scottc.additionalthings.registries;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.scottc.additionalthings.AdditionalThings;

public class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, AdditionalThings.MODID);

    public static final RegistryObject<Item> BREAKER = ITEMS.register("breaker",
            () -> new BlockItem(BlockRegistry.BREAKER.get(), new Item.Properties().group(AdditionalThings.ITEM_TABLE)));
    public static final RegistryObject<Item> PLACER = ITEMS.register("placer",
            () -> new BlockItem(BlockRegistry.PLACER.get(), new Item.Properties().group(AdditionalThings.ITEM_TABLE)));
}
