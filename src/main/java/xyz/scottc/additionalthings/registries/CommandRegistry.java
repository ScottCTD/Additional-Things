package xyz.scottc.additionalthings.registries;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.scottc.additionalthings.AdditionalThings;
import xyz.scottc.additionalthings.commands.CommandAT;
import xyz.scottc.additionalthings.commands.CommandSpawn;
import xyz.scottc.additionalthings.commands.CommandTPA;

@Mod.EventBusSubscriber(modid = AdditionalThings.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommandRegistry {

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        CommandAT.register(event.getDispatcher());
        CommandSpawn.register(event.getDispatcher());
        CommandTPA.register(event.getDispatcher());
    }

}
