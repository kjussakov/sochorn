package model;

import org.kjussakov.sochorn.WebSocket;

public class Event {
    private WebSocket target;
    private WebSocket currentTarget;
    private String type;

    public Event() {
    }

    public Event(WebSocket target, WebSocket currentTarget, String type) {
        this.target = target;
        this.currentTarget = currentTarget;
        this.type = type;
    }

    public WebSocket getTarget() {
        return target;
    }

    public void setTarget(WebSocket target) {
        this.target = target;
    }

    public WebSocket getCurrentTarget() {
        return currentTarget;
    }

    public void setCurrentTarget(WebSocket currentTarget) {
        this.currentTarget = currentTarget;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
