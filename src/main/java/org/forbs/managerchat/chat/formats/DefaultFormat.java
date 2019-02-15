package org.forbs.managerchat.chat.formats;

public class DefaultFormat implements FormatInterface {

    protected String format;

    public void set(String value) {
        format = value;
    }

    public String get() {
        return format;
    }
}
