package xyz.scottc.additionalthings.utils.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import xyz.scottc.additionalthings.blocks.placer.DataPlacer;
import xyz.scottc.additionalthings.blocks.placer.TileentityPlacer;

import java.util.function.Supplier;

public class PacketStartPlacer extends AbstractPacket<PacketStartPlacer> {

    public boolean start;
    public BlockPos pos;

    public PacketStartPlacer(PacketBuffer buffer) {
        super(buffer);
        this.start = buffer.readBoolean();
        this.pos = buffer.readBlockPos();
    }

    public PacketStartPlacer(boolean start, BlockPos pos) {
        this.start = start;
        this.pos = pos;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBoolean(this.start);
        buffer.writeBlockPos(this.pos);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayerEntity player = context.get().getSender();
            if (player != null) {
                ServerWorld world = player.getServerWorld();
                TileEntity tile = world.getTileEntity(this.pos);
                if (tile instanceof TileentityPlacer) {
                    TileentityPlacer tilePlacer = (TileentityPlacer) tile;
                    DataPlacer data = tilePlacer.data;
                    if (data.get(DataPlacer.START_INDEX) >= 1) {
                        data.set(DataPlacer.START_INDEX, 0);
                    } else {
                        data.set(DataPlacer.START_INDEX, 1);
                    }
                }
            }
        });
    }
}
