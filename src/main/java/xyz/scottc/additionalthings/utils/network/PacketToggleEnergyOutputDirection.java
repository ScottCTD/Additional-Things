package xyz.scottc.additionalthings.utils.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import xyz.scottc.additionalthings.blocks.diamondgenerator.TileentityDiamondGenerator;

import java.util.function.Supplier;

public class PacketToggleEnergyOutputDirection extends AbstractPacket<PacketToggleEnergyOutputDirection> {

    public int[] energyOutputDirections;
    public BlockPos blockPos;

    public PacketToggleEnergyOutputDirection(BlockPos blockPos, int[] directions) {
        this.energyOutputDirections = directions;
        this.blockPos = blockPos;
    }

    public PacketToggleEnergyOutputDirection(PacketBuffer buffer) {
        super(buffer);
        this.blockPos = buffer.readBlockPos();
        this.energyOutputDirections = buffer.readVarIntArray();
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBlockPos(this.blockPos);
        buffer.writeVarIntArray(this.energyOutputDirections);
    }


    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayerEntity player = context.get().getSender();
            if (player != null) {
                World world = player.world;
                TileEntity tileEntity = world.getTileEntity(this.blockPos);
                if (tileEntity instanceof TileentityDiamondGenerator) {
                    TileentityDiamondGenerator diamondGenerator = (TileentityDiamondGenerator) tileEntity;
                    diamondGenerator.data.energyOutputDirections = this.energyOutputDirections;
                }
            }
        });
    }
}
