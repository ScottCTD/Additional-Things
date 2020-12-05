package xyz.scottc.additionalthings.utils.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.network.NetworkEvent;
import xyz.scottc.additionalthings.utils.ATEnergyStorage;

import java.util.function.Supplier;

public class PacketEnergySync {

    private final int energy;
    private final BlockPos pos;

    public PacketEnergySync(int energy, BlockPos pos) {
        this.energy = energy;
        this.pos = pos;
    }

    public static PacketEnergySync decode(PacketBuffer buffer) {
        return new PacketEnergySync(buffer.readInt(), buffer.readBlockPos());
    }

    public static void encode(PacketEnergySync message, PacketBuffer buffer) {
        buffer.writeInt(message.energy);
        buffer.writeBlockPos(message.pos);
    }

    public static void handle(PacketEnergySync message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            // Get the server world
            ServerPlayerEntity player = context.get().getSender();
            if (player != null) {
                ServerWorld world = player.getServerWorld();
                TileEntity tile = world.getTileEntity(message.pos);
                if (tile != null) {
                    // 获取是否存储能量 是，就set能量
                    tile.getCapability(CapabilityEnergy.ENERGY).ifPresent(energyStorage -> {
                        if (energyStorage instanceof ATEnergyStorage) {
                            ((ATEnergyStorage) energyStorage).setEnergy(message.energy);
                        }
                    });
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
