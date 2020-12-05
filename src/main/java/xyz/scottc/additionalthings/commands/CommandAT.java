package xyz.scottc.additionalthings.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import xyz.scottc.additionalthings.AdditionalThings;

public class CommandAT {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralCommandNode<CommandSource> command = dispatcher.register(
                Commands.literal(AdditionalThings.MODID)
                        .then(CommandTest.register(dispatcher))
        );
        dispatcher.register(Commands.literal("at").redirect(command));
    }

}
