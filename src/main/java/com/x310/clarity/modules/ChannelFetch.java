package com.x310.clarity.modules;

import com.x310.clarity.Main;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;

public class ChannelFetch extends Module {

    public ChannelFetch() {
        super(Main.CATEGORY, "Channel Fetcher", "gets the current open channels in the minecraft server");
    }

    @EventHandler()
    public static void onPacketReceive(PacketEvent.Receive event) {
        if (event.packet instanceof CustomPayloadS2CPacket(CustomPayload payload)) {
            ChatUtils.info("Channel: " + payload.getId().id());

        }
    }

}
