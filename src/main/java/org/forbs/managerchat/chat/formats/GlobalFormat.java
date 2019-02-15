package org.forbs.managerchat.chat.formats;

public class GlobalFormat implements FormatInterface {

    private String template;

    public void set(String value) {
        template = value;
    }

    public String get() {
        return template;
    }
}
