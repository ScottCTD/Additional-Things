package xyz.scottc.additionalthings.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import xyz.scottc.additionalthings.utils.network.Network;
import xyz.scottc.additionalthings.utils.network.PacketOpenGui;

public class CommandTest implements Command<CommandSource> {

    private static final CommandTest INSTANCE = new CommandTest();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("test")
                .requires(source -> source.hasPermissionLevel(0))
                .executes(INSTANCE);
    }

    @Override
    public int run(CommandContext<CommandSource> context) {
        Network.sendToServer(new PacketOpenGui());
        return 0;
    }

}
