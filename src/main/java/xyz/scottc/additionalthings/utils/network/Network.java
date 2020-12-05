package xyz.scottc.additionalthings.utils.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import xyz.scottc.additionalthings.AdditionalThings;

public class Network {

    private static final String PROTOCAL_VERSION = "1.0";
    private static int id = 0;
    public static final SimpleChannel INSTANCE = net.minecraftforge.fml.network.NetworkRegistry.newSimpleChannel(
            new ResourceLocation(AdditionalThings.MODID, "main"),
            () -> PROTOCAL_VERSION,
            PROTOCAL_VERSION::equals,
            PROTOCAL_VERSION::equals
    );

    public static void registerMessages() {
        INSTANCE.messageBuilder(PacketStartPlacer.class, nextID())
                .encoder(PacketStartPlacer::encode)
                .decoder(PacketStartPlacer::new)
                .consumer(PacketStartPlacer::handle)
                .add();

    }

    private static int nextID() {
        return ++id;
    }

    // For sending to more clients, check the forge documentation.
    // https://mcforge.readthedocs.io/en/1.16.x/networking/simpleimpl/
    public static void sendToClient(Object message, ServerPlayerEntity player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static void sendToAllClients(Object message, World world) {
        for (PlayerEntity player : world.getPlayers()) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
            sendToClient(message, serverPlayerEntity);
        }
    }

    public static void sendToServer(Object message) {
        INSTANCE.sendToServer(message);
    }

}
