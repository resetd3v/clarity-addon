package com.x310.clarity.hud;

import com.x310.clarity.Main;
import meteordevelopment.meteorclient.systems.hud.HudElement;
import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import meteordevelopment.meteorclient.systems.hud.HudRenderer;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.mixin.MinecraftClientAccessor;
import meteordevelopment.meteorclient.utils.world.TickRate;
import net.minecraft.client.network.PlayerListEntry;
import static meteordevelopment.meteorclient.MeteorClient.mc;

public class Watermark extends HudElement {
    private static int ping() {
        if (mc.getNetworkHandler() == null || mc.player == null) return 0;
        PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
        return playerListEntry != null ? playerListEntry.getLatency() : 0;
    }
    public static final HudElementInfo<Watermark> INFO = new HudElementInfo<>(Main.HUD_GROUP, "Watermark", "Watermark", Watermark::new);

    public Watermark() {
        super(INFO);
    }

    @Override
    public void render(HudRenderer renderer) {
        int fps = MinecraftClientAccessor.getFps();
        int tps = (int) TickRate.INSTANCE.getTickRate();
        int ping = ping();

        String text = String.format("clarity.gay | 0.2.0 / %d FPS / %d TPS / %d ms", fps, tps, ping);
        int textWidth = (int) renderer.textWidth(text, true);
        int textHeight = (int) renderer.textHeight(true);

        int paddingX = 6;
        int paddingY = 3;
        int underlineHeight = 2;
        int backgroundOffset = 3;

        int outerWidth = textWidth + paddingX * 2;
        int outerHeight = textHeight + paddingY * 2;

        setSize(outerWidth, outerHeight);

//        renderer.quad(x - backgroundOffset, y - backgroundOffset, outerWidth + backgroundOffset * 2, outerHeight + backgroundOffset * 2, new Color(50, 50, 50, 255));
        renderer.quad(x, y, outerWidth, outerHeight, new Color(0, 0, 0, 255));
        renderer.quad(x, y - underlineHeight, outerWidth, underlineHeight, new Color(255, 255, 255, 255));
        renderer.text(text, x + paddingX, y + paddingY, new Color(255, 255, 255, 255), true);
        renderer.text("clarity.gay", x + paddingX, y + paddingY, new Color(255, 255, 255, 255), true);
    }

}
