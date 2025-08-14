package com.x310.clarity.features.modules;

import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Category;

public abstract class Module extends meteordevelopment.meteorclient.systems.modules.Module {
    public final SettingGroup sg;

    public Module(Category category, String name, String description, String sgName, String... aliases) {
        super(category, name, description, aliases);
        sg = settings.createGroup((sgName != null) ? sgName : name);
    }

    public Module(Category category, String name, String description, String sgName) {
        this(category, name, description, sgName, (String[]) null);
    }

    public Module(Category category, String name, String description, String... aliases) {
        this(category, name, description, null, aliases);
    }

    public Module(Category category, String name, String desc) {
        this(category, name, desc, null,  (String[]) null);
    }
}
