package org.kjussakov.sochorn.model;

import org.kjussakov.sochorn.WebSocket;

public class MessageEvent extends Event {
    private String data;
    private String origin;

    public MessageEvent() {
    }

    public MessageEvent(WebSocket target, WebSocket currentTarget, String type, String data, String origin) {
        super(target, currentTarget, type);
        this.data = data;
        this.origin = origin;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
