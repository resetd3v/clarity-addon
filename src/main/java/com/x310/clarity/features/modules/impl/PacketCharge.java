package com.x310.clarity.features.modules.impl;

import com.x310.clarity.Main;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import com.x310.clarity.features.modules.Module;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class PacketCharge extends Module {

    public PacketCharge() {
        super(Main.CATEGORY, "Packet Charge", "Charges your packets as long as you\'re not moving");
    }

    @EventHandler(priority = EventPriority.HIGHEST + 1)
    public static void onSendPacket(PacketEvent.Send event) {
        if (event.packet instanceof PlayerMoveC2SPacket.OnGroundOnly
            || event.packet instanceof PlayerMoveC2SPacket.PositionAndOnGround) {
            event.cancel();
        }
    }

}

