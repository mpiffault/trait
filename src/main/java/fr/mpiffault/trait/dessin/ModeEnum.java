package fr.mpiffault.trait.dessin;

import lombok.Getter;

import java.awt.event.KeyEvent;

public enum ModeEnum {
    SELECTION("SELECTION", KeyEvent.VK_SPACE, null),
    POINT("POINT", KeyEvent.VK_P, SELECTION),

    SEGMENT("SEGMENT", KeyEvent.VK_S, null),
    SEGMENT_H("SEGMENT HORIZONTAL", KeyEvent.VK_H, SEGMENT),
    SEGMENT_V("SEGMENT VERTICAL", KeyEvent.VK_V, SEGMENT),
    SEGMENT_A("SEGMENT ANGLE", KeyEvent.VK_A, SEGMENT),

    CURVE("CURVE", KeyEvent.VK_Y, null),

    CONSTRUCTION("CONSTRUCTION", KeyEvent.VK_C, null),
    CONSTRUCTION_H("CONSTRUCTION HORIZONTAL", KeyEvent.VK_H, CONSTRUCTION),
    CONSTRUCTION_V("CONSTRUCTION VERTICAL", KeyEvent.VK_V, CONSTRUCTION),
    // TODO : calculate the 2 parallels, and keep the nearest from the cursor
    CONSTRUCTION_P("CONSTRUCTION PARALLEL", KeyEvent.VK_P, CONSTRUCTION),
    CONSTRUCTION_A("CONSTRUCTION ANGLE", KeyEvent.VK_A, CONSTRUCTION),
    CONSTRUCTION_C("CONSTRUCTION CIRCLE", KeyEvent.VK_C, CONSTRUCTION),

    MESURE("MESURE", KeyEvent.VK_M, SELECTION);

    private final static ModeEnum DEFAULT_MODE_ENUM = SELECTION;

    @Getter private int keyEvent;
    @Getter private String name;
    private final ModeEnum parentModeEnum;

    ModeEnum(String name, int keyEvent, ModeEnum requiredModeEnum) {
        this.name = name;
        this.keyEvent = keyEvent;
        this.parentModeEnum = requiredModeEnum;
    }

    public static ModeEnum fromKeyEventAndMode(int keyEvent, ModeEnum currentModeEnum) {
        if (keyEvent == KeyEvent.VK_ESCAPE) {
            return currentModeEnum.getParentModeEnum();
        }
        for (ModeEnum modeEnumValue : values()) {
            if (modeEnumValue.isValidFor(currentModeEnum, keyEvent)) {
                return modeEnumValue;
            }
        }
        return currentModeEnum;
    }

    private boolean isValidFor(ModeEnum currentModeEnum, int keyEvent) {
        return (this.parentModeEnum == null || this.parentModeEnum == currentModeEnum)
                && keyEvent == this.keyEvent;
    }

    public ModeEnum getParentModeEnum() {
        if (this.parentModeEnum != null) {
            return this.parentModeEnum;
        } else {
            return DEFAULT_MODE_ENUM;
        }
    }
}
