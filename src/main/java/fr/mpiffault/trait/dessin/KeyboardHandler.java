package fr.mpiffault.trait.dessin;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static java.awt.event.KeyEvent.*;

public class KeyboardHandler extends KeyAdapter {

    private final Table table;

    private boolean typingDigits = false;

    public KeyboardHandler(Table table) {
        super();
        this.table = table;
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        super.keyPressed(keyEvent);

        if (keyEvent.getKeyCode() == VK_ESCAPE && table.ongoingAction()) {
            table.cancelCurrentAction();
        } else {
            performSpecialActions(keyEvent);
            switchMode(keyEvent);
        }
        this.table.repaint();
    }

    private void switchMode(KeyEvent keyEvent) {
        ModeEnum newModeEnum = ModeEnum.fromKeyEventAndMode(keyEvent.getKeyCode(), table.getCurrentMode());
        this.table.setCurrentMode(newModeEnum);
    }

    private void performSpecialActions(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case VK_DELETE:
                this.table.deleteSelectedObjects();
                break;
            case VK_X:
                table.clearConstructionLines();
                break;
            case VK_L:
                table.toggleLengthLog();
                break;
            default:
                break;
        }
        if (keyEvent.getKeyCode() >= VK_0 && keyEvent.getKeyCode() <= VK_9) {
            if (typingDigits) {
                table.setCurrentValue(table.getCurrentValue() * 10.0 + doubleFromDigit(keyEvent));
            } else {
                table.setCurrentValue(doubleFromDigit(keyEvent));
            }
            typingDigits = true;
        } else if (keyEvent.getKeyCode() != VK_SHIFT){
            typingDigits = false;
        }
        this.table.repaint();
    }

    private double doubleFromDigit (KeyEvent keyEvent) {
        return keyEvent.getKeyCode() - VK_0;
    }
}
