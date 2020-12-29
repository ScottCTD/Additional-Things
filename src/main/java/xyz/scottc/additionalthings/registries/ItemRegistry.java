package xyz.scottc.additionalthings.registries;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.scottc.additionalthings.AdditionalThings;
import xyz.scottc.additionalthings.items.CopperIngot;
import xyz.scottc.additionalthings.items.InfiniteSword;

public class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, AdditionalThings.MODID);

    // Item
    public static final RegistryObject<Item> COPPER_INGOT = ITEMS.register("copper_ingot", CopperIngot::new);
    public static final RegistryObject<Item> INFINITE_SWORD = ITEMS.register("infinite_sword", InfiniteSword::new);

    // BlockItem
    public static final RegistryObject<Item> BREAKER = ITEMS.register("breaker",
            () -> new BlockItem(BlockRegistry.BREAKER.get(), new Item.Properties().group(AdditionalThings.ITEM_TABLE)));
    public static final RegistryObject<Item> PLACER = ITEMS.register("placer",
            () -> new BlockItem(BlockRegistry.PLACER.get(), new Item.Properties().group(AdditionalThings.ITEM_TABLE)));
    public static final RegistryObject<Item> DIAMOND_GENERATOR = ITEMS.register("diamond_generator",
            () -> new BlockItem(BlockRegistry.DIAMOND_GENERATOR.get(), new Item.Properties().group(AdditionalThings.ITEM_TABLE)));
    public static final RegistryObject<Item> TREE_CUTTER = ITEMS.register("tree_cutter",
            () -> new BlockItem(BlockRegistry.TREE_CUTTER.get(), new Item.Properties().group(AdditionalThings.ITEM_TABLE)));
    public static final RegistryObject<Item> ITEM_COLLECTOR = ITEMS.register("item_collector",
            () -> new BlockItem(BlockRegistry.ITEM_COLLECTOR.get(), new Item.Properties().group(AdditionalThings.ITEM_TABLE)));


}
