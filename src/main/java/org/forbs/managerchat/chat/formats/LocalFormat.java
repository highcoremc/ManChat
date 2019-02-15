package org.forbs.managerchat.chat.formats;

public class LocalFormat implements FormatInterface {

    private String format;

    @Override
    public void set(String value) {
        format = value;
    }

    @Override
    public String get() {
        return format;
    }
}
