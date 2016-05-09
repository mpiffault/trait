package fr.mpiffault.trait.dessin;

import lombok.Getter;

import java.awt.event.KeyEvent;

public enum Mode {
    SELECTION("SELECTION", KeyEvent.VK_SPACE, null),
    POINT("POINT", KeyEvent.VK_P, SELECTION),

    SEGMENT("SEGMENT", KeyEvent.VK_S, null),
    SEGMENT_H("SEGMENT HORIZONTAL", KeyEvent.VK_H, SEGMENT),
    SEGMENT_V("SEGMENT VERTICAL", KeyEvent.VK_V, SEGMENT),
    SEGMENT_A("SEGMENT ANGLE", KeyEvent.VK_A, SEGMENT),

    CONSTRUCTION("CONSTRUCTION", KeyEvent.VK_C, null),
    CONSTRUCTION_H("CONSTRUCTION HORIZONTAL", KeyEvent.VK_H, CONSTRUCTION),
    CONSTRUCTION_V("CONSTRUCTION VERTICAL", KeyEvent.VK_V, CONSTRUCTION),
    CONSTRUCTION_A("CONSTRUCTION ANGLE", KeyEvent.VK_A, CONSTRUCTION),
    CONSTRUCTION_C("CONSTRUCTION CERCLE", KeyEvent.VK_C, CONSTRUCTION),

    MESURE("MESURE", KeyEvent.VK_M, SELECTION);

    private final static Mode DEFAULT_MODE = SELECTION;

    @Getter private int keyEvent;
    @Getter private String name;
    private Mode parentMode;

    Mode(String name, int keyEvent, Mode requiredMode) {
        this.name = name;
        this.keyEvent = keyEvent;
        this.parentMode = requiredMode;
    }

    public static Mode fromEventAndMode(int keyEvent, Mode currentMode) {
        if (keyEvent == KeyEvent.VK_ESCAPE) {
            return currentMode.getParentMode();
        }
        for (Mode modeValue : values()) {
            if (modeValue.isValidFor(currentMode, keyEvent)) {
                return modeValue;
            }
        }
        return currentMode;
    }

    private boolean isValidFor(Mode currentMode, int keyEvent) {
        return (this.parentMode == null || this.parentMode == currentMode)
                && keyEvent == this.keyEvent;
    }

    private Mode getParentMode() {
        if (this.parentMode != null) {
            return this.parentMode;
        } else {
            return DEFAULT_MODE;
        }
    }
}
