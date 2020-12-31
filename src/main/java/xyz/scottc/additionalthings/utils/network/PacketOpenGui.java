package xyz.scottc.additionalthings.utils.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import xyz.scottc.additionalthings.gui.TestGui;

import java.util.function.Supplier;

public class PacketOpenGui extends AbstractPacket<PacketOpenGui> {

    public PacketOpenGui(PacketBuffer buffer) {
        super(buffer);
    }

    public PacketOpenGui() {
    }

    @Override
    public void encode(PacketBuffer buffer) {

    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(TestGui::openGui);
    }
}
