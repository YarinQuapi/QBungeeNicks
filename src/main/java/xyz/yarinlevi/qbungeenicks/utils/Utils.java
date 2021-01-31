package xyz.yarinlevi.qbungeenicks.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Utils {
    public static void sendToServer(String message, ServerInfo server) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF("qnicksdata");
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.sendData("qbungee:nicks", stream.toByteArray());
    }


    public static void sendMessage(ProxiedPlayer player, String message, Object... objects) {
        player.sendMessage(new TextComponent(String.format(ChatColor.translateAlternateColorCodes('&', message), objects)));
    }
}
