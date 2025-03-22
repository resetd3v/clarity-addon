package com.x310.clarity.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class SpamPos extends Command {
    public SpamPos() {
        super("spampos", "spams server with PlayerMoveC2S packets");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes((context) -> {
            new Thread(() -> {
                ClientPlayNetworkHandler handler = mc.getNetworkHandler();
                if (handler == null || mc.player == null) return;

                while (true) {
                    double x = mc.player.getX();
                    double y = -10000;
                    double z = mc.player.getZ();
                    float yaw = mc.player.getYaw();
                    float pitch = mc.player.getPitch();
                    handler.sendPacket(new PlayerMoveC2SPacket.Full(x, y, z, yaw, pitch, true, false));
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }).start();

            return 1;
        });
    }
}
