package xyz.yarinlevi.qbungeenicks.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import xyz.yarinlevi.qbungeenicks.utils.Utils;

public class AdminCommands extends Command {
    public AdminCommands(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(new TextComponent("No arguments detected."));
            return;
        }

        else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("setbungeemode")) {
                sender.sendMessage(new TextComponent("REQUESTING PLAYER DATA FROM CURRENT LOGGED IN SERVER..."));

                if (sender instanceof ProxiedPlayer) {
                    ProxiedPlayer player = (ProxiedPlayer) sender;
                    Utils.sendAdminAccess("REQUEST_OFFLINE_PLAYER_DATA", player.getServer().getInfo());

                    sender.sendMessage(new TextComponent("SENT PLAYER DATA REQUEST.. AWAITING TRANSFER."));
                } else {
                    sender.sendMessage(new TextComponent("REQUEST NOT SENT. MUST BE PLAYER."));
                }
            }
        }
    }
}
