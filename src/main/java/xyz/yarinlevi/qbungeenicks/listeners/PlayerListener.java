package xyz.yarinlevi.qbungeenicks.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import xyz.yarinlevi.qbungeenicks.QBungeeNicks;
import xyz.yarinlevi.qbungeenicks.utils.Utils;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (QBungeeNicks.getMySQLHandler().isInTable(player)) {

            if (player.hasPermission("qbungeenicks.use")) {
                //Check for nicks
                String nick = QBungeeNicks.getMySQLHandler().getNick(player);

                player.setDisplayName(nick);
            }
        } else {
            ProxyServer.getInstance().getScheduler().runAsync(QBungeeNicks.getInstance(), () -> QBungeeNicks.getMySQLHandler().addToTable(player));
        }
    }

    @EventHandler
    public void onPlayerSwitchServer(ServerSwitchEvent event) {
        ProxiedPlayer player = event.getPlayer();

        ServerInfo serverInfo = player.getServer().getInfo();

        if (serverInfo != event.getFrom()) {
            Utils.sendToServer(player.getUniqueId() + "_" + player.getDisplayName(), serverInfo);
        }
    }
}
