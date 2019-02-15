package org.forbs.managerchat.chat;

import javax.annotation.Nonnull;

public class Message {

    private String message;

    public Message(@Nonnull String msg) {
        message = msg;
    }

    public void set(@Nonnull String msg) {
        message = msg;
    }

    public String get() {
        return message;
    }

    public Message filter() {
        message = message.startsWith("!")
                ? message.substring(1)
                : message;

        return this;
    }

    public boolean isGlobal() {
        return message.startsWith("!");
    }
}
