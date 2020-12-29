package xyz.scottc.additionalthings.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class CommandSpawn {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralCommandNode<CommandSource> spawnCommand = dispatcher.register(
                Commands.literal("spawn")
                        .requires(source -> source.hasPermissionLevel(0))
                        .executes(new Spawn())
        );
    }

    private static class Spawn implements Command<CommandSource> {

        @Override
        public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
            ServerPlayerEntity player = context.getSource().asPlayer();
            ServerWorld world = player.getServerWorld();
            BlockPos spawnPoint = world.getSpawnPoint();
            player.teleport(world, spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ(), player.getYaw(0), player.getPitch(0));
            return 0;
        }

    }

}
