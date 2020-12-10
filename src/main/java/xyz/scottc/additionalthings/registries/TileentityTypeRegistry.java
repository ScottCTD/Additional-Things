package xyz.scottc.additionalthings.registries;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.scottc.additionalthings.AdditionalThings;
import xyz.scottc.additionalthings.blocks.breaker.TileentityBreaker;
import xyz.scottc.additionalthings.blocks.diamondgenerator.TileentityDiamondGenerator;
import xyz.scottc.additionalthings.blocks.itemcollector.TileentityItemCollector;
import xyz.scottc.additionalthings.blocks.placer.TileentityPlacer;
import xyz.scottc.additionalthings.blocks.treecutter.TileentityTreeCutter;

public class TileentityTypeRegistry {

    public static final DeferredRegister<TileEntityType<?>> TILEENTITIES =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, AdditionalThings.MODID);

    public static final RegistryObject<TileEntityType<TileentityBreaker>> BREAKER = TILEENTITIES.register("breaker",
            () -> TileEntityType.Builder.create(TileentityBreaker::new, BlockRegistry.BREAKER.get()).build(null));
    public static final RegistryObject<TileEntityType<TileentityPlacer>> PLACER = TILEENTITIES.register("placer",
            () -> TileEntityType.Builder.create(TileentityPlacer::new, BlockRegistry.PLACER.get()).build(null));
    public static final RegistryObject<TileEntityType<TileentityDiamondGenerator>> DIAMOND_GENERATOR = TILEENTITIES.register("diamond_generator",
            () -> TileEntityType.Builder.create(TileentityDiamondGenerator::new, BlockRegistry.DIAMOND_GENERATOR.get()).build(null));
    public static final RegistryObject<TileEntityType<TileentityTreeCutter>> TREE_CUTTER = TILEENTITIES.register("tree_cutter",
            () -> TileEntityType.Builder.create(TileentityTreeCutter::new, BlockRegistry.TREE_CUTTER.get()).build(null));
    public static final RegistryObject<TileEntityType<TileentityItemCollector>> ITEM_COLLECTOR = TILEENTITIES.register("item_collector",
            () -> TileEntityType.Builder.create(TileentityItemCollector::new, BlockRegistry.ITEM_COLLECTOR.get()).build(null));

}
