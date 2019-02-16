package org.forbs.managerchat.listener;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.forbs.managerchat.ManagerChat;
import org.forbs.managerchat.chat.Chat;
import org.forbs.managerchat.chat.Message;
import org.forbs.managerchat.chat.formats.DefaultFormat;
import org.forbs.managerchat.chat.formats.FormatInterface;
import org.forbs.managerchat.chat.formats.GlobalFormat;
import org.forbs.managerchat.chat.formats.LocalFormat;


import javax.inject.Inject;
import java.util.logging.Logger;

public class ChatEventListener implements Listener {

    private static final Logger LOGGER = Logger.getLogger("ManChat");

    private Chat chat;
    private ManagerChat plugin;
    private FileConfiguration config;

    @Inject
    public ChatEventListener(ManagerChat plugin, Chat chat) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.chat = chat;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatLow(AsyncPlayerChatEvent event) {
        execute(event);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatHigh(AsyncPlayerChatEvent event) {
        execute(event);
    }

    public void execute(AsyncPlayerChatEvent event) {
        Message message = new Message(event.getMessage());
        Player player = event.getPlayer();

        String prefix = ManagerChat.chat.getPlayerPrefix(player.getWorld().getName(), player);
        String suffix = ManagerChat.chat.getPlayerSuffix(player.getWorld().getName(), player);

        FormatInterface format = getFormat(plugin.rangeModeEnabled(), message);

        chat.setFormat(format).setFormatText();

        String formattedText = chat
                .replacePlaceholders(player, message, prefix, suffix)
                .colorize()
                .getContext();

        if (format instanceof LocalFormat) {
            chat.activateLocalChat(player, event);
        }

        event.setFormat(formattedText);
    }

    private FormatInterface getFormat(boolean rangeMode, Message message) {
        FormatInterface format = new DefaultFormat();

        if (rangeMode) {
            if (message.isGlobal()) {
                format = new GlobalFormat();
            } else {
                format = new LocalFormat();
            }
        }

        return format;
    }
}
