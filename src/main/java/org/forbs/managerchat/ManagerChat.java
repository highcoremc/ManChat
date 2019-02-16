package org.forbs.managerchat;

import javax.inject.Inject;

import com.google.inject.Injector;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.forbs.managerchat.config.Configuration;
import org.forbs.managerchat.command.ManagerChatCommand;
import org.forbs.managerchat.listener.ChatEventListener;

import java.util.logging.Logger;

public final class ManagerChat extends JavaPlugin {

    @Inject private ManagerChatCommand command;
    @Inject private Configuration configuration;
    @Inject private ChatEventListener chatListener;

    public static Chat chat = null;

    private static final Logger logger = Logger.getLogger("Minecraft");

    @Override
    public void onDisable() {
        logger.info("is unloaded!");
    }

    @Override
    public void onEnable() {
        onEnable(0);
    }

    public void onEnable(int arg) {
        BinderModule module = new BinderModule(this);
        Injector injector = module.createInjector();
        injector.injectMembers(this);

        this.configuration.load();
        this.setupChat();

        this.getCommand("chat").setExecutor(this.command);
        this.getServer().getPluginManager().registerEvents(this.chatListener, this);

        logger.info("Successfully loaded!");
    }

    public void setupChat() {
        RegisteredServiceProvider<Chat> rsp =
                getServer().getServicesManager().getRegistration(Chat.class);

        chat = rsp.getProvider();
    }

    @Override
    public FileConfiguration getConfig() {
        return configuration.get();
    }

    public boolean rangeModeEnabled() {
        try {
            return configuration
                    .get()
                    .getBoolean("range-mode", false);
        } catch(NullPointerException ex) {
            ex.printStackTrace();
        }

        return false;
    }
}
