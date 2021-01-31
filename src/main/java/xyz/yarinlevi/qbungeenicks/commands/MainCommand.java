package xyz.yarinlevi.qbungeenicks.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import xyz.yarinlevi.qbungeenicks.QBungeeNicks;
import xyz.yarinlevi.qbungeenicks.exceptions.NickDoesNotExistException;
import xyz.yarinlevi.qbungeenicks.utils.Utils;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;

public class MainCommand extends Command {
    public MainCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    Pattern pattern = Pattern.compile("([A-z0-9\\u0590-\\u05fe])+");

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent("Sorry, you are required to be a player to use this command."));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reset")) {
                if (QBungeeNicks.getMySQLHandler().isNicked(player)) {
                    String potentialNick = player.getDisplayName();

                    if (QBungeeNicks.getMySQLHandler().removeNick(player)) {
                        player.setDisplayName(player.getName());
                        Utils.sendToServer(player.getUniqueId() + "_" + player.getDisplayName(), player.getServer().getInfo());

                        try {
                            if (QBungeeNicks.getMySQLHandler().isSelectedNick(potentialNick)) {
                                QBungeeNicks.getMySQLHandler().toggleNick(potentialNick);
                            }

                        } catch (NickDoesNotExistException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    player.sendMessage("You are not nicked!");
                }
            } else if (args[0].equalsIgnoreCase("random")) {
                if (player.hasPermission("qnicks.vip.1")) {

                    ArrayList<String> nickList = QBungeeNicks.getMySQLHandler().getNickList();

                    String randomlySelectedNick = nickList.get(new Random().nextInt(nickList.size()));

                    player.setDisplayName(randomlySelectedNick);

                    Utils.sendToServer(player.getUniqueId() + "_" + player.getDisplayName(), player.getServer().getInfo());

                    //QBungeeNicks.getMySQLHandler().removeNick(player);
                    QBungeeNicks.getMySQLHandler().addNickToMySQL(player, randomlySelectedNick);
                } else {
                    player.sendMessage("You do not have permission to select a random nick.");
                }
            } else {
                if (player.hasPermission("qnicks.vip.3")) {
                    String nick = args[0];

                    if (pattern.matcher(nick).matches()) {
                        if (!QBungeeNicks.getMySQLHandler().isNickTaken(nick)) {
                            player.setDisplayName(nick);

                            Utils.sendToServer(player.getUniqueId() + "_" + player.getDisplayName(), player.getServer().getInfo());

                            //QBungeeNicks.getMySQLHandler().removeNick(player);
                            QBungeeNicks.getMySQLHandler().addNickToMySQL(player, nick);
                        } else {
                            player.sendMessage("There is already someone with that nick!");
                        }
                    } else {
                        player.sendMessage("Invalid characters");
                    }
                } else {
                    player.sendMessage("You do not have permission to set a custom nick!");
                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("select")) {
                String nick = args[1];

                ArrayList<String> nickList = QBungeeNicks.getMySQLHandler().getNickList();

                if (nickList.contains(nick)) {
                    QBungeeNicks.getMySQLHandler().toggleNick(nick);
                    QBungeeNicks.getMySQLHandler().addNickToMySQL(player, nick);

                    Utils.sendToServer(player.getUniqueId() + "_" + player.getDisplayName(), player.getServer().getInfo());
                } else {
                    Utils.sendMessage(player, "Sorry, the nick you selected is taken or does not exist.");
                }
            }
        }
    }
}
