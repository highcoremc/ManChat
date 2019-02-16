package org.forbs.managerchat.config;

import org.bukkit.configuration.file.FileConfiguration;

public interface Configuration {

    FileConfiguration get();
    void load();
}
