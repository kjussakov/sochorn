package org.kjussakov.sochorn.model;

import org.kjussakov.sochorn.WebSocket;

public class CloseEvent extends Event {
    private int code;
    private String reason;
    private boolean wasClean;

    public CloseEvent() {
    }

    public CloseEvent(WebSocket target, WebSocket currentTarget, String type, int code, String reason, boolean wasClean) {
        super(target, currentTarget, type);
        this.code = code;
        this.reason = reason;
        this.wasClean = wasClean;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isWasClean() {
        return wasClean;
    }

    public void setWasClean(boolean wasClean) {
        this.wasClean = wasClean;
    }
}
