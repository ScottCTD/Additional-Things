package xyz.scottc.additionalthings.registries;

import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.scottc.additionalthings.AdditionalThings;
import xyz.scottc.additionalthings.blocks.breaker.BlockBreaker;
import xyz.scottc.additionalthings.blocks.diamondgenerator.BlockDiamondGenerator;
import xyz.scottc.additionalthings.blocks.placer.BlockPlacer;

public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, AdditionalThings.MODID);

    public static RegistryObject<Block> BREAKER = BLOCKS.register("breaker", BlockBreaker::new);
    public static RegistryObject<Block> PLACER = BLOCKS.register("placer", BlockPlacer::new);
    public static RegistryObject<Block> DIAMOND_GENERATOR = BLOCKS.register("diamond_generator", BlockDiamondGenerator::new);

}
