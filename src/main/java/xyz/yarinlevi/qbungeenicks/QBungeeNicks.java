package xyz.yarinlevi.qbungeenicks;

import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import xyz.yarinlevi.qbungeenicks.commands.MainCommand;
import xyz.yarinlevi.qbungeenicks.listeners.PlayerListener;
import xyz.yarinlevi.qbungeenicks.mysql.MySQLHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public final class QBungeeNicks extends Plugin {
    @Getter private static QBungeeNicks instance;

    @Getter private Configuration pluginConfig;
    @Getter private static MySQLHandler mySQLHandler;

    @Override
    public void onEnable() {
        instance = this;

        if (!getDataFolder().exists())
            //noinspection ResultOfMethodCallIgnored
            getDataFolder().mkdir();

        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            //configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File("config.yml"));
            pluginConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mySQLHandler = new MySQLHandler(this);

        this.getProxy().getPluginManager().registerListener(this, new PlayerListener());

        this.getProxy().getPluginManager().registerCommand(this, new MainCommand("qnick", "qbungeenicks.use", "nick"));

        //ProxyServer.getInstance().registerChannel("qbungee:admin_Mc4dXpBc");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
