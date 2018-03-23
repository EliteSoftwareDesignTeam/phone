package com.teamness.smane.event;

public class ButtonEvent extends Event {

    public final ButtonAction action;

    public ButtonEvent(ButtonAction action) {
        this.action = action;
    }

    public static enum ButtonAction {
        PRESSED, RELEASED
    }

}
