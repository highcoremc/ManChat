package org.forbs.managerchat.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.forbs.managerchat.ManagerChat;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

final public class FileConfiguration implements Configuration {

    private org.bukkit.configuration.file.FileConfiguration config;
    private ManagerChat plugin;
    private File configFile;

    protected static final String CONFIG_FILE_NAME = "config.yml";

    @Inject
    public FileConfiguration(ManagerChat plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), CONFIG_FILE_NAME);
        this.config = new YamlConfiguration();
    }

    public org.bukkit.configuration.file.FileConfiguration get() {
        if (config == null) {
            plugin.getLogger().log(Level.WARNING, "FileConfiguration doesn't loaded");
        }

        return config;
    }

    public void reload() {
        config = new YamlConfiguration();
        load();
    }

    public void load() {
        if(!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource(CONFIG_FILE_NAME, false);
        }

        try {
            config.load(configFile);
            plugin.getLogger().info("config.yml successfully loaded.");
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getLogger().info("config.yml not found and we load it.");
            e.printStackTrace();
        }
    }
}
