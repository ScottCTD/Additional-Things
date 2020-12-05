package xyz.scottc.additionalthings.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;
import xyz.scottc.additionalthings.AdditionalThings;

public class CommandTest implements Command<CommandSource> {

    private static final CommandTest INSTANCE = new CommandTest();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("test")
                .requires(source -> source.hasPermissionLevel(0))
                .executes(INSTANCE);
    }

    @Override
    public int run(CommandContext<CommandSource> context) {
        context.getSource().sendFeedback(new TranslationTextComponent(
                "message." + AdditionalThings.MODID + ".command.test"), true);
        return 0;
    }

}
