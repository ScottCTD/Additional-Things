package xyz.scottc.additionalthings.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.server.ServerWorld;

import java.util.HashMap;
import java.util.Map;

public class CommandTPA {

    private static final Map<Long, TPAData> MAP = new HashMap<>();
    private static long id = 0;

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralCommandNode<CommandSource> tpaCommand = dispatcher.register(
                Commands.literal("tpa")
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(context -> tpa(context.getSource().asPlayer(), EntityArgument.getPlayer(context, "target"))))
                        .requires(source -> source.hasPermissionLevel(0))
        );
        LiteralCommandNode<CommandSource> tapAcceptCommand = dispatcher.register(
                Commands.literal("tpaaccept")
                        .then(Commands.argument("id", LongArgumentType.longArg())
                                .executes(context -> tpaAccpet(LongArgumentType.getLong(context, "id"))))
                        .requires(source -> source.hasPermissionLevel(0))
        );
    }

    private static int tpa(ServerPlayerEntity sender, ServerPlayerEntity target) {
        TPAData data = new TPAData(nextId(), sender, target);
        MAP.put(data.getId(), data);
        StringTextComponent line01 = new StringTextComponent(sender.getName().getString() + "want to teleport to you!");
        StringTextComponent line02 = new StringTextComponent("Accept or Deny: ");
        line02.append(new StringTextComponent("Accecpt")
                .setStyle(Style.EMPTY
                        .setUnderlined(true)
                        .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaaccept " + data.getId()))));
        target.sendStatusMessage(line01, false);
        target.sendStatusMessage(line02, false);

        sender.sendStatusMessage(new StringTextComponent("TPA Request Sent!"), false);
        return 0;
    }

    private static int tpaAccpet(long id) {
        TPAData data = MAP.get(id);
        ServerWorld targetWorld = data.getTarget().getServerWorld();
        BlockPos targetPos = data.getTarget().getPosition();
        data.getSender().teleport(targetWorld, targetPos.getX(), targetPos.getY(), targetPos.getZ(), data.getTarget().getYaw(0), data.getTarget().getPitch(0));
        MAP.remove(id);
        return 0;
    }

    private static long nextId() {
        return ++id;
    }

    private static class TPAData {

        private final long id;
        private final ServerPlayerEntity sender;
        private final ServerPlayerEntity target;

        public TPAData(long id, ServerPlayerEntity sender, ServerPlayerEntity target) {
            this.id = id;
            this.sender = sender;
            this.target = target;
        }

        public long getId() {
            return this.id;
        }

        public ServerPlayerEntity getSender() {
            return this.sender;
        }

        public ServerPlayerEntity getTarget() {
            return this.target;
        }
    }

}
