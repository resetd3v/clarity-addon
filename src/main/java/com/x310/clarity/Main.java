package com.x310.clarity;

import com.x310.clarity.commands.*;
import com.x310.clarity.commands.SpamCommandExec;
import com.x310.clarity.hud.Arraylist;
import com.x310.clarity.hud.Watermark;
import com.mojang.logging.LogUtils;
import com.x310.clarity.modules.Canceller;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;

import com.x310.clarity.modules.ClarityNametags;
import org.slf4j.Logger;

public class Main extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("Clarity Addon");
    public static final HudGroup HUD_GROUP = new HudGroup("Clarity Addon");

    @Override
    public void onInitialize() {
        LOG.info("Initializing Meteor Addon Template");

        // Modules
        Modules.get().add(new ClarityNametags());
        Modules.get().add(new Canceller());

        // Commands
        Commands.add(new GetAccessToken());
        Commands.add(new ChangeUsername());
        Commands.add(new SpamCommandExec());
        Commands.add(new SpamPos());

        // HUD
        Hud.get().register(Watermark.INFO);
        Hud.get().register(Arraylist.INFO);
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "com.x310.clarity";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("ck-clarity", "internal");
    }
}
