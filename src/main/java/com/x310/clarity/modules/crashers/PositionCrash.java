package com.x310.clarity.modules.crashers;

import com.x310.clarity.Main;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import java.util.Random;

public class PositionCrash extends Module {
    public PositionCrash() {
        super(Main.CRASH_GROUP, "Position Spam", "Spams PlayerMoveC2SPackets.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.player == null || mc.getNetworkHandler() == null) return;

        Random random = new Random();
        double x = mc.player.getX() + random.nextDouble();
        double y = -10000 + random.nextDouble();
        double z = mc.player.getZ() + random.nextDouble();
        float yaw = mc.player.getYaw();
        float pitch = mc.player.getPitch();

        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.Full(x, y, z, yaw, pitch, true, false));
    }
}
