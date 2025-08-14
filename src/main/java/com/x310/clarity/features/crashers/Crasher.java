package com.x310.clarity.features.crashers;

import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

public abstract class Crasher extends Module {
    public final SettingGroup sg;
    public final Setting<Boolean> disableOnLeave;

    public Crasher(Category category, String name, String description, String... aliases) {
        super(category, name, description, aliases);
        sg = settings.createGroup(name);
        disableOnLeave = sg.add(new BoolSetting.Builder()
            .name("disable-on-leave")
            .description("Disables spam when you leave a server.")
            .defaultValue(true)
            .build()
        );
    }

    public Crasher(Category category, String name, String desc) {
        this(category, name, desc, (String[]) null);
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (disableOnLeave.get()) toggle();
    }
}
