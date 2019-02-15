package org.forbs.managerchat.chat;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.forbs.managerchat.ManagerChat;
import org.forbs.managerchat.chat.formats.DefaultFormat;
import org.forbs.managerchat.chat.formats.FormatInterface;
import org.forbs.managerchat.chat.formats.GlobalFormat;
import org.forbs.managerchat.chat.formats.LocalFormat;
import org.forbs.managerchat.config.Configuration;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class Chat {

    protected static final String PLAYER_PLACEHOLDER = "{player}";
    protected static final String MESSAGE_PLACEHOLDER = "{message}";
    protected static final String PREFIX_PLACEHOLDER = "{prefix}";
    protected static final String SUFFIX_PLACEHOLDER = "{suffix}";

    protected static final String DEFAULT_FORMAT = "<" + PREFIX_PLACEHOLDER + PLAYER_PLACEHOLDER + SUFFIX_PLACEHOLDER + "> " + MESSAGE_PLACEHOLDER;

    protected static final String DEFAULT_FORMAT_CONFIG = "default-format";
    protected static final String GLOBAL_FORMAT_CONFIG = "global-format";
    protected static final String LOCAL_FORMAT_CONFIG = "local-format";

    protected static final String RANGE_RADIUS_CONFIG = "range-radius";
    protected static final double DEFAULT_RANGE_RADIUS = 100;

    @Inject
    protected ManagerChat plugin;

    private FileConfiguration config;

    private FormatInterface format;
    private String output;

    public Chat(FormatInterface format, ManagerChat plugin) {
        this.format = format;
        this.config = plugin.getConfig();

        setFormatText();

        this.output = this.format.get();
    }

    public FormatInterface getFormatType() {
        return this.format;
    }

    public void activateLocalChat(Player sender, AsyncPlayerChatEvent event) {
        List<Player> recipients = getLocalRecipients(sender, getRadius());

        event.getRecipients().clear();
        event.getRecipients().addAll(recipients);
    }

    public double getRadius() {
        if (config == null) {
            return DEFAULT_RANGE_RADIUS;
        }

        return config.getDouble(
                RANGE_RADIUS_CONFIG,
                DEFAULT_RANGE_RADIUS);
    }

    public List<Player> getLocalRecipients(Player sender, double rangeRadius) {
        Location playerLocation = sender.getLocation();
        List<Player> recipients = new ArrayList<>();

        double squaredDistance = Math.pow(rangeRadius, 2);

        for (Player recipient : sender.getWorld().getPlayers()) {
            if (playerLocation.distanceSquared(recipient.getLocation()) > squaredDistance) {
                continue;
            }
            recipients.add(recipient);
        }

        return recipients;
    }

    public Chat setFormatText() {
        String formatText = DEFAULT_FORMAT;

        try {
            if (format instanceof GlobalFormat) {
                formatText = config.getString(GLOBAL_FORMAT_CONFIG);
            } else if (format instanceof LocalFormat) {
                formatText = config.getString(LOCAL_FORMAT_CONFIG);
            } else if (format instanceof DefaultFormat) {
                formatText = config.getString(DEFAULT_FORMAT_CONFIG);
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

        this.format.set(formatText);

        return this;
    }

    public Chat replacePlaceholders(Player p, Message msg, String prefix, String suffix) {
        format.set(
            format.get()
                .replace(PREFIX_PLACEHOLDER, prefix)
                .replace(SUFFIX_PLACEHOLDER, suffix)
                .replace(MESSAGE_PLACEHOLDER, msg.filter().get())
                .replace(PLAYER_PLACEHOLDER, p.getName()));

        return this;
    }

    public Chat colorize() {
        if (format != null) {
            String parsed =
                    ChatColor.translateAlternateColorCodes('&', format.get());
            format.set(parsed);
        }

        return this;
    }

    public String getContext() {
        return format.get();
    }
}
