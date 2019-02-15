package org.forbs.managerchat.config;

import org.bukkit.configuration.file.FileConfiguration;

public interface ConfigurationInterface {

    FileConfiguration get();
    void load();
}
