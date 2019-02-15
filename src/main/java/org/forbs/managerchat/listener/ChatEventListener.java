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

    private static final Logger logger = Logger.getLogger("Minecraft");


    private ManagerChat plugin;
    private FileConfiguration config;

    @Inject
    public ChatEventListener(ManagerChat plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
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
        Player player = event.getPlayer();
        Message message = new Message(event.getMessage());

        String prefix = ManagerChat.chat.getPlayerPrefix(player.getWorld().getName(), player);
        String suffix = ManagerChat.chat.getPlayerSuffix(player.getWorld().getName(), player);

        Chat chat = getFormat(plugin.rangeModeEnabled(), message);
        String formattedText = chat
                .replacePlaceholders(player, message, prefix, suffix)
                .colorize()
                .getContext();
        logger.info("Simple context: " + chat.getContext());
        logger.info("Parsed context: " + formattedText);

        if (chat.getFormatType() instanceof LocalFormat) {
            chat.activateLocalChat(player, event);
        }

        event.setFormat(formattedText);
    }

    private Chat getFormat(boolean rangeMode, Message message) {
        FormatInterface format = new DefaultFormat();

        if (rangeMode) {
            if (message.isGlobal()) {
                format = new GlobalFormat();
            } else {
                format = new LocalFormat();
            }
        }

        return new Chat(format, plugin);
    }
}
