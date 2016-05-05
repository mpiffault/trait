package fr.mpiffault.trait.dessin;

import lombok.Getter;

import java.awt.event.KeyEvent;

public enum Mode {
    SELECTION(KeyEvent.VK_SPACE),
    POINT(KeyEvent.VK_P),
    SEGMENT(KeyEvent.VK_S),
    CONSTRUCTION(KeyEvent.VK_C);

    @Getter
    private int keyEvent;

    Mode(int keyEvent) {
        this.keyEvent = keyEvent;
    }

    public static Mode fromEvent(int keyEvent) {
        for (Mode mode : values()) {
            if (keyEvent == mode.keyEvent) {
                return mode;
            }
        }
        return SELECTION;
    }
}
