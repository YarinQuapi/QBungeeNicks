package xyz.yarinlevi.qbungeenicks.utils;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import xyz.yarinlevi.qbungeenicks.QBungeeNicks;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class CommandCooldownUtils {
    private static final HashMap<ProxiedPlayer, Boolean> cooldowns = new HashMap<>();


    public static boolean isCooldown(ProxiedPlayer player) {
        return cooldowns.getOrDefault(player, false);
    }

    public static void addCooldown(ProxiedPlayer player) {
        cooldowns.put(player, true);

        ProxyServer.getInstance().getScheduler().schedule(QBungeeNicks.getInstance(), () -> cooldowns.remove(player), 5, TimeUnit.SECONDS);
    }
}
