package xyz.scottc.additionalthings.registries;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.scottc.additionalthings.AdditionalThings;
import xyz.scottc.additionalthings.blocks.diamondgenerator.ContainerDiamondGenerator;
import xyz.scottc.additionalthings.blocks.placer.ContainerPlacer;

public class ContainerTypeRegistry {

    public static final DeferredRegister<ContainerType<?>> CONTAINERS =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, AdditionalThings.MODID);

    public static final RegistryObject<ContainerType<ContainerPlacer>> PLACER = CONTAINERS.register("placer",
            () -> IForgeContainerType.create((windowsId, inv, data) ->
                    new ContainerPlacer(windowsId, inv.player.getEntityWorld(), data.readBlockPos(), inv, inv.player)));
    public static final RegistryObject<ContainerType<ContainerDiamondGenerator>> DIAMOND_GENERATOR = CONTAINERS.register("diamond_generator",
            () -> IForgeContainerType.create((windowsId, inv, data) ->
                    new ContainerDiamondGenerator(windowsId, inv.player.getEntityWorld(), data.readBlockPos(), inv, inv.player)));
}
